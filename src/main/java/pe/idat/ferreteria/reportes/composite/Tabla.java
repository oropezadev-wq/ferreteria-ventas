package pe.idat.ferreteria.reportes.composite;

import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

public class Tabla extends ElementoReporte {
    private final List<String> encabezados;
    private final List<List<String>> filas;

    public Tabla(List<String> encabezados, List<List<String>> filas) {
        this.encabezados = new ArrayList<>(encabezados);
        this.filas = new ArrayList<>();
        for (List<String> fila : filas) {
            this.filas.add(new ArrayList<>(fila));
        }
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<table class=\"table table-sm\"><thead><tr>");
        for (String encabezado : encabezados) {
            html.append("<th>").append(HtmlUtils.htmlEscape(encabezado)).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        if (filas.isEmpty()) {
            html.append("<tr><td colspan=\"").append(encabezados.size()).append("\">Sin datos</td></tr>");
        }
        for (List<String> fila : filas) {
            html.append("<tr>");
            for (String celda : fila) {
                html.append("<td>").append(HtmlUtils.htmlEscape(celda)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        return html.toString();
    }

    @Override
    public int contarElementos() {
        return 1;
    }

    @Override
    public Tabla copiar() {
        return new Tabla(encabezados, filas);
    }
}
