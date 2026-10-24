package pe.idat.ferreteria.reportes.builder;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.configuracion.FormatoRegional;
import pe.idat.ferreteria.reportes.datos.ConsultaDatosReporte;
import pe.idat.ferreteria.reportes.modelo.Periodo;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;

import java.time.LocalDate;

/**
 * Director del patrón Builder: sabe QUÉ va en cada sección de la plantilla y se lo pide al
 * {@link ReporteBuilder} paso a paso. La plantilla (Prototype) dice qué secciones lleva el reporte;
 * el director las llena con los datos de {@link ConsultaDatosReporte}.
 */
@Component
public class DirectorReporte {

    private final ConsultaDatosReporte datos;
    private final FormatoRegional formato;

    public DirectorReporte(ConsultaDatosReporte datos, FormatoRegional formato) {
        this.datos = datos;
        this.formato = formato;
    }

    public Reporte construir(PlantillaReporte plantilla, LocalDate fecha) {
        // TODO (issue Builder):
        //   Periodo periodo = Periodo.de(plantilla.getGranularidad(), fecha);
        //   ReporteBuilder builder = ReporteBuilder.nuevo(plantilla.getTitulo())
        //           .subtitulo("Del " + formato.formatearFecha(periodo.desde())
        //                   + " al " + formato.formatearFecha(periodo.hasta()));
        //   for (SeccionPlantilla seccion : plantilla.getSecciones()) {
        //       switch (seccion) {
        //           case RESUMEN -> agregarResumen(builder, periodo);
        //           case VENTAS_POR_CATEGORIA -> agregarVentasPorCategoria(builder, periodo);
        //           case DETALLE_VENTAS -> agregarDetalleVentas(builder, periodo);
        //           case INVENTARIO -> agregarInventario(builder);
        //       }
        //   }
        //   return builder.build();
        throw new UnsupportedOperationException("Por implementar: DirectorReporte.construir");
    }

    private void agregarResumen(ReporteBuilder builder, Periodo periodo) {
        // TODO (issue Builder): con datos.ventasDelPeriodo(periodo) calcular cantidad de ventas y total, y:
        //   builder.seccion("Resumen")
        //          .parrafo("Ventas registradas: " + cantidad)
        //          .parrafo("Total vendido: " + formato.formatearDinero(total))
        //          .finSeccion();
    }

    private void agregarVentasPorCategoria(ReporteBuilder builder, Periodo periodo) {
        // TODO (issue Builder): con datos.totalPorCategoria(periodo) armar una sección con una tabla
        // (Categoría | Total) y un gráfico de barras con los mismos valores.
    }

    private void agregarDetalleVentas(ReporteBuilder builder, Periodo periodo) {
        // TODO (issue Builder): sección con una tabla (Fecha | N.º | Producto | Cantidad | Total)
        // a partir de datos.ventasDelPeriodo(periodo).
    }

    private void agregarInventario(ReporteBuilder builder) {
        // TODO (issue Builder): sección con una tabla (Código | Producto | Stock | Precio | Valorizado)
        // a partir de datos.inventario(), y un párrafo con cuántos productos están bajo el stock mínimo.
    }
}
