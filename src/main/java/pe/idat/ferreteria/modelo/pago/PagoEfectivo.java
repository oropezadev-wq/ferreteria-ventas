package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PagoEfectivo extends Pago {
    private final BigDecimal recibido;

    public PagoEfectivo(BigDecimal recibido) {
        if (recibido == null) {
            throw new IllegalArgumentException("Indique el monto recibido en efectivo");
        }
        this.recibido = recibido.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void validar(BigDecimal total) {
        if (recibido.compareTo(total) < 0) {
            throw new IllegalArgumentException("El monto recibido (S/ " + recibido + ") no cubre el total (S/ "
                    + total + ")");
        }
    }

    public BigDecimal vuelto(BigDecimal total) {
        return recibido.subtract(total);
    }

    @Override
    public String descripcion(BigDecimal total) {
        return "Efectivo: recibido S/ " + recibido + ", vuelto S/ " + vuelto(total);
    }
}
