package pe.idat.ferreteria.dominio.producto;

import pe.idat.ferreteria.infraestructura.persistencia.Repositorio;

/** Dónde se guardan los productos. Los servicios dependen de esta interfaz, no de la implementación. */
public interface ProductoRepositorio extends Repositorio<Producto> {
}
