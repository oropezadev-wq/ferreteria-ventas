package pe.idat.ferreteria.dominio.venta;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.EntidadBase;
import pe.idat.ferreteria.dominio.venta.pago.Pago;

import java.math.BigDecimal;
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
        // TODO (issue Ventas):
        //  1. Si fechaHora o pago son null, o lineas es null o vacía: IllegalArgumentException.
        //  2. this.numero = String.format("V-%06d", id);   // por ejemplo V-000012
        //  3. this.fechaHora = fechaHora;  this.lineas = List.copyOf(lineas);  this.pago = pago;
        //  4. pago.validar(getTotal());   // el pago debe cubrir el total (lo valida cada tipo de pago)
        throw new UnsupportedOperationException("Por implementar: constructor de Venta");
    }

    public Dinero getTotal() {
        // TODO (issue Ventas): sumar el subtotal() de cada línea, empezando en Dinero.soles("0").
        throw new UnsupportedOperationException("Por implementar: Venta.getTotal");
    }

    /** Parte del total sin IGV: total / (1 + tasa). Con tasa 0.18 y total 118.00 da 100.00. */
    public Dinero getOpGravada(BigDecimal tasaIgv) {
        // TODO (issue Ventas):
        //   BigDecimal divisor = BigDecimal.ONE.add(tasaIgv);
        //   return Dinero.soles(getTotal().monto().divide(divisor, 2, RoundingMode.HALF_UP));
        throw new UnsupportedOperationException("Por implementar: Venta.getOpGravada");
    }

    /** IGV incluido en el total: total - opGravada (así la suma cuadra al céntimo). */
    public Dinero getIgv(BigDecimal tasaIgv) {
        // TODO (issue Ventas): return getTotal().restar(getOpGravada(tasaIgv));
        throw new UnsupportedOperationException("Por implementar: Venta.getIgv");
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
