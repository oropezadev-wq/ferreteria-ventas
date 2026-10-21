package pe.idat.ferreteria.reportes.modelo;

/**
 * Un reporte listo para mostrar. Lo implementan {@link ReporteBase} (lo que arma el Builder) y los
 * decoradores (marca de agua, sello, firma), que envuelven a otro Reporte y le agregan algo.
 */
public interface Reporte {

    String getTitulo();

    /** Contenido del reporte en HTML (sin {@code <html>} ni {@code <body>}: se inserta dentro de una página). */
    String getHtml();
}
