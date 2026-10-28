package pe.idat.ferreteria.dominio.venta;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.EntidadBase;
import pe.idat.ferreteria.dominio.venta.pago.Pago;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Una venta: sus líneas, el pago y la fecha. Los precios ya incluyen IGV; el IGV se calcula "hacia atrás"
 * desde el total para mostrarlo en el ticket.
 */
public class Venta extends EntidadBase {

    private final String numero;
    private final LocalDateTime fechaHora;
    private final List<LineaVenta> lineas;
    private final Pago pago;

    public Venta(Long id, LocalDateTime fechaHora, List<LineaVenta> lineas, Pago pago) {
        super(id);
        if (fechaHora == null || pago == null || lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("La venta necesita fecha, líneas y pago");
        }
        this.numero = String.format("V-%06d", id);
        this.fechaHora = fechaHora;
        this.lineas = List.copyOf(lineas);
        this.pago = pago;
        pago.validar(getTotal());
    }

    public Dinero getTotal() {
        Dinero total = Dinero.soles("0");
        for (LineaVenta linea : lineas) {
            total = total.sumar(linea.subtotal());
        }
        return total;
    }

    /** Parte del total sin IGV: total / (1 + tasa). Con tasa 0.18 y total 118.00 da 100.00. */
    public Dinero getOpGravada(BigDecimal tasaIgv) {
        if (tasaIgv == null || tasaIgv.signum() < 0) {
            throw new IllegalArgumentException("La tasa de IGV debe ser no negativa");
        }
        BigDecimal divisor = BigDecimal.ONE.add(tasaIgv);
        return Dinero.soles(getTotal().monto().divide(divisor, 2, RoundingMode.HALF_UP));
    }

    /** IGV incluido en el total: total - opGravada (así la suma cuadra al céntimo). */
    public Dinero getIgv(BigDecimal tasaIgv) {
        return getTotal().restar(getOpGravada(tasaIgv));
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public List<LineaVenta> getLineas() {
        return lineas;
    }

    public Pago getPago() {
        return pago;
    }
}
