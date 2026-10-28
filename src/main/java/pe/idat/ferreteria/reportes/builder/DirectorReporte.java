package pe.idat.ferreteria.reportes.builder;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.modelo.LineaVenta;
import pe.idat.ferreteria.modelo.Producto;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.reportes.datos.DatosReporte;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DirectorReporte {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DatosReporte datos;

    public DirectorReporte(DatosReporte datos) {
        this.datos = datos;
    }

    public Reporte construir(PlantillaReporte plantilla, LocalDate fecha) {
        LocalDate desde = fecha;
        LocalDate hasta = fecha;
        switch (plantilla.getGranularidad()) {
            case MES -> {
                desde = fecha.withDayOfMonth(1);
                hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());
            }
            case ANIO -> {
                desde = fecha.withDayOfYear(1);
                hasta = fecha.withDayOfYear(fecha.lengthOfYear());
            }
            default -> {
            }
        }

        ReporteBuilder builder = new ReporteBuilder(plantilla.getTitulo());
        if (desde.equals(hasta)) {
            builder.subtitulo("Fecha: " + desde.format(FECHA));
        } else {
            builder.subtitulo("Del " + desde.format(FECHA) + " al " + hasta.format(FECHA));
        }

        List<Venta> ventas = datos.ventasEntre(desde, hasta);
        for (SeccionPlantilla seccion : plantilla.getSecciones()) {
            switch (seccion) {
                case RESUMEN -> agregarResumen(builder, ventas);
                case VENTAS_POR_CATEGORIA -> agregarVentasPorCategoria(builder, ventas);
                case DETALLE_VENTAS -> agregarDetalleVentas(builder, ventas);
                case INVENTARIO -> agregarInventario(builder);
            }
        }
        return builder.build();
    }

    private void agregarResumen(ReporteBuilder builder, List<Venta> ventas) {
        BigDecimal total = BigDecimal.ZERO;
        for (Venta venta : ventas) {
            total = total.add(venta.getTotal());
        }
        builder.seccion("Resumen")
                .parrafo("Ventas registradas: " + ventas.size())
                .parrafo("Total vendido: S/ " + total);
    }

    private void agregarVentasPorCategoria(ReporteBuilder builder, List<Venta> ventas) {
        Map<String, BigDecimal> totales = datos.totalPorCategoria(ventas);
        List<List<String>> filas = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entrada : totales.entrySet()) {
            filas.add(List.of(entrada.getKey(), "S/ " + entrada.getValue()));
        }
        builder.seccion("Ventas por categoría")
                .tabla(List.of("Categoría", "Total"), filas)
                .grafico("Total por categoría", totales);
    }

    private void agregarDetalleVentas(ReporteBuilder builder, List<Venta> ventas) {
        List<List<String>> filas = new ArrayList<>();
        for (Venta venta : ventas) {
            for (LineaVenta linea : venta.getLineas()) {
                filas.add(List.of(venta.getFechaHora().toLocalDate().format(FECHA), venta.getNumero(),
                        linea.getProducto(), String.valueOf(linea.getCantidad()), "S/ " + linea.getSubtotal()));
            }
        }
        builder.seccion("Detalle de ventas")
                .tabla(List.of("Fecha", "N.º", "Producto", "Cantidad", "Subtotal"), filas);
    }

    private void agregarInventario(ReporteBuilder builder) {
        List<List<String>> filas = new ArrayList<>();
        BigDecimal totalValorizado = BigDecimal.ZERO;
        int bajoMinimo = 0;
        for (Producto producto : datos.productos()) {
            BigDecimal valorizado = producto.getPrecio().multiply(BigDecimal.valueOf(producto.getStock()));
            totalValorizado = totalValorizado.add(valorizado);
            if (producto.estaBajoMinimo()) {
                bajoMinimo++;
            }
            filas.add(List.of(producto.getCodigo(), producto.getNombre(), String.valueOf(producto.getStock()),
                    "S/ " + producto.getPrecio(), "S/ " + valorizado));
        }
        builder.seccion("Inventario")
                .tabla(List.of("Código", "Producto", "Stock", "Precio", "Valorizado"), filas)
                .parrafo("Valor total del inventario: S/ " + totalValorizado)
                .parrafo("Productos bajo el stock mínimo: " + bajoMinimo);
    }
}
