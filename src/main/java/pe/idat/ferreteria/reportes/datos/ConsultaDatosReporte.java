package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.reportes.modelo.Periodo;

import java.util.List;
import java.util.Map;

/**
 * Única puerta por la que los reportes leen datos de ventas e inventario. Así el Builder no depende de
 * cómo se guardan las ventas ni los productos (principio de inversión de dependencias).
 */
public interface ConsultaDatosReporte {

    /** Una fila por cada línea de venta del período, ordenadas por fecha. */
    List<FilaVenta> ventasDelPeriodo(Periodo periodo);

    /** Total vendido por categoría en el período (nombre de categoría → total), de mayor a menor. */
    Map<String, Dinero> totalPorCategoria(Periodo periodo);

    /** Una fila por cada producto con su stock actual valorizado. */
    List<FilaInventario> inventario();
}
