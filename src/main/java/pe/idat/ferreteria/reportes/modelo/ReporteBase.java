package pe.idat.ferreteria.reportes.modelo;

import org.springframework.web.util.HtmlUtils;
import pe.idat.ferreteria.reportes.composite.Seccion;

import java.util.ArrayList;
import java.util.List;

public class ReporteBase implements Reporte {
    private final String titulo;
    private final String subtitulo;
    private final List<Seccion> secciones;

    public ReporteBase(String titulo, String subtitulo, List<Seccion> secciones) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.secciones = new ArrayList<>(secciones);
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public String getHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<h1>").append(HtmlUtils.htmlEscape(titulo)).append("</h1>");
        if (subtitulo != null && !subtitulo.isBlank()) {
            html.append("<p class=\"subtitulo\">").append(HtmlUtils.htmlEscape(subtitulo)).append("</p>");
        }
        for (Seccion seccion : secciones) {
            html.append(seccion.renderizarHtml());
        }
        return html.toString();
    }
}
