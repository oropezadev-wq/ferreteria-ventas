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
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría del producto no puede ser nula");
        }
        if (precio == null) {
            throw new IllegalArgumentException("El precio del producto no puede ser nulo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock inicial no puede ser negativo");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
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
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException("Stock insuficiente de " + nombre + ": hay " + stock + " y se pidieron " + cantidad);
        }
        this.stock -= cantidad;
    }

    /** Suma unidades al stock cuando llega mercadería. */
    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor a cero");
        }
        this.stock += cantidad;
    }

    /** Cambia los datos editables desde la pantalla "Editar producto" (el código y la categoría no cambian). */
    public void actualizar(String nuevoNombre, Dinero nuevoPrecio, int nuevoStockMinimo) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre no puede ser nulo o vacío");
        }
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        if (nuevoStockMinimo < 0) {
            throw new IllegalArgumentException("El nuevo stock mínimo no puede ser negativo");
        }

        this.nombre = nuevoNombre;
        this.precio = nuevoPrecio;
        this.stockMinimo = nuevoStockMinimo;
    }

    public boolean estaBajoMinimo() {
        return this.stock <= this.stockMinimo;
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

    /** Excepción interna para manejar la falta de stock sin crear archivos externos. */
    public static class StockInsuficienteException extends RuntimeException {
        public StockInsuficienteException(String mensaje) {
            super(mensaje);
        }
    }
}