package pe.idat.ferreteria.reportes.decorador;

import org.springframework.web.util.HtmlUtils;
import pe.idat.ferreteria.reportes.modelo.Reporte;

// Agrega un texto grande y transparente detrás del reporte, por ejemplo "CONFIDENCIAL".
public class MarcaAguaDecorador extends DecoradorReporte {

    private final String texto;

    public MarcaAguaDecorador(Reporte reporte, String texto) {
        super(reporte);
        this.texto = texto;
    }

    @Override
    public String getHtml() {
        return "<div class=\"marca-agua\">" + HtmlUtils.htmlEscape(texto) + "</div>" + reporte.getHtml();
    }
}
