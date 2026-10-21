package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.PagoInvalidoException;

/**
 * Pago en efectivo: el monto recibido debe cubrir el total y se calcula el vuelto.
 * <p>
 * EJEMPLO RESUELTO: {@link PagoTarjeta} y {@link PagoYape} siguen esta misma forma.
 */
public final class PagoEfectivo extends Pago {

    private final Dinero recibido;

    public PagoEfectivo(Dinero recibido) {
        if (recibido == null) {
            throw new PagoInvalidoException("Indique el monto recibido en efectivo");
        }
        this.recibido = recibido;
    }

    @Override
    public void validar(Dinero total) {
        if (!recibido.esMayorOIgualQue(total)) {
            throw new PagoInvalidoException("El monto recibido (" + recibido + ") no cubre el total (" + total + ")");
        }
    }

    public Dinero vuelto(Dinero total) {
        return recibido.restar(total);
    }

    @Override
    public String descripcion(Dinero total) {
        return "Efectivo: recibido " + recibido + ", vuelto " + vuelto(total);
    }
}
