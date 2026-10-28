package pe.idat.ferreteria.servicio;

public class ItemVenta {
    private final Long productoId;
    private final int cantidad;

    public ItemVenta(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }
}
