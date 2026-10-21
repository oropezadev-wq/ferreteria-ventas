package pe.idat.ferreteria.dominio.venta;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;

/**
 * Un producto dentro de una venta. Guarda una "foto" del nombre, la categoría y el precio al momento de
 * vender: si el producto cambia de precio después, la venta ya registrada no cambia.
 */
public record LineaVenta(Long productoId, String producto, CategoriaProducto categoria, Dinero precioUnitario,
                         int cantidad) {

    public LineaVenta {
        // TODO (issue Ventas): lanzar IllegalArgumentException si productoId, producto, categoria o
        // precioUnitario son null, o si cantidad <= 0.
    }

    public Dinero subtotal() {
        // TODO (issue Ventas): return precioUnitario.multiplicar(cantidad);
        throw new UnsupportedOperationException("Por implementar: LineaVenta.subtotal");
    }
}
