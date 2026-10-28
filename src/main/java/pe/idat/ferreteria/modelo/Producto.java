package pe.idat.ferreteria.modelo;

import java.math.BigDecimal;

public class Producto {
    private final Long id;
    private final String codigo;
    private String nombre;
    private final CategoriaProducto categoria;
    private BigDecimal precio;
    private int stock;
    private int stockMinimo;

    public Producto(Long id, String codigo, String nombre, CategoriaProducto categoria, BigDecimal precio, int stock,
                    int stockMinimo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Elija una categoría");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        this.id = id;
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente de " + nombre + ": hay " + stock
                    + " y se pidieron " + cantidad);
        }
        stock -= cantidad;
    }

    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor a cero");
        }
        stock += cantidad;
    }

    public void actualizar(String nuevoNombre, BigDecimal nuevoPrecio, int nuevoStockMinimo) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (nuevoStockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        this.nombre = nuevoNombre.trim();
        this.precio = nuevoPrecio;
        this.stockMinimo = nuevoStockMinimo;
    }

    public boolean estaBajoMinimo() {
        return stock <= stockMinimo;
    }

    public Long getId() {
        return id;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }
}
