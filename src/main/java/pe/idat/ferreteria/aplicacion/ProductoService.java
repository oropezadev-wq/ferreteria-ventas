package pe.idat.ferreteria.aplicacion;

import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.dominio.producto.Producto;

import java.math.BigDecimal;
import java.util.List;

/** Casos de uso de productos. Los controladores dependen de esta interfaz, no de la implementación. */
public interface ProductoService {

    List<Producto> listar();

    /** Lanza RecursoNoEncontradoException si no existe. */
    Producto buscar(Long id);

    Producto crear(String codigo, String nombre, CategoriaProducto categoria, BigDecimal precio, int stock,
                   int stockMinimo);

    Producto actualizar(Long id, String nombre, BigDecimal precio, int stockMinimo);

    Producto reponerStock(Long id, int cantidad);
}
