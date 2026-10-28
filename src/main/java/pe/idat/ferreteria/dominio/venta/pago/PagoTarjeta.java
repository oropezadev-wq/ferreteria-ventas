package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.PagoInvalidoException;

/** Pago con tarjeta. Por seguridad solo se guardan los últimos 4 dígitos, nunca el número completo. */
public final class PagoTarjeta extends Pago {

    private final String ultimosCuatro;

    public PagoTarjeta(String ultimosCuatro) {
        if (ultimosCuatro == null || !ultimosCuatro.matches("\\d{4}")) {
            throw new PagoInvalidoException("Ingrese los últimos 4 dígitos de la tarjeta");
        }
        this.ultimosCuatro = ultimosCuatro;
    }

    @Override
    public void validar(Dinero total) {
        // El banco cobra el total exacto; no hay un monto adicional que validar.
    }

    @Override
    public String descripcion(Dinero total) {
        return "Tarjeta terminada en " + ultimosCuatro;
    }
}
