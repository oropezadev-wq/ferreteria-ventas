package pe.idat.ferreteria.reportes.builder;

import pe.idat.ferreteria.reportes.composite.Seccion;
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
        // TODO (issue Builder): guardar el texto (si es null, dejar "") y return this.
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.subtitulo");
    }

    public ReporteBuilder seccion(String tituloSeccion) {
        // TODO (issue Builder):
        //   Seccion nueva = new Seccion(tituloSeccion);
        //   abiertas.peek().agregar(nueva);   // la nueva queda dentro de la sección actual
        //   abiertas.push(nueva);             // y pasa a ser la sección actual
        //   return this;
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.seccion");
    }

    public ReporteBuilder finSeccion() {
        // TODO (issue Builder): si solo queda la raíz (abiertas.size() == 1), lanzar
        //   new IllegalStateException("No hay ninguna sección abierta para cerrar");
        // si no, abiertas.pop() y return this.
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.finSeccion");
    }

    public ReporteBuilder parrafo(String texto) {
        // TODO (issue Builder): abiertas.peek().agregar(new Parrafo(texto)); return this;
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.parrafo");
    }

    public ReporteBuilder tabla(List<String> encabezados, List<List<String>> filas) {
        // TODO (issue Builder): igual que parrafo, con new Tabla(encabezados, filas).
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.tabla");
    }

    public ReporteBuilder grafico(String tituloGrafico, Map<String, BigDecimal> valores) {
        // TODO (issue Builder): igual que parrafo, con new Grafico(tituloGrafico, valores).
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.grafico");
    }

    /** Entrega el reporte terminado. */
    public Reporte build() {
        // TODO (issue Builder): si quedan secciones abiertas (abiertas.size() > 1), lanzar
        //   new IllegalStateException("Hay secciones sin cerrar");
        // si no: return new ReporteBase(titulo, subtitulo, raiz);
        throw new UnsupportedOperationException("Por implementar: ReporteBuilder.build");
    }
}
