package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.PagoInvalidoException;

/** Pago con Yape: se registra el celular desde el que se pagó. */
public final class PagoYape extends Pago {

    private final String celular;

    public PagoYape(String celular) {
        if (celular == null || !celular.matches("9\\d{8}")) {
            throw new PagoInvalidoException("Ingrese un celular válido de 9 dígitos");
        }
        this.celular = celular;
    }

    @Override
    public void validar(Dinero total) {
        // Yape cobra el total exacto; no hay un monto adicional que validar.
    }

    @Override
    public String descripcion(Dinero total) {
        return "Yape desde el celular " + celular;
    }
}
