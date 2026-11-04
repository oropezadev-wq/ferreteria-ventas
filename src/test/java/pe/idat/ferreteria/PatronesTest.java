package pe.idat.ferreteria;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.reportes.builder.DirectorReporte;
import pe.idat.ferreteria.reportes.builder.ReporteBuilder;
import pe.idat.ferreteria.reportes.composite.Parrafo;
import pe.idat.ferreteria.reportes.composite.Seccion;
import pe.idat.ferreteria.reportes.datos.DatosReporte;
import pe.idat.ferreteria.reportes.decorador.MarcaAguaDecorador;
import pe.idat.ferreteria.reportes.decorador.SelloFerreteriaDecorador;
import pe.idat.ferreteria.reportes.fachada.ReporteFacade;
import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;
import pe.idat.ferreteria.reportes.prototipo.RegistroPlantillas;
import pe.idat.ferreteria.repositorio.ProductoRepositorio;
import pe.idat.ferreteria.repositorio.VentaRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatronesTest {
    @Test
    void singletonDevuelveSiempreLaMismaInstancia() {
        assertSame(ConfiguracionGlobal.getInstancia(), ConfiguracionGlobal.getInstancia());
    }

    @Test
    void prototypeCopiaSinCambiarElOriginal() {
        PlantillaReporte original = new PlantillaReporte("Ventas", Granularidad.DIA)
                .agregarSeccion(SeccionPlantilla.RESUMEN);

        PlantillaReporte copia = original.copiar().agregarSeccion(SeccionPlantilla.DETALLE_VENTAS);

        assertNotSame(original, copia);
        assertEquals(1, original.getSecciones().size());
        assertEquals(2, copia.getSecciones().size());
    }

    @Test
    void compositeCuentaLasSeccionesYSusHijos() {
        Seccion interna = new Seccion("Interna").agregar(new Parrafo("b"));
        Seccion raiz = new Seccion("Raíz").agregar(new Parrafo("a")).agregar(interna);

        assertEquals(4, raiz.contarElementos());
    }

    @Test
    void builderYDecoratorArmanElHtml() {
        Reporte reporte = new ReporteBuilder("Prueba")
                .seccion("Resumen")
                .parrafo("Hola")
                .tabla(List.of("A"), List.of(List.of("1")))
                .build();

        reporte = new MarcaAguaDecorador(new SelloFerreteriaDecorador(reporte), "COPIA");

        String html = reporte.getHtml();
        assertTrue(html.startsWith("<div class=\"marca-agua\">COPIA</div><h1>Prueba</h1>"));
        assertTrue(html.contains("<p>Hola</p>"));
        assertTrue(html.contains("class=\"sello\""));
    }

    @Test
    void facadeGeneraElReporteConUnSoloMetodo() {
        DatosReporte datos = new DatosReporte(new VentaRepositorio(), new ProductoRepositorio());
        ReporteFacade facade = new ReporteFacade(new RegistroPlantillas(), new DirectorReporte(datos));

        String html = facade.generarReporte(TipoReporte.INVENTARIO_VALORIZADO, LocalDate.now(), true, "COPIA", true)
                .getHtml();

        assertTrue(html.contains("Inventario valorizado"));
        assertTrue(html.contains("class=\"sello\""));
        assertTrue(html.contains("class=\"marca-agua\""));
        assertTrue(html.contains("class=\"firma\""));
    }

    @Test
    void singletonConvierteDolaresASoles() {
        // con el tipo de cambio por defecto: 3.75
        assertEquals(new BigDecimal("37.50"), ConfiguracionGlobal.getInstancia().convertirASoles(new BigDecimal("10")));
    }

    @Test
    void builderArmaSubseccionesDentroDeUnaSeccion() {
        String html = new ReporteBuilder("Prueba")
                .seccion("Ingresos")
                .subseccion("Por sede")
                .parrafo("Lima")
                .build()
                .getHtml();

        assertTrue(html.contains("<section><h2>Ingresos</h2><section><h2>Por sede</h2><p>Lima</p></section></section>"));
    }

    @Test
    void reporteTrimestralTieneIngresosGastosYConclusiones() {
        DatosReporte datos = new DatosReporte(new VentaRepositorio(), new ProductoRepositorio());
        ReporteFacade facade = new ReporteFacade(new RegistroPlantillas(), new DirectorReporte(datos));

        String html = facade.generarReporte(TipoReporte.VENTAS_TRIMESTRAL, LocalDate.now(), false, "", false).getHtml();

        assertTrue(html.contains("Ingresos por sede"));
        assertTrue(html.contains("Gastos de compra"));
        assertTrue(html.contains("Conclusiones"));
    }
}
