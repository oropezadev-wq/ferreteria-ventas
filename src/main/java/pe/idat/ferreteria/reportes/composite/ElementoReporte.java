package pe.idat.ferreteria.reportes.composite;

import pe.idat.ferreteria.reportes.prototipo.Prototipo;

// Patrón Composite: hojas (Parrafo, Tabla, Grafico) y contenedores (Seccion) se tratan igual.
public abstract class ElementoReporte implements Prototipo<ElementoReporte> {

    public abstract String renderizarHtml();

    // Cuántos elementos hay contando este y los que tenga dentro.
    public abstract int contarElementos();
}
