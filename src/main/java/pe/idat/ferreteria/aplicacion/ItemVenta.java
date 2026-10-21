package pe.idat.ferreteria.aplicacion;

/** Lo que se pide al registrar una venta: qué producto y cuántas unidades. */
public record ItemVenta(Long productoId, int cantidad) {
}
