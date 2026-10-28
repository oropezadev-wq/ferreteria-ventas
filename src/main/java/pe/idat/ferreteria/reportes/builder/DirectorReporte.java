package pe.idat.ferreteria.reportes.builder;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.configuracion.FormatoRegional;
import pe.idat.ferreteria.reportes.datos.ConsultaDatosReporte;
import pe.idat.ferreteria.reportes.datos.FilaVenta;
import pe.idat.ferreteria.reportes.datos.FilaInventario;
import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.reportes.modelo.Periodo;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        Periodo periodo = Periodo.de(plantilla.getGranularidad(), fecha);
        ReporteBuilder builder = ReporteBuilder.nuevo(plantilla.getTitulo())
                .subtitulo("Del " + formato.formatearFecha(periodo.desde())
                        + " al " + formato.formatearFecha(periodo.hasta()));
        for (SeccionPlantilla seccion : plantilla.getSecciones()) {
            switch (seccion) {
                case RESUMEN -> agregarResumen(builder, periodo);
                case VENTAS_POR_CATEGORIA -> agregarVentasPorCategoria(builder, periodo);
                case DETALLE_VENTAS -> agregarDetalleVentas(builder, periodo);
                case INVENTARIO -> agregarInventario(builder);
            }
        }
        return builder.build();
    }

    private void agregarResumen(ReporteBuilder builder, Periodo periodo) {
        List<FilaVenta> filas = datos.ventasDelPeriodo(periodo);
        long cantidad = filas.stream().map(FilaVenta::numeroVenta).distinct().count();
        Dinero total = Dinero.soles("0");
        for (FilaVenta fila : filas) {
            total = total.sumar(fila.total());
        }
        builder.seccion("Resumen")
                .parrafo("Ventas registradas: " + cantidad)
                .parrafo("Total vendido: " + formato.formatearDinero(total))
                .finSeccion();
    }

    private void agregarVentasPorCategoria(ReporteBuilder builder, Periodo periodo) {
        Map<String, Dinero> totales = datos.totalPorCategoria(periodo);
        List<List<String>> filas = new ArrayList<>();
        Map<String, BigDecimal> barras = new LinkedHashMap<>();
        for (Map.Entry<String, Dinero> entrada : totales.entrySet()) {
            filas.add(List.of(entrada.getKey(), formato.formatearDinero(entrada.getValue())));
            barras.put(entrada.getKey(), entrada.getValue().monto());
        }
        builder.seccion("Ventas por categoría")
                .tabla(List.of("Categoría", "Total"), filas)
                .grafico("Ventas por categoría", barras)
                .finSeccion();
    }

    private void agregarDetalleVentas(ReporteBuilder builder, Periodo periodo) {
        List<List<String>> filas = new ArrayList<>();
        for (FilaVenta fila : datos.ventasDelPeriodo(periodo)) {
            filas.add(List.of(formato.formatearFecha(fila.fecha()), fila.numeroVenta(), fila.producto(),
                    Integer.toString(fila.cantidad()), formato.formatearDinero(fila.total())));
        }
        builder.seccion("Detalle de ventas")
                .tabla(List.of("Fecha", "N.º", "Producto", "Cantidad", "Total"), filas)
                .finSeccion();
    }

    private void agregarInventario(ReporteBuilder builder) {
        List<FilaInventario> inventario = datos.inventario();
        List<List<String>> filas = new ArrayList<>();
        for (FilaInventario fila : inventario) {
            filas.add(List.of(fila.codigo(), fila.producto(), Integer.toString(fila.stock()),
                    formato.formatearDinero(fila.precio()), formato.formatearDinero(fila.valorizado())));
        }
        long bajos = inventario.stream().filter(FilaInventario::bajoMinimo).count();
        builder.seccion("Inventario")
                .tabla(List.of("Código", "Producto", "Stock", "Precio", "Valorizado"), filas)
                .parrafo("Productos bajo el stock mínimo: " + bajos)
                .finSeccion();
    }
}
