package pe.idat.ferreteria.reportes.composite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hoja del Composite: gráfico de barras horizontales hecho solo con HTML y CSS
 * (por ejemplo, ventas por categoría). Las claves son las etiquetas y los valores, los montos.
 */
public final class Grafico extends ElementoReporte {

    private final String titulo;
    private final Map<String, BigDecimal> valores;

    public Grafico(String titulo, Map<String, BigDecimal> valores) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El gráfico necesita un título");
        }
        if (valores == null) {
            throw new IllegalArgumentException("Los valores no pueden ser null (usar un mapa vacío)");
        }
        this.titulo = titulo;
        // LinkedHashMap conserva el orden en que llegaron las barras.
        this.valores = Collections.unmodifiableMap(new LinkedHashMap<>(valores));
    }

    public String getTitulo() {
        return titulo;
    }

    public Map<String, BigDecimal> getValores() {
        return valores;
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<div class=\"grafico\"><h3>")
                .append(Html.escapar(titulo)).append("</h3>");
        if (valores.isEmpty()) {
            return html.append("<p>Sin datos</p></div>").toString();
        }
        BigDecimal maximo = valores.values().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        for (Map.Entry<String, BigDecimal> entrada : valores.entrySet()) {
            int ancho = maximo.signum() == 0 ? 0 : entrada.getValue().multiply(BigDecimal.valueOf(100))
                    .divide(maximo, 0, RoundingMode.HALF_UP).intValue();
            html.append("<div class=\"barra\" style=\"width: ").append(ancho).append("%\">")
                    .append(Html.escapar(entrada.getKey())).append(": ")
                    .append(entrada.getValue().toPlainString()).append("</div>");
        }
        return html.append("</div>").toString();
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
