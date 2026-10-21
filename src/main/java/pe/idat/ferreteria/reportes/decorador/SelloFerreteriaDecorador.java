package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.configuracion.DatosTienda;
import pe.idat.ferreteria.reportes.modelo.Reporte;

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
        // TODO (issue Decorator): seguir el ejemplo de MarcaAguaDecorador, pero el sello va AL FINAL:
        //   getEnvuelto().getHtml()
        //     + "<footer class=\"sello\">" + razón social + " · RUC " + ruc + " · " + dirección + "</footer>"
        // Los datos salen de tienda.getRazonSocial(), tienda.getRuc(), tienda.getDireccion()
        // y cada uno pasa por Html.escapar(...).
        throw new UnsupportedOperationException("Por implementar: SelloFerreteriaDecorador.getHtml");
    }
}
