package pe.idat.ferreteria.web.formulario;

import pe.idat.ferreteria.aplicacion.ItemVenta;
import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.PagoInvalidoException;
import pe.idat.ferreteria.dominio.venta.pago.Pago;
import pe.idat.ferreteria.dominio.venta.pago.PagoEfectivo;
import pe.idat.ferreteria.dominio.venta.pago.PagoTarjeta;
import pe.idat.ferreteria.dominio.venta.pago.PagoYape;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Datos de la pantalla "Nueva venta": hasta {@value #FILAS} productos (las filas vacías se ignoran)
 * y el método de pago con sus datos.
 */
public class VentaForm {

    public static final int FILAS = 5;

    public enum MetodoPago { EFECTIVO, TARJETA, YAPE }

    private List<Fila> filas = new ArrayList<>();
    private MetodoPago metodoPago = MetodoPago.EFECTIVO;
    private BigDecimal montoRecibido;
    private String ultimosCuatro;
    private String celular;

    public VentaForm() {
        for (int i = 0; i < FILAS; i++) {
            filas.add(new Fila());
        }
    }

    /** Solo las filas con producto y cantidad mayor que cero. */
    public List<ItemVenta> items() {
        return filas.stream()
                .filter(f -> f.getProductoId() != null && f.getCantidad() != null && f.getCantidad() > 0)
                .map(f -> new ItemVenta(f.getProductoId(), f.getCantidad()))
                .toList();
    }

    /** Crea el pago según el método elegido; cada subclase valida sus propios datos. */
    public Pago pago() {
        return switch (metodoPago) {
            case EFECTIVO -> {
                if (montoRecibido == null) {
                    throw new PagoInvalidoException("Indique el monto recibido en efectivo");
                }
                yield new PagoEfectivo(Dinero.soles(montoRecibido));
            }
            case TARJETA -> new PagoTarjeta(ultimosCuatro);
            case YAPE -> new PagoYape(celular);
        };
    }

    public List<Fila> getFilas() {
        return filas;
    }

    public void setFilas(List<Fila> filas) {
        this.filas = filas;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
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

    /** Una fila de la venta: producto y cantidad. */
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
