package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;

/** Pago con tarjeta. Por seguridad solo se guardan los últimos 4 dígitos, nunca el número completo. */
public final class PagoTarjeta extends Pago {

    private final String ultimosCuatro;

    public PagoTarjeta(String ultimosCuatro) {
        // TODO (issue Ventas): si ultimosCuatro es null o no son exactamente 4 dígitos
        // (ultimosCuatro.matches("\\d{4}") es false), lanzar
        // new PagoInvalidoException("Ingrese los últimos 4 dígitos de la tarjeta"). Si no, asignarlo.
        throw new UnsupportedOperationException("Por implementar: constructor de PagoTarjeta");
    }

    @Override
    public void validar(Dinero total) {
        // TODO (issue Ventas): el banco cobra el total exacto, así que no hay nada más que validar.
        // Dejar el método vacío y explicarlo con un comentario.
        throw new UnsupportedOperationException("Por implementar: PagoTarjeta.validar");
    }

    @Override
    public String descripcion(Dinero total) {
        // TODO (issue Ventas): return "Tarjeta terminada en " + ultimosCuatro;
        throw new UnsupportedOperationException("Por implementar: PagoTarjeta.descripcion");
    }
}
