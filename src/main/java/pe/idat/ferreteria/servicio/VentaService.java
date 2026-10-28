package pe.idat.ferreteria.servicio;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.modelo.LineaVenta;
import pe.idat.ferreteria.modelo.Producto;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.modelo.pago.Pago;
import pe.idat.ferreteria.repositorio.VentaRepositorio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepositorio repositorio;
    private final ProductoService productos;

    public VentaService(VentaRepositorio repositorio, ProductoService productos) {
        this.repositorio = repositorio;
        this.productos = productos;
    }

    public Venta registrar(List<ItemVenta> items, Pago pago) {
        return registrar(items, pago, LocalDateTime.now());
    }

    // synchronized: así dos ventas a la vez no descuentan el mismo stock.
    public synchronized Venta registrar(List<ItemVenta> items, Pago pago, LocalDateTime fechaHora) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La venta necesita al menos un producto");
        }

        // 1. Revisar que haya stock de todo antes de descontar nada.
        List<LineaVenta> lineas = new ArrayList<>();
        for (ItemVenta item : items) {
            Producto producto = productos.buscar(item.getProductoId());
            int pedido = item.getCantidad() + cantidadYaPedida(lineas, producto.getId());
            if (pedido > producto.getStock()) {
                throw new IllegalArgumentException("Stock insuficiente de " + producto.getNombre() + ": hay "
                        + producto.getStock() + " y se pidieron " + pedido);
            }
            lineas.add(new LineaVenta(producto.getId(), producto.getNombre(), producto.getCategoria(),
                    producto.getPrecio(), item.getCantidad()));
        }

        // 2. Crear la venta: aquí se valida el pago.
        Venta venta = new Venta(repositorio.siguienteId(), fechaHora, lineas, pago);

        // 3. Recién ahora se descuenta el stock y se guarda.
        for (LineaVenta linea : lineas) {
            productos.buscar(linea.getProductoId()).descontarStock(linea.getCantidad());
        }
        repositorio.guardar(venta);
        return venta;
    }

    // De la más reciente a la más antigua.
    public List<Venta> listar() {
        List<Venta> ventas = repositorio.listar();
        ventas.sort(Comparator.comparing(Venta::getFechaHora).reversed());
        return ventas;
    }

    public Venta buscar(Long id) {
        Venta venta = repositorio.buscarPorId(id);
        if (venta == null) {
            throw new IllegalArgumentException("No existe la venta con id " + id);
        }
        return venta;
    }

    private int cantidadYaPedida(List<LineaVenta> lineas, Long productoId) {
        int total = 0;
        for (LineaVenta linea : lineas) {
            if (linea.getProductoId().equals(productoId)) {
                total += linea.getCantidad();
            }
        }
        return total;
    }
}
