package pe.idat.ferreteria.reportes.composite;

import org.springframework.web.util.HtmlUtils;

public class Parrafo extends ElementoReporte {
    private final String texto;

    public Parrafo(String texto) {
        this.texto = texto;
    }

    @Override
    public String renderizarHtml() {
        return "<p>" + HtmlUtils.htmlEscape(texto) + "</p>";
    }

    @Override
    public int contarElementos() {
        return 1;
    }

    @Override
    public Parrafo copiar() {
        return new Parrafo(texto);
    }
}
