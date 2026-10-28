package pe.idat.ferreteria.aplicacion;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.dominio.comun.RecursoNoEncontradoException;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;
import pe.idat.ferreteria.dominio.comun.StockInsuficienteException;
import pe.idat.ferreteria.dominio.venta.LineaVenta;
import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.dominio.venta.VentaRepositorio;
import pe.idat.ferreteria.dominio.venta.pago.Pago;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;
    private final Clock reloj;

    public VentaServiceImpl(VentaRepositorio ventas, ProductoRepositorio productos, Clock reloj) {
        this.ventas = ventas;
        this.productos = productos;
        this.reloj = reloj;
    }

    /** synchronized: dos ventas a la vez no pueden descontar el mismo stock. */
    @Override
    public synchronized Venta registrar(List<ItemVenta> items, Pago pago) {
        if (items == null || items.isEmpty()) {
            throw new ReglaNegocioException("La venta necesita al menos un producto");
        }
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        List<LineaVenta> lineas = new ArrayList<>();
        for (ItemVenta item : items) {
            if (item == null || item.productoId() == null || item.cantidad() <= 0) {
                throw new IllegalArgumentException("Cada producto necesita una cantidad positiva");
            }
            Producto producto = productos.buscarPorId(item.productoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto", item.productoId()));
            cantidades.merge(item.productoId(), item.cantidad(), Math::addExact);
            if (producto.getStock() < cantidades.get(item.productoId())) {
                throw new StockInsuficienteException("Stock insuficiente de " + producto.getNombre());
            }
            lineas.add(new LineaVenta(producto.getId(), producto.getNombre(), producto.getCategoria(),
                    producto.getPrecio(), item.cantidad()));
        }
        Venta venta = new Venta(ventas.siguienteId(), LocalDateTime.now(reloj), lineas, pago);
        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            Producto producto = productos.buscarPorId(entrada.getKey()).orElseThrow();
            producto.descontarStock(entrada.getValue());
            productos.guardar(producto);
        }
        return ventas.guardar(venta);
    }

    @Override
    public List<Venta> listar() {
        return ventas.listar().stream()
                .sorted(Comparator.comparing(Venta::getFechaHora).reversed())
                .toList();
    }

    @Override
    public Venta buscar(Long id) {
        return ventas.buscarPorId(id).orElseThrow(() -> new RecursoNoEncontradoException("Venta", id));
    }
}
