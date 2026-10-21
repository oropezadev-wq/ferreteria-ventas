package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.time.LocalDate;

/** Datos de una línea de venta tal como los necesita un reporte. */
public record FilaVenta(LocalDate fecha, String numeroVenta, String producto, String categoria, int cantidad,
                        Dinero total) {
}
