package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;

public class PagoYape extends Pago {
    private final String celular;

    public PagoYape(String celular) {
        if (celular == null || !celular.matches("9\\d{8}")) {
            throw new IllegalArgumentException("Ingrese un celular válido de 9 dígitos");
        }
        this.celular = celular;
    }

    @Override
    public void validar(BigDecimal total) {
        // yape cobra el total exacto
    }

    @Override
    public String descripcion(BigDecimal total) {
        return "Yape desde el celular " + celular;
    }
}
