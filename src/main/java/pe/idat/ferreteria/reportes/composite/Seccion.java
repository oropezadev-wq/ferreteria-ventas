package pe.idat.ferreteria.reportes.composite;

import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

// Contenedor: tiene un título y otros elementos dentro (hojas u otras secciones).
public class Seccion extends ElementoReporte {

    private final String titulo;
    private final List<ElementoReporte> hijos = new ArrayList<>();

    public Seccion(String titulo) {
        this.titulo = titulo;
    }

    public Seccion agregar(ElementoReporte hijo) {
        if (hijo == null || hijo == this) {
            throw new IllegalArgumentException("No se puede agregar ese elemento a la sección");
        }
        hijos.add(hijo);
        return this;
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<section><h2>").append(HtmlUtils.htmlEscape(titulo)).append("</h2>");
        // No importa si el hijo es hoja o sección: todos saben renderizarse.
        for (ElementoReporte hijo : hijos) {
            html.append(hijo.renderizarHtml());
        }
        html.append("</section>");
        return html.toString();
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
        // Copia profunda: se copia cada hijo, no se reutiliza el mismo objeto.
        Seccion copia = new Seccion(titulo);
        for (ElementoReporte hijo : hijos) {
            copia.agregar(hijo.copiar());
        }
        return copia;
    }

    public List<ElementoReporte> getHijos() {
        return new ArrayList<>(hijos);
    }
}
