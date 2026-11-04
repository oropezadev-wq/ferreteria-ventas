package pe.idat.ferreteria.modelo;

import java.math.BigDecimal;

public class LineaVenta {
    private final Long productoId;
    private final String producto;
    private final CategoriaProducto categoria;
    private final BigDecimal precioUnitario;
    private final BigDecimal costoUnitario;
    private final int cantidad;

    public LineaVenta(Long productoId, String producto, CategoriaProducto categoria, BigDecimal precioUnitario,
                      BigDecimal costoUnitario, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.productoId = productoId;
        this.producto = producto;
        this.categoria = categoria;
        this.precioUnitario = precioUnitario;
        this.costoUnitario = costoUnitario;
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // lo que le costó a la ferretería lo vendido en esta línea (en soles)
    public BigDecimal getCostoTotal() {
        return costoUnitario.multiply(BigDecimal.valueOf(cantidad));
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

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }
}
