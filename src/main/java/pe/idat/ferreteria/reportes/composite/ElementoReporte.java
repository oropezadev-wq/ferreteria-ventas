package pe.idat.ferreteria.reportes.composite;

import pe.idat.ferreteria.reportes.prototipo.Prototipo;

/**
 * Componente del patrón Composite (diseño "seguro"): solo declara lo que comparten las hojas
 * (Parrafo, Tabla, Grafico, Indicador) y la Seccion; agregar y quitar hijos existe solo en Seccion.
 * <p>
 * Lo implementa Integrante 4.
 */
public abstract class ElementoReporte implements Prototipo<ElementoReporte> {

    /** HTML del elemento; {@code nivel} es la profundidad en el árbol (0 = raíz). Todo texto debe ir escapado. */
    public abstract String renderizarHtml(int nivel);

    /** Cantidad de elementos de este subárbol, incluido el propio elemento. */
    public abstract int contarElementos();

    /** Copia profunda del subárbol. */
    @Override
    public abstract ElementoReporte copiar();
}
