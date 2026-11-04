package pe.idat.ferreteria.modelo;

import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.modelo.pago.Pago;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private final Long id;
    private final String numero;
    private final LocalDateTime fechaHora;
    private final Sede sede;
    private final String cliente;
    private final String vendedor;
    private final List<LineaVenta> lineas;
    private final Pago pago;

    public Venta(Long id, LocalDateTime fechaHora, Sede sede, String cliente, String vendedor, List<LineaVenta> lineas,
                 Pago pago) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("La venta necesita al menos un producto");
        }
        if (sede == null) {
            throw new IllegalArgumentException("Elija la sede");
        }
        if (vendedor == null || vendedor.isBlank()) {
            throw new IllegalArgumentException("Ingrese el vendedor");
        }
        if (pago == null) {
            throw new IllegalArgumentException("Elija un método de pago");
        }
        this.id = id;
        this.numero = String.format("V-%06d", id);
        this.fechaHora = fechaHora;
        this.sede = sede;
        this.cliente = cliente == null || cliente.isBlank() ? "Cliente general" : cliente.trim();
        this.vendedor = vendedor.trim();
        this.lineas = new ArrayList<>(lineas);
        this.pago = pago;
        pago.validar(getTotal());
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineaVenta linea : lineas) {
            total = total.add(linea.getSubtotal());
        }
        return total;
    }

    // lo que costaron los productos vendidos (gasto de compra)
    public BigDecimal getCosto() {
        BigDecimal costo = BigDecimal.ZERO;
        for (LineaVenta linea : lineas) {
            costo = costo.add(linea.getCostoTotal());
        }
        return costo;
    }

    public BigDecimal getOpGravada() {
        BigDecimal igv = ConfiguracionGlobal.getInstancia().getIgv();
        BigDecimal divisor = BigDecimal.ONE.add(igv.divide(new BigDecimal("100")));
        return getTotal().divide(divisor, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getIgv() {
        return getTotal().subtract(getOpGravada());
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Sede getSede() {
        return sede;
    }

    public String getCliente() {
        return cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public List<LineaVenta> getLineas() {
        return lineas;
    }

    public Pago getPago() {
        return pago;
    }
}
