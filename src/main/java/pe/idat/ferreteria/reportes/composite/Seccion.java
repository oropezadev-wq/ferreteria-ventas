package pe.idat.ferreteria.reportes.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Compuesto del Composite: una sección con título que contiene otras piezas (párrafos, tablas, gráficos
 * u otras secciones). Como una sección también es un {@link ElementoReporte}, se puede anidar sin límite.
 */
public final class Seccion extends ElementoReporte {

    private final String titulo;
    private final List<ElementoReporte> hijos = new ArrayList<>();

    public Seccion(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("La sección necesita un título");
        }
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    /** Copia de solo lectura: desde fuera no se puede alterar la lista interna. */
    public List<ElementoReporte> getHijos() {
        return List.copyOf(hijos);
    }

    /** Agrega una pieza dentro de esta sección. Devuelve la propia sección para poder encadenar llamadas. */
    public Seccion agregar(ElementoReporte hijo) {
        if (hijo == null || hijo == this) {
            throw new IllegalArgumentException("La pieza debe existir y no puede contenerse a sí misma");
        }
        hijos.add(hijo);
        return this;
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<section><h2>").append(Html.escapar(titulo)).append("</h2>");
        for (ElementoReporte hijo : hijos) {
            html.append(hijo.renderizarHtml());
        }
        return html.append("</section>").toString();
    }

    @Override
    public int contarElementos() {
        int total = 1;
        for (ElementoReporte hijo : hijos) {
            total += hijo.contarElementos();
        }
        return total;
    }

    @Override
    public Seccion copiar() {
        Seccion copia = new Seccion(titulo);
        for (ElementoReporte hijo : hijos) {
            copia.agregar(hijo.copiar());
        }
        return copia;
    }
}
