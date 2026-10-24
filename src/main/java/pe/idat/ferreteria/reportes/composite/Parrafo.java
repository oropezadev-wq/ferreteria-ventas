package pe.idat.ferreteria.reportes.composite;

/**
 * Hoja del Composite: un párrafo de texto.
 * <p>
 * EJEMPLO RESUELTO: {@link Tabla} y {@link Grafico} siguen esta misma forma.
 */
public final class Parrafo extends ElementoReporte {

    private final String texto;

    public Parrafo(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El párrafo necesita un texto");
        }
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String renderizarHtml() {
        return "<p>" + Html.escapar(texto) + "</p>";
    }

    @Override
    public int contarElementos() {
        return 1;
    }

    /** El texto es un String (no se puede modificar), así que basta con crear otro Parrafo con el mismo texto. */
    @Override
    public Parrafo copiar() {
        return new Parrafo(texto);
    }
}
