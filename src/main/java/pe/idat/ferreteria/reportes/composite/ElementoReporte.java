package pe.idat.ferreteria.reportes.composite;

import pe.idat.ferreteria.reportes.prototipo.Prototipo;

// Patrón Composite
public abstract class ElementoReporte implements Prototipo<ElementoReporte> {
    public abstract String renderizarHtml();

    public abstract int contarElementos();
}
