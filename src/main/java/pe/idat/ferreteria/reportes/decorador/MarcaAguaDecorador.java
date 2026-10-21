package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.composite.Html;
import pe.idat.ferreteria.reportes.modelo.Reporte;

/**
 * Decorador que pone una marca de agua (por ejemplo "CONFIDENCIAL") sobre el reporte.
 * <p>
 * EJEMPLO RESUELTO: {@link SelloFerreteriaDecorador} y {@link FirmaDigitalDecorador} siguen esta misma forma.
 */
public final class MarcaAguaDecorador extends DecoradorReporte {

    private final String texto;

    public MarcaAguaDecorador(Reporte envuelto, String texto) {
        super(envuelto);
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("La marca de agua necesita un texto");
        }
        this.texto = texto;
    }

    /** Agrega la marca de agua y debajo pone el HTML del reporte envuelto, sin cambiarlo. */
    @Override
    public String getHtml() {
        return "<div class=\"marca-agua\">" + Html.escapar(texto) + "</div>" + getEnvuelto().getHtml();
    }
}
