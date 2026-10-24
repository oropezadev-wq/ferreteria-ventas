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
        // TODO (issue Composite):
        //  1. Si hijo es null, o es esta misma sección (hijo == this), lanzar IllegalArgumentException.
        //  2. Agregarlo a la lista hijos.
        //  3. return this;
        throw new UnsupportedOperationException("Por implementar: Seccion.agregar");
    }

    @Override
    public String renderizarHtml() {
        // TODO (issue Composite): devolver
        //   "<section><h2>" + Html.escapar(titulo) + "</h2>" + (HTML de cada hijo) + "</section>"
        // Recorrer hijos con un for y llamar hijo.renderizarHtml(): no importa si el hijo es hoja o sección.
        // Tip: usar un StringBuilder para ir juntando el texto.
        throw new UnsupportedOperationException("Por implementar: Seccion.renderizarHtml");
    }

    @Override
    public int contarElementos() {
        // TODO (issue Composite): 1 (esta sección) + la suma de hijo.contarElementos() de cada hijo.
        throw new UnsupportedOperationException("Por implementar: Seccion.contarElementos");
    }

    @Override
    public Seccion copiar() {
        // TODO (issue Composite): copia PROFUNDA.
        //   Seccion copia = new Seccion(titulo);
        //   por cada hijo: copia.agregar(hijo.copiar());   <- copiar el hijo, no agregar el mismo objeto
        //   return copia;
        throw new UnsupportedOperationException("Por implementar: Seccion.copiar");
    }
}
