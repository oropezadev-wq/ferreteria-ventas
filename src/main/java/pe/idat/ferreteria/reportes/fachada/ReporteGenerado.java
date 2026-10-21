package pe.idat.ferreteria.reportes.fachada;

/**
 * Lo que entrega el {@link ReporteFacade}: título y HTML listos para mostrar.
 *
 * @param huella huella SHA-256 si se pidió la firma; null si no
 */
public record ReporteGenerado(String titulo, String html, String huella) {
}
