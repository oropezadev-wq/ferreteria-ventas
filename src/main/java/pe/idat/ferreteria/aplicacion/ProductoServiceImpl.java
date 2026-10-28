package pe.idat.ferreteria.aplicacion;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;
import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.comun.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepositorio repositorio;

    public ProductoServiceImpl(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Producto> listar() {
        return repositorio.listar();
    }

    @Override
    public Producto buscar(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }

    @Override
    public Producto crear(String codigo, String nombre, CategoriaProducto categoria, BigDecimal precio, int stock,
                          int stockMinimo) {
        Producto producto = new Producto(repositorio.siguienteId(), codigo, nombre, categoria,
                Dinero.soles(precio), stock, stockMinimo);
        return repositorio.guardar(producto);
    }

    @Override
    public Producto actualizar(Long id, String nombre, BigDecimal precio, int stockMinimo) {
        Producto producto = buscar(id);
        producto.actualizar(nombre, Dinero.soles(precio), stockMinimo);
        return repositorio.guardar(producto);
    }

    @Override
    public Producto reponerStock(Long id, int cantidad) {
        Producto producto = buscar(id);
        producto.reponerStock(cantidad);
        return repositorio.guardar(producto);
    }
}
