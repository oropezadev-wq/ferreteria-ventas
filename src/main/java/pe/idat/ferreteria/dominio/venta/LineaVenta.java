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
        if (productoId == null || producto == null || producto.isBlank() || categoria == null
                || precioUnitario == null || cantidad <= 0) {
            throw new IllegalArgumentException("La línea de venta necesita producto, categoría, precio y cantidad válida");
        }
    }

    public Dinero subtotal() {
        return precioUnitario.multiplicar(cantidad);
    }
}
