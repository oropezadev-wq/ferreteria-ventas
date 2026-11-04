package pe.idat.ferreteria.modelo;

import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Producto {
    private final Long id;
    private final String codigo;
    private String nombre;
    private String descripcion;
    private final CategoriaProducto categoria;
    private BigDecimal costoDolares;
    private BigDecimal precio;
    private String proveedor;
    private final LocalDate fechaIngreso;
    private int stock;
    private int stockMinimo;

    public Producto(Long id, String codigo, String nombre, String descripcion, CategoriaProducto categoria,
                    BigDecimal costoDolares, BigDecimal precio, String proveedor, LocalDate fechaIngreso,
                    int stock, int stockMinimo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Elija una categoría");
        }
        if (costoDolares == null || costoDolares.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo de compra debe ser mayor a cero");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (proveedor == null || proveedor.isBlank()) {
            throw new IllegalArgumentException("Ingrese el proveedor");
        }
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("Ingrese la fecha de ingreso");
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
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.categoria = categoria;
        this.costoDolares = costoDolares;
        this.precio = precio;
        this.proveedor = proveedor.trim();
        this.fechaIngreso = fechaIngreso;
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

    public void actualizar(String nuevoNombre, String nuevaDescripcion, BigDecimal nuevoCosto, BigDecimal nuevoPrecio,
                           String nuevoProveedor, int nuevoStockMinimo) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (nuevoCosto == null || nuevoCosto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo de compra debe ser mayor a cero");
        }
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (nuevoProveedor == null || nuevoProveedor.isBlank()) {
            throw new IllegalArgumentException("Ingrese el proveedor");
        }
        if (nuevoStockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        this.nombre = nuevoNombre.trim();
        this.descripcion = nuevaDescripcion == null ? "" : nuevaDescripcion.trim();
        this.costoDolares = nuevoCosto;
        this.precio = nuevoPrecio;
        this.proveedor = nuevoProveedor.trim();
        this.stockMinimo = nuevoStockMinimo;
    }

    public boolean estaBajoMinimo() {
        return stock <= stockMinimo;
    }

    // el costo se compra en dólares; aquí se pasa a soles con el tipo de cambio del Singleton
    public BigDecimal getCostoSoles() {
        return ConfiguracionGlobal.getInstancia().convertirASoles(costoDolares);
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

    public String getDescripcion() {
        return descripcion;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public BigDecimal getCostoDolares() {
        return costoDolares;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }
}
