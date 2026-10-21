package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.Periodo;

import java.util.List;

/**
 * Única puerta por la que el módulo de reportes lee datos de ventas e inventario; así los ensambladores
 * no dependen de los repositorios ni de las entidades. Devuelve solo records de lectura.
 * <p>
 * La implementa Integrante 3 (con los repositorios de productos y ventas).
 */
public interface ConsultaDatosReporte {

    /**
     * Totales de las ventas no anuladas del período y su serie por tramos.
     *
     * @param tramos tamaño de cada punto de la serie (DIA dentro de un mes, MES dentro de un año...)
     */
    ResumenVentas resumenVentas(Periodo periodo, Granularidad tramos);

    /** Ventas no anuladas del período agrupadas por categoría de producto, de mayor a menor total. */
    List<VentasPorCategoria> ventasPorCategoria(Periodo periodo);

    /** Stock actual de los productos activos valorizado a su costo unitario. */
    InventarioValorizado inventarioValorizado();
}
