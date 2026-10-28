package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;

public class PagoTarjeta extends Pago {

    private final String ultimosCuatro;

    public PagoTarjeta(String ultimosCuatro) {
        if (ultimosCuatro == null || !ultimosCuatro.matches("\\d{4}")) {
            throw new IllegalArgumentException("Ingrese los últimos 4 dígitos de la tarjeta");
        }
        this.ultimosCuatro = ultimosCuatro;
    }

    @Override
    public void validar(BigDecimal total) {
        // El banco cobra el total exacto, no hay nada más que revisar.
    }

    @Override
    public String descripcion(BigDecimal total) {
        return "Tarjeta terminada en " + ultimosCuatro;
    }
}
