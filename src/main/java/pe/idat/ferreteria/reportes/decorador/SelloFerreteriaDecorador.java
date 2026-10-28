package pe.idat.ferreteria.reportes.decorador;

import org.springframework.web.util.HtmlUtils;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.reportes.modelo.Reporte;

public class SelloFerreteriaDecorador extends DecoradorReporte {
    public SelloFerreteriaDecorador(Reporte reporte) {
        super(reporte);
    }

    @Override
    public String getHtml() {
        ConfiguracionGlobal tienda = ConfiguracionGlobal.getInstancia();
        return reporte.getHtml()
                + "<footer class=\"sello\">" + HtmlUtils.htmlEscape(tienda.getRazonSocial())
                + " · RUC " + HtmlUtils.htmlEscape(tienda.getRuc())
                + " · " + HtmlUtils.htmlEscape(tienda.getDireccion()) + "</footer>";
    }
}
