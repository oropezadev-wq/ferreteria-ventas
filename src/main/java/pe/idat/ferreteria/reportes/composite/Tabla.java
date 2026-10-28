package pe.idat.ferreteria.reportes.composite;

import java.util.List;

/** Hoja del Composite: una tabla con encabezados y filas de texto. */
public final class Tabla extends ElementoReporte {

    private final List<String> encabezados;
    private final List<List<String>> filas;

    public Tabla(List<String> encabezados, List<List<String>> filas) {
        if (encabezados == null || encabezados.isEmpty()) {
            throw new IllegalArgumentException("La tabla necesita al menos un encabezado");
        }
        if (filas == null) {
            throw new IllegalArgumentException("Las filas no pueden ser null (usar una lista vacía)");
        }
        // List.copyOf crea listas que no se pueden modificar: nadie puede cambiar la tabla después de creada.
        this.encabezados = List.copyOf(encabezados);
        this.filas = filas.stream().map(List::copyOf).toList();
    }

    public List<String> getEncabezados() {
        return encabezados;
    }

    public List<List<String>> getFilas() {
        return filas;
    }

    @Override
    public String renderizarHtml() {
        StringBuilder html = new StringBuilder("<table class=\"table table-sm\"><thead><tr>");
        for (String encabezado : encabezados) {
            html.append("<th>").append(Html.escapar(encabezado)).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (List<String> fila : filas) {
            html.append("<tr>");
            for (String celda : fila) {
                html.append("<td>").append(Html.escapar(celda)).append("</td>");
            }
            html.append("</tr>");
        }
        return html.append("</tbody></table>").toString();
    }

    @Override
    public int contarElementos() {
        return 1;
    }

    @Override
    public Tabla copiar() {
        return new Tabla(encabezados, filas);
    }
}
