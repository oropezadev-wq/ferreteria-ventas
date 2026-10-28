package pe.idat.ferreteria.reportes.composite;

import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

// Hoja: un gráfico de barras hecho con divs.
public class Grafico extends ElementoReporte {

    private final String titulo;
    private final Map<String, BigDecimal> valores;

    public Grafico(String titulo, Map<String, BigDecimal> valores) {
        this.titulo = titulo;
        // LinkedHashMap mantiene el orden de las barras.
        this.valores = new LinkedHashMap<>(valores);
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<div class=\"grafico\"><h3>")
                .append(HtmlUtils.htmlEscape(titulo)).append("</h3>");
        if (valores.isEmpty()) {
            html.append("<p>Sin datos</p>");
        }

        BigDecimal maximo = BigDecimal.ZERO;
        for (BigDecimal valor : valores.values()) {
            if (valor.compareTo(maximo) > 0) {
                maximo = valor;
            }
        }

        for (Map.Entry<String, BigDecimal> entrada : valores.entrySet()) {
            int ancho = 0;
            if (maximo.signum() > 0) {
                ancho = entrada.getValue().multiply(new BigDecimal("100")).divide(maximo, 0, RoundingMode.HALF_UP)
                        .intValue();
            }
            html.append("<div class=\"barra\" style=\"width: ").append(ancho).append("%\">")
                    .append(HtmlUtils.htmlEscape(entrada.getKey())).append(": S/ ").append(entrada.getValue())
                    .append("</div>");
        }
        html.append("</div>");
        return html.toString();
    }

    @Override
    public int contarElementos() {
        return 1;
    }

    @Override
    public Grafico copiar() {
        return new Grafico(titulo, valores);
    }
}
