package pe.idat.ferreteria.aplicacion;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;
import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.dominio.venta.VentaRepositorio;
import pe.idat.ferreteria.dominio.venta.pago.Pago;

import java.time.Clock;
import java.util.List;

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
        // TODO (issue Ventas), en este orden:
        //  1. Si items es null o vacío: new ReglaNegocioException("La venta necesita al menos un producto").
        //  2. Por cada item: buscar el producto (si no existe, RecursoNoEncontradoException) y comprobar que
        //     producto.getStock() >= item.cantidad(); si no alcanza, StockInsuficienteException.
        //     ¡Todavía NO descontar! Primero se revisan todos.
        //  3. Armar la lista de LineaVenta con los datos de cada producto (id, nombre, categoría, precio, cantidad).
        //  4. Venta venta = new Venta(ventas.siguienteId(), LocalDateTime.now(reloj), lineas, pago);
        //     (el constructor valida el pago: si no alcanza, lanza excepción y no se descontó nada)
        //  5. Recién ahora: producto.descontarStock(cantidad) y productos.guardar(producto) por cada item.
        //  6. return ventas.guardar(venta);
        throw new UnsupportedOperationException("Por implementar: VentaServiceImpl.registrar");
    }

    @Override
    public List<Venta> listar() {
        // TODO (issue Ventas): ventas.listar() ordenada por fecha, de la más reciente a la más antigua.
        throw new UnsupportedOperationException("Por implementar: VentaServiceImpl.listar");
    }

    @Override
    public Venta buscar(Long id) {
        // TODO (issue Ventas): igual que ProductoServiceImpl.buscar, con "Venta".
        throw new UnsupportedOperationException("Por implementar: VentaServiceImpl.buscar");
    }
}
