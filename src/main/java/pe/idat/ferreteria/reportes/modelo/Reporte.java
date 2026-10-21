package pe.idat.ferreteria.reportes.modelo;

import java.util.List;

/**
 * Componente del patrón Decorator: un reporte expresado en partes HTML separadas para que cada decorador
 * agregue lo suyo sin manipular un documento completo. {@code DocumentoHtml} une las partes en un archivo.
 * <p>
 * Lo implementan ReporteBase (Builder, Integrante 3) y los decoradores (Integrante 4).
 */
public interface Reporte {

    String titulo();

    /** Reglas CSS que se insertan en el {@code <style>} del documento. */
    String estilosCss();

    String encabezadoHtml();

    String cuerpoHtml();

    String pieHtml();

    /** Descripción de las capas aplicadas en orden, por ejemplo "Sello de la ferretería". */
    List<String> caracteristicas();
}
