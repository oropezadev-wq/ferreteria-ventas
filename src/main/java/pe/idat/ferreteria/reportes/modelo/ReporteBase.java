package pe.idat.ferreteria.reportes.modelo;

import pe.idat.ferreteria.reportes.composite.ElementoReporte;
import pe.idat.ferreteria.reportes.composite.Html;
import pe.idat.ferreteria.reportes.composite.Seccion;

/**
 * El producto que entrega el Builder: título, subtítulo y el árbol de secciones (Composite).
 * Es el reporte "sin adornos" que luego pueden envolver los decoradores.
 */
public final class ReporteBase implements Reporte {

    private final String titulo;
    private final String subtitulo;
    private final Seccion contenido;

    public ReporteBase(String titulo, String subtitulo, Seccion contenido) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El reporte necesita un título");
        }
        if (contenido == null) {
            throw new IllegalArgumentException("El reporte necesita contenido");
        }
        this.titulo = titulo;
        this.subtitulo = subtitulo == null ? "" : subtitulo;
        this.contenido = contenido.copiar();
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    @Override
    public String getHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<h1>").append(Html.escapar(titulo)).append("</h1>");
        if (!subtitulo.isBlank()) {
            html.append("<p class=\"subtitulo\">").append(Html.escapar(subtitulo)).append("</p>");
        }
        for (ElementoReporte elemento : contenido.getHijos()) {
            html.append(elemento.renderizarHtml());
        }
        return html.toString();
    }
}
