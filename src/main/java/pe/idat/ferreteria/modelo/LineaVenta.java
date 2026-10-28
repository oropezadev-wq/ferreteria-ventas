package pe.idat.ferreteria.modelo;

import java.math.BigDecimal;

// Un producto dentro de una venta. Guarda nombre y precio del momento de la venta.
public class LineaVenta {

    private final Long productoId;
    private final String producto;
    private final CategoriaProducto categoria;
    private final BigDecimal precioUnitario;
    private final int cantidad;

    public LineaVenta(Long productoId, String producto, CategoriaProducto categoria, BigDecimal precioUnitario,
                      int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.productoId = productoId;
        this.producto = producto;
        this.categoria = categoria;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getProducto() {
        return producto;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }
}
