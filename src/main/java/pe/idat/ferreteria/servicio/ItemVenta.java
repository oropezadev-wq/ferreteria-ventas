package pe.idat.ferreteria.servicio;

// Lo que el vendedor pide en la pantalla: qué producto y cuántos.
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
