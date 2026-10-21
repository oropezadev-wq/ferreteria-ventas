package pe.idat.ferreteria.web.formulario;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.dominio.producto.Producto;

import java.math.BigDecimal;

/** Datos de la pantalla "Nuevo / Editar producto". Separado de {@link Producto}, que no tiene setters. */
public class ProductoForm {

    @NotBlank(message = "Ingrese el código")
    @Size(max = 20, message = "Máximo {max} caracteres")
    private String codigo;

    @NotBlank(message = "Ingrese el nombre")
    @Size(max = 100, message = "Máximo {max} caracteres")
    private String nombre;

    @NotNull(message = "Elija una categoría")
    private CategoriaProducto categoria;

    @NotNull(message = "Ingrese el precio")
    @DecimalMin(value = "0.10", message = "El precio mínimo es 0.10")
    private BigDecimal precio;

    @NotNull(message = "Ingrese el stock")
    @Min(value = 0, message = "No puede ser negativo")
    private Integer stock;

    @NotNull(message = "Ingrese el stock mínimo")
    @Min(value = 0, message = "No puede ser negativo")
    private Integer stockMinimo;

    public static ProductoForm desde(Producto producto) {
        ProductoForm form = new ProductoForm();
        form.codigo = producto.getCodigo();
        form.nombre = producto.getNombre();
        form.categoria = producto.getCategoria();
        form.precio = producto.getPrecio().monto();
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
