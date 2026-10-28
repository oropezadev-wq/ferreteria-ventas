package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;

// Cada método de pago valida a su manera y se describe en el ticket.
public abstract class Pago {

    public abstract void validar(BigDecimal total);

    public abstract String descripcion(BigDecimal total);
}
