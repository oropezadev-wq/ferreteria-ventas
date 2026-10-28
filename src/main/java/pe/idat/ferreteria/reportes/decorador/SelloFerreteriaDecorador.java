package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.configuracion.DatosTienda;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.composite.Html;

/** Decorador que agrega al final del reporte el sello de la ferretería: razón social, RUC y dirección. */
public final class SelloFerreteriaDecorador extends DecoradorReporte {

    private final DatosTienda tienda;

    public SelloFerreteriaDecorador(Reporte envuelto, DatosTienda tienda) {
        super(envuelto);
        if (tienda == null) {
            throw new IllegalArgumentException("El sello necesita los datos de la tienda");
        }
        this.tienda = tienda;
    }

    @Override
    public String getHtml() {
        return getEnvuelto().getHtml() + "<footer class=\"sello\">"
                + Html.escapar(tienda.getRazonSocial()) + " · RUC " + Html.escapar(tienda.getRuc())
                + " · " + Html.escapar(tienda.getDireccion()) + "</footer>";
    }
}
