package pe.idat.ferreteria.reportes.builder;

import pe.idat.ferreteria.reportes.composite.Seccion;
import pe.idat.ferreteria.reportes.composite.Parrafo;
import pe.idat.ferreteria.reportes.composite.Tabla;
import pe.idat.ferreteria.reportes.composite.Grafico;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.ReporteBase;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Patrón Builder: arma un reporte paso a paso con una API fluida (cada método devuelve el builder):
 * <pre>
 * Reporte r = ReporteBuilder.nuevo("Ventas del mes")
 *         .subtitulo("Del 01/10/2026 al 31/10/2026")
 *         .seccion("Resumen")
 *             .parrafo("Ventas: 120")
 *         .finSeccion()
 *         .build();
 * </pre>
 * {@code seccion(...)} abre una sección dentro de la actual; todo lo que se agregue después va dentro de ella
 * hasta {@code finSeccion()}. Así se pueden anidar secciones sin límite.
 */
public final class ReporteBuilder {

    private final String titulo;
    private String subtitulo = "";
    private final Seccion raiz;
    /** Pila de secciones abiertas: la de arriba es donde se agregan las piezas. La raíz siempre está abajo. */
    private final Deque<Seccion> abiertas = new ArrayDeque<>();

    private ReporteBuilder(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El reporte necesita un título");
        }
        this.titulo = titulo;
        this.raiz = new Seccion(titulo);
        this.abiertas.push(raiz);
    }

    public static ReporteBuilder nuevo(String titulo) {
        return new ReporteBuilder(titulo);
    }

    public ReporteBuilder subtitulo(String texto) {
        subtitulo = texto == null ? "" : texto;
        return this;
    }

    public ReporteBuilder seccion(String tituloSeccion) {
        Seccion nueva = new Seccion(tituloSeccion);
        abiertas.peek().agregar(nueva);
        abiertas.push(nueva);
        return this;
    }

    public ReporteBuilder finSeccion() {
        if (abiertas.size() == 1) {
            throw new IllegalStateException("No hay ninguna sección abierta para cerrar");
        }
        abiertas.pop();
        return this;
    }

    public ReporteBuilder parrafo(String texto) {
        abiertas.peek().agregar(new Parrafo(texto));
        return this;
    }

    public ReporteBuilder tabla(List<String> encabezados, List<List<String>> filas) {
        abiertas.peek().agregar(new Tabla(encabezados, filas));
        return this;
    }

    public ReporteBuilder grafico(String tituloGrafico, Map<String, BigDecimal> valores) {
        abiertas.peek().agregar(new Grafico(tituloGrafico, valores));
        return this;
    }

    /** Entrega el reporte terminado. */
    public Reporte build() {
        if (abiertas.size() > 1) {
            throw new IllegalStateException("Hay secciones sin cerrar");
        }
        return new ReporteBase(titulo, subtitulo, raiz);
    }
}
