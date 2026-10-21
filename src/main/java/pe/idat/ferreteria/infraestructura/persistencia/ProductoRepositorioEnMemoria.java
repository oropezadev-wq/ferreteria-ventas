package pe.idat.ferreteria.infraestructura.persistencia;

import org.springframework.stereotype.Repository;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;

/** Productos guardados en memoria (toda la lógica está en {@link RepositorioEnMemoria}). */
@Repository
public class ProductoRepositorioEnMemoria extends RepositorioEnMemoria<Producto> implements ProductoRepositorio {
}
