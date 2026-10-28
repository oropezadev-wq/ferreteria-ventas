package pe.idat.ferreteria;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.reportes.builder.ReporteBuilder;
import pe.idat.ferreteria.reportes.composite.Parrafo;
import pe.idat.ferreteria.reportes.composite.Seccion;
import pe.idat.ferreteria.reportes.decorador.MarcaAguaDecorador;
import pe.idat.ferreteria.reportes.decorador.SelloFerreteriaDecorador;
import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Una prueba sencilla por patrón.
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

        // raíz + párrafo a + sección interna + párrafo b
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
}
