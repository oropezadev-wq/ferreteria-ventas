package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;

public abstract class Pago {
    public abstract void validar(BigDecimal total);

    public abstract String descripcion(BigDecimal total);
}
