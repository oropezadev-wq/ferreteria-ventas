package pe.idat.ferreteria.reportes.datos;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;
import pe.idat.ferreteria.dominio.venta.VentaRepositorio;
import pe.idat.ferreteria.reportes.modelo.Periodo;

import java.util.List;
import java.util.Map;

/** Lee las ventas y los productos guardados en memoria y los convierte en filas para los reportes. */
@Component
public class ConsultaDatosReporteEnMemoria implements ConsultaDatosReporte {

    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;

    public ConsultaDatosReporteEnMemoria(VentaRepositorio ventas, ProductoRepositorio productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @Override
    public List<FilaVenta> ventasDelPeriodo(Periodo periodo) {
        // TODO (issue Datos para reportes): recorrer ventas.listar(), quedarse con las ventas cuya fecha
        // esté en el período (periodo.contiene(venta.getFechaHora().toLocalDate())) y crear una FilaVenta
        // por cada línea de cada venta. Ordenar por fecha.
        throw new UnsupportedOperationException("Por implementar: ventasDelPeriodo");
    }

    @Override
    public Map<String, Dinero> totalPorCategoria(Periodo periodo) {
        // TODO (issue Datos para reportes): agrupar ventasDelPeriodo(periodo) por categoría sumando los totales.
        // Usar un LinkedHashMap ordenado de mayor a menor total.
        throw new UnsupportedOperationException("Por implementar: totalPorCategoria");
    }

    @Override
    public List<FilaInventario> inventario() {
        // TODO (issue Datos para reportes): una FilaInventario por cada producto de productos.listar():
        // valorizado = precio.multiplicar(stock); bajoMinimo = producto.estaBajoMinimo().
        throw new UnsupportedOperationException("Por implementar: inventario");
    }
}
