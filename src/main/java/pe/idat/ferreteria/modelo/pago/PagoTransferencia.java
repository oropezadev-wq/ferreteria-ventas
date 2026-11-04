package pe.idat.ferreteria.modelo.pago;

import java.math.BigDecimal;

public class PagoTransferencia extends Pago {
    private final String numeroOperacion;

    public PagoTransferencia(String numeroOperacion) {
        if (numeroOperacion == null || !numeroOperacion.matches("\\d{6,12}")) {
            throw new IllegalArgumentException("Ingrese el número de operación (de 6 a 12 dígitos)");
        }
        this.numeroOperacion = numeroOperacion;
    }

    @Override
    public void validar(BigDecimal total) {
        // la transferencia se hace por el total exacto
    }

    @Override
    public String descripcion(BigDecimal total) {
        return "Transferencia, operación N.º " + numeroOperacion;
    }
}
