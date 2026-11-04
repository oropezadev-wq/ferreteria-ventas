package pe.idat.ferreteria.controlador.formulario;

import org.springframework.format.annotation.DateTimeFormat;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.Producto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductoForm {
    private String codigo;
    private String nombre;
    private String descripcion;
    private CategoriaProducto categoria;
    private BigDecimal costoDolares;
    private BigDecimal precio;
    private String proveedor;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaIngreso = LocalDate.now();
    private int stock;
    private int stockMinimo;

    public static ProductoForm desde(Producto producto) {
        ProductoForm form = new ProductoForm();
        form.codigo = producto.getCodigo();
        form.nombre = producto.getNombre();
        form.descripcion = producto.getDescripcion();
        form.categoria = producto.getCategoria();
        form.costoDolares = producto.getCostoDolares();
        form.precio = producto.getPrecio();
        form.proveedor = producto.getProveedor();
        form.fechaIngreso = producto.getFechaIngreso();
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getCostoDolares() {
        return costoDolares;
    }

    public void setCostoDolares(BigDecimal costoDolares) {
        this.costoDolares = costoDolares;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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
