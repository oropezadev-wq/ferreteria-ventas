package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;

/** Pago con Yape: se registra el celular desde el que se pagó. */
public final class PagoYape extends Pago {

    private final String celular;

    public PagoYape(String celular) {
        // TODO (issue Ventas): el celular debe tener 9 dígitos y empezar con 9
        // (celular.matches("9\\d{8}")). Si no, new PagoInvalidoException("Ingrese un celular válido de 9 dígitos").
        throw new UnsupportedOperationException("Por implementar: constructor de PagoYape");
    }

    @Override
    public void validar(Dinero total) {
        // TODO (issue Ventas): igual que la tarjeta, Yape cobra el total exacto: método vacío con un comentario.
        throw new UnsupportedOperationException("Por implementar: PagoYape.validar");
    }

    @Override
    public String descripcion(Dinero total) {
        // TODO (issue Ventas): return "Yape desde el celular " + celular;
        throw new UnsupportedOperationException("Por implementar: PagoYape.descripcion");
    }
}
