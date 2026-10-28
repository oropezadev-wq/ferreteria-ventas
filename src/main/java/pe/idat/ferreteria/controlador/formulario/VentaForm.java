package pe.idat.ferreteria.controlador.formulario;

import pe.idat.ferreteria.modelo.pago.Pago;
import pe.idat.ferreteria.modelo.pago.PagoEfectivo;
import pe.idat.ferreteria.modelo.pago.PagoTarjeta;
import pe.idat.ferreteria.modelo.pago.PagoYape;
import pe.idat.ferreteria.servicio.ItemVenta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaForm {
    private List<Fila> filas = new ArrayList<>();
    private String metodoPago = "EFECTIVO";
    private BigDecimal montoRecibido;
    private String ultimosCuatro;
    private String celular;

    public VentaForm() {
        for (int i = 0; i < 5; i++) {
            filas.add(new Fila());
        }
    }

    public List<ItemVenta> items() {
        List<ItemVenta> items = new ArrayList<>();
        for (Fila fila : filas) {
            if (fila.getProductoId() != null && fila.getCantidad() != null && fila.getCantidad() > 0) {
                items.add(new ItemVenta(fila.getProductoId(), fila.getCantidad()));
            }
        }
        return items;
    }

    public Pago pago() {
        if ("TARJETA".equals(metodoPago)) {
            return new PagoTarjeta(ultimosCuatro);
        }
        if ("YAPE".equals(metodoPago)) {
            return new PagoYape(celular);
        }
        return new PagoEfectivo(montoRecibido);
    }

    public List<Fila> getFilas() {
        return filas;
    }

    public void setFilas(List<Fila> filas) {
        this.filas = filas;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMontoRecibido() {
        return montoRecibido;
    }

    public void setMontoRecibido(BigDecimal montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public String getUltimosCuatro() {
        return ultimosCuatro;
    }

    public void setUltimosCuatro(String ultimosCuatro) {
        this.ultimosCuatro = ultimosCuatro;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public static class Fila {
        private Long productoId;
        private Integer cantidad;

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
