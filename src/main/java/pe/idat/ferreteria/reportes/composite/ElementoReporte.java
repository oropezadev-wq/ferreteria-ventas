package pe.idat.ferreteria.reportes.composite;

import pe.idat.ferreteria.reportes.prototipo.Prototipo;

/**
 * Patrón Composite: lo que tienen en común todas las piezas de un reporte.
 * <p>
 * Las hojas ({@link Parrafo}, {@link Tabla}, {@link Grafico}) y la {@link Seccion}, que contiene otras piezas,
 * se usan igual: quien arma o muestra el reporte llama a {@link #renderizarHtml()} sin preguntar qué tipo de
 * pieza es. Por eso una sección puede tener secciones dentro, sin límite de niveles.
 */
public abstract class ElementoReporte implements Prototipo<ElementoReporte> {

    /** HTML de la pieza. Todo texto que venga de los datos debe pasar por {@link Html#escapar(String)}. */
    public abstract String renderizarHtml();

    /** Cuántas piezas hay en este elemento, contándose a sí mismo (una hoja devuelve 1). */
    public abstract int contarElementos();

    /** Copia profunda: la copia no comparte con el original nada que se pueda modificar. */
    @Override
    public abstract ElementoReporte copiar();
}
