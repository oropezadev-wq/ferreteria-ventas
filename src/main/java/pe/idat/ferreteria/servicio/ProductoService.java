package pe.idat.ferreteria.servicio;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.Producto;
import pe.idat.ferreteria.repositorio.ProductoRepositorio;

import java.math.BigDecimal;
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

    public Producto crear(String codigo, String nombre, CategoriaProducto categoria, BigDecimal precio, int stock,
                          int stockMinimo) {
        for (Producto otro : repositorio.listar()) {
            if (otro.getCodigo().equalsIgnoreCase(codigo == null ? "" : codigo.trim())) {
                throw new IllegalArgumentException("Ya existe un producto con el código " + codigo);
            }
        }
        Producto producto = new Producto(repositorio.siguienteId(), codigo, nombre, categoria, precio, stock,
                stockMinimo);
        repositorio.guardar(producto);
        return producto;
    }

    public Producto actualizar(Long id, String nombre, BigDecimal precio, int stockMinimo) {
        Producto producto = buscar(id);
        producto.actualizar(nombre, precio, stockMinimo);
        return producto;
    }

    public Producto reponerStock(Long id, int cantidad) {
        Producto producto = buscar(id);
        producto.reponerStock(cantidad);
        return producto;
    }
}
