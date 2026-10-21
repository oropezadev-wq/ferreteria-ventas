package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;

/** Un producto del inventario: {@code valorizado} = stock x precio. */
public record FilaInventario(String codigo, String producto, int stock, Dinero precio, Dinero valorizado,
                             boolean bajoMinimo) {
}
