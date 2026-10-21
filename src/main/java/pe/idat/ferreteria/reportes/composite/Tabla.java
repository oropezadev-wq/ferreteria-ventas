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
        // TODO (issue Composite): armar
        //   <table class="table table-sm"><thead><tr><th>Encabezado</th>...</tr></thead>
        //   <tbody><tr><td>celda</td>...</tr>...</tbody></table>
        // Cada encabezado y cada celda pasan por Html.escapar(...). Seguir el ejemplo de Parrafo.
        throw new UnsupportedOperationException("Por implementar: Tabla.renderizarHtml");
    }

    @Override
    public int contarElementos() {
        // TODO (issue Composite): una tabla es una hoja.
        throw new UnsupportedOperationException("Por implementar: Tabla.contarElementos");
    }

    @Override
    public Tabla copiar() {
        // TODO (issue Composite): crear otra Tabla con los mismos encabezados y filas
        // (el constructor ya hace copias de las listas).
        throw new UnsupportedOperationException("Por implementar: Tabla.copiar");
    }
}
