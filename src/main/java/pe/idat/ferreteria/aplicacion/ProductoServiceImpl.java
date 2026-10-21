package pe.idat.ferreteria.aplicacion;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;

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
        // TODO (issue Productos): return repositorio.listar();
        throw new UnsupportedOperationException("Por implementar: ProductoServiceImpl.listar");
    }

    @Override
    public Producto buscar(Long id) {
        // TODO (issue Productos):
        //   return repositorio.buscarPorId(id)
        //           .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
        throw new UnsupportedOperationException("Por implementar: ProductoServiceImpl.buscar");
    }

    @Override
    public Producto crear(String codigo, String nombre, CategoriaProducto categoria, BigDecimal precio, int stock,
                          int stockMinimo) {
        // TODO (issue Productos):
        //   Producto producto = new Producto(repositorio.siguienteId(), codigo, nombre, categoria,
        //           Dinero.soles(precio), stock, stockMinimo);
        //   return repositorio.guardar(producto);
        throw new UnsupportedOperationException("Por implementar: ProductoServiceImpl.crear");
    }

    @Override
    public Producto actualizar(Long id, String nombre, BigDecimal precio, int stockMinimo) {
        // TODO (issue Productos): buscar(id), llamar producto.actualizar(...) y guardar.
        throw new UnsupportedOperationException("Por implementar: ProductoServiceImpl.actualizar");
    }

    @Override
    public Producto reponerStock(Long id, int cantidad) {
        // TODO (issue Productos): buscar(id), producto.reponerStock(cantidad) y guardar.
        throw new UnsupportedOperationException("Por implementar: ProductoServiceImpl.reponerStock");
    }
}
