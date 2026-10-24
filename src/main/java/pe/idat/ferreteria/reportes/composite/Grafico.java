package pe.idat.ferreteria.reportes.composite;

import java.math.BigDecimal;
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
        // TODO (issue Composite): armar
        //   <div class="grafico"><h3>titulo</h3>
        //     <div class="barra" style="width: NN%">etiqueta: valor</div>   (una por cada entrada del mapa)
        //   </div>
        // NN = valor * 100 / (el valor más grande del mapa), redondeado a entero. Si el mapa está vacío,
        // mostrar solo el título y un <p>Sin datos</p>. Escapar título y etiquetas con Html.escapar(...).
        throw new UnsupportedOperationException("Por implementar: Grafico.renderizarHtml");
    }

    @Override
    public int contarElementos() {
        // TODO (issue Composite): un gráfico es una hoja.
        throw new UnsupportedOperationException("Por implementar: Grafico.contarElementos");
    }

    @Override
    public Grafico copiar() {
        // TODO (issue Composite): crear otro Grafico con el mismo título y valores
        // (el constructor ya copia el mapa).
        throw new UnsupportedOperationException("Por implementar: Grafico.copiar");
    }
}
