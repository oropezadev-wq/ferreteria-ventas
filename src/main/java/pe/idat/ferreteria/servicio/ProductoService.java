package pe.idat.ferreteria.servicio;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.Producto;
import pe.idat.ferreteria.repositorio.ProductoRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepositorio repositorio;

    public ProductoService(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.listar();
    }

    public Producto buscar(Long id) {
        Producto producto = repositorio.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("No existe el producto con id " + id);
        }
        return producto;
    }

    public Producto crear(String codigo, String nombre, String descripcion, CategoriaProducto categoria,
                          BigDecimal costoDolares, BigDecimal precio, String proveedor, LocalDate fechaIngreso,
                          int stock, int stockMinimo) {
        for (Producto otro : repositorio.listar()) {
            if (otro.getCodigo().equalsIgnoreCase(codigo == null ? "" : codigo.trim())) {
                throw new IllegalArgumentException("Ya existe un producto con el código " + codigo);
            }
        }
        Producto producto = new Producto(repositorio.siguienteId(), codigo, nombre, descripcion, categoria,
                costoDolares, precio, proveedor, fechaIngreso, stock, stockMinimo);
        repositorio.guardar(producto);
        return producto;
    }

    public Producto actualizar(Long id, String nombre, String descripcion, BigDecimal costoDolares, BigDecimal precio,
                               String proveedor, int stockMinimo) {
        Producto producto = buscar(id);
        producto.actualizar(nombre, descripcion, costoDolares, precio, proveedor, stockMinimo);
        return producto;
    }

    public Producto reponerStock(Long id, int cantidad) {
        Producto producto = buscar(id);
        producto.reponerStock(cantidad);
        return producto;
    }
}
