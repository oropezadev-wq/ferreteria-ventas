package pe.idat.ferreteria.controlador.formulario;

import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.Producto;

import java.math.BigDecimal;

// Datos que llegan desde el formulario de productos.
public class ProductoForm {

    private String codigo;
    private String nombre;
    private CategoriaProducto categoria;
    private BigDecimal precio;
    private int stock;
    private int stockMinimo;

    public static ProductoForm desde(Producto producto) {
        ProductoForm form = new ProductoForm();
        form.codigo = producto.getCodigo();
        form.nombre = producto.getNombre();
        form.categoria = producto.getCategoria();
        form.precio = producto.getPrecio();
        form.stock = producto.getStock();
        form.stockMinimo = producto.getStockMinimo();
        return form;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
