package pe.idat.ferreteria.dominio.producto;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.EntidadBase;
import pe.idat.ferreteria.dominio.comun.StockInsuficienteException;

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
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El código y el nombre son obligatorios");
        }
        if (categoria == null || precio == null) {
            throw new IllegalArgumentException("La categoría y el precio son obligatorios");
        }
        if (stock < 0 || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock y el stock mínimo no pueden ser negativos");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    /** Resta unidades del stock al vender. */
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException("Stock insuficiente de " + nombre
                    + ": hay " + stock + " y se pidieron " + cantidad);
        }
        stock -= cantidad;
    }

    /** Suma unidades al stock cuando llega mercadería. */
    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        stock += cantidad;
    }

    /** Cambia los datos editables desde la pantalla "Editar producto" (el código y la categoría no cambian). */
    public void actualizar(String nuevoNombre, Dinero nuevoPrecio, int nuevoStockMinimo) {
        if (nuevoNombre == null || nuevoNombre.isBlank() || nuevoPrecio == null || nuevoStockMinimo < 0) {
            throw new IllegalArgumentException("Nombre, precio y stock mínimo deben ser válidos");
        }
        nombre = nuevoNombre;
        precio = nuevoPrecio;
        stockMinimo = nuevoStockMinimo;
    }

    public boolean estaBajoMinimo() {
        return stock <= stockMinimo;
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
