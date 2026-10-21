package pe.idat.ferreteria.dominio.producto;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.EntidadBase;

/**
 * Un producto de la ferretería. Encapsulamiento: los campos son privados y no hay setters; el estado solo
 * cambia con métodos que validan (descontarStock, reponerStock, actualizar).
 */
public class Producto extends EntidadBase {

    private final String codigo;
    private String nombre;
    private final CategoriaProducto categoria;
    /** Precio de venta al público, con IGV incluido. */
    private Dinero precio;
    private int stock;
    private int stockMinimo;

    public Producto(Long id, String codigo, String nombre, CategoriaProducto categoria, Dinero precio, int stock,
                    int stockMinimo) {
        super(id);
        // TODO (issue Productos): validar y asignar. Lanzar IllegalArgumentException con un mensaje claro si:
        //  - codigo o nombre son null o vacíos (isBlank)
        //  - categoria o precio son null
        //  - stock o stockMinimo son negativos
        // Luego asignar: this.codigo = codigo; this.nombre = nombre; ... (todos los campos)
        throw new UnsupportedOperationException("Por implementar: constructor de Producto");
    }

    /** Resta unidades del stock al vender. */
    public void descontarStock(int cantidad) {
        // TODO (issue Productos):
        //  - si cantidad <= 0: IllegalArgumentException
        //  - si cantidad > stock: new StockInsuficienteException("Stock insuficiente de " + nombre
        //        + ": hay " + stock + " y se pidieron " + cantidad)
        //  - si no: stock -= cantidad;
        throw new UnsupportedOperationException("Por implementar: Producto.descontarStock");
    }

    /** Suma unidades al stock cuando llega mercadería. */
    public void reponerStock(int cantidad) {
        // TODO (issue Productos): si cantidad <= 0, IllegalArgumentException; si no, stock += cantidad;
        throw new UnsupportedOperationException("Por implementar: Producto.reponerStock");
    }

    /** Cambia los datos editables desde la pantalla "Editar producto" (el código y la categoría no cambian). */
    public void actualizar(String nuevoNombre, Dinero nuevoPrecio, int nuevoStockMinimo) {
        // TODO (issue Productos): mismas validaciones del constructor para estos tres datos, y asignarlos.
        throw new UnsupportedOperationException("Por implementar: Producto.actualizar");
    }

    public boolean estaBajoMinimo() {
        // TODO (issue Productos): true si el stock es menor o igual al stock mínimo.
        throw new UnsupportedOperationException("Por implementar: Producto.estaBajoMinimo");
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public Dinero getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }
}
