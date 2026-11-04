package pe.idat.ferreteria.reportes.builder;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
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
            case TRIMESTRE -> {
                int primerMes = ((fecha.getMonthValue() - 1) / 3) * 3 + 1;
                desde = fecha.withMonth(primerMes).withDayOfMonth(1);
                hasta = desde.plusMonths(3).minusDays(1);
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
            builder.subtitulo("Fecha: " + desde.format(FECHA) + " - Todas las sedes - Montos en soles");
        } else {
            builder.subtitulo("Del " + desde.format(FECHA) + " al " + hasta.format(FECHA)
                    + " - Todas las sedes - Montos en soles");
        }

        List<Venta> ventas = datos.ventasEntre(desde, hasta);
        for (SeccionPlantilla seccion : plantilla.getSecciones()) {
            switch (seccion) {
                case RESUMEN -> agregarResumen(builder, ventas);
                case INGRESOS -> agregarIngresos(builder, ventas);
                case GASTOS -> agregarGastos(builder, ventas);
                case DETALLE_VENTAS -> agregarDetalleVentas(builder, ventas);
                case INVENTARIO -> agregarInventario(builder);
                case CONCLUSIONES -> agregarConclusiones(builder, ventas);
            }
        }
        return builder.build();
    }

    private void agregarResumen(ReporteBuilder builder, List<Venta> ventas) {
        builder.seccion("Resumen")
                .parrafo("Ventas registradas: " + ventas.size())
                .parrafo("Total vendido: S/ " + total(ventas));
    }

    // Composite: la sección "Ingresos" tiene dos subsecciones
    private void agregarIngresos(ReporteBuilder builder, List<Venta> ventas) {
        Map<String, BigDecimal> porSede = datos.totalPorSede(ventas);
        Map<String, BigDecimal> porCategoria = datos.totalPorCategoria(ventas);
        builder.seccion("Ingresos")
                .subseccion("Ingresos por sede")
                .tabla(List.of("Sede", "Total"), filas(porSede))
                .grafico("Comparación por sede", porSede)
                .subseccion("Ingresos por categoría")
                .tabla(List.of("Categoría", "Total"), filas(porCategoria))
                .grafico("Comparación por categoría", porCategoria);
    }

    private void agregarGastos(ReporteBuilder builder, List<Venta> ventas) {
        BigDecimal costo = BigDecimal.ZERO;
        for (Venta venta : ventas) {
            costo = costo.add(venta.getCosto());
        }
        BigDecimal tipoCambio = ConfiguracionGlobal.getInstancia().getTipoCambio();
        builder.seccion("Gastos de compra")
                .parrafo("Los productos se compran en dólares y se pasan a soles con el tipo de cambio S/ " + tipoCambio)
                .tabla(List.of("Categoría", "Costo de lo vendido"), filas(datos.costoPorCategoria(ventas)))
                .parrafo("Total de gastos: S/ " + costo)
                .parrafo("Ganancia (ingresos - gastos): S/ " + total(ventas).subtract(costo));
    }

    private void agregarDetalleVentas(ReporteBuilder builder, List<Venta> ventas) {
        List<List<String>> filas = new ArrayList<>();
        for (Venta venta : ventas) {
            for (LineaVenta linea : venta.getLineas()) {
                filas.add(List.of(venta.getFechaHora().toLocalDate().format(FECHA), venta.getNumero(),
                        venta.getSede().getNombre(), linea.getProducto(), String.valueOf(linea.getCantidad()),
                        "S/ " + linea.getSubtotal()));
            }
        }
        builder.seccion("Detalle de ventas")
                .tabla(List.of("Fecha", "N.º", "Sede", "Producto", "Cantidad", "Subtotal"), filas);
    }

    private void agregarInventario(ReporteBuilder builder) {
        List<List<String>> filas = new ArrayList<>();
        BigDecimal totalValorizado = BigDecimal.ZERO;
        int bajoMinimo = 0;
        for (Producto producto : datos.productos()) {
            BigDecimal valorizado = producto.getCostoSoles().multiply(BigDecimal.valueOf(producto.getStock()));
            totalValorizado = totalValorizado.add(valorizado);
            if (producto.estaBajoMinimo()) {
                bajoMinimo++;
            }
            filas.add(List.of(producto.getCodigo(), producto.getNombre(), String.valueOf(producto.getStock()),
                    "US$ " + producto.getCostoDolares(), "S/ " + producto.getCostoSoles(), "S/ " + valorizado));
        }
        builder.seccion("Inventario")
                .tabla(List.of("Código", "Producto", "Stock", "Costo (US$)", "Costo (S/)", "Valorizado"), filas)
                .parrafo("Valor total del inventario (al costo): S/ " + totalValorizado)
                .parrafo("Productos bajo el stock mínimo: " + bajoMinimo);
    }

    private void agregarConclusiones(ReporteBuilder builder, List<Venta> ventas) {
        builder.seccion("Conclusiones");
        if (ventas.isEmpty()) {
            builder.parrafo("No hubo ventas en este período.");
            return;
        }
        builder.parrafo("La sede con más ingresos fue " + mayor(datos.totalPorSede(ventas)) + ".")
                .parrafo("La categoría con más ingresos fue " + mayor(datos.totalPorCategoria(ventas)) + ".");
    }

    private BigDecimal total(List<Venta> ventas) {
        BigDecimal total = BigDecimal.ZERO;
        for (Venta venta : ventas) {
            total = total.add(venta.getTotal());
        }
        return total;
    }

    private List<List<String>> filas(Map<String, BigDecimal> valores) {
        List<List<String>> filas = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entrada : valores.entrySet()) {
            filas.add(List.of(entrada.getKey(), "S/ " + entrada.getValue()));
        }
        return filas;
    }

    private String mayor(Map<String, BigDecimal> valores) {
        String nombre = "";
        BigDecimal maximo = null;
        for (Map.Entry<String, BigDecimal> entrada : valores.entrySet()) {
            if (maximo == null || entrada.getValue().compareTo(maximo) > 0) {
                maximo = entrada.getValue();
                nombre = entrada.getKey();
            }
        }
        return nombre + " (S/ " + maximo + ")";
    }
}
