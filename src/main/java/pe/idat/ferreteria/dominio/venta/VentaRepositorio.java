package pe.idat.ferreteria.dominio.venta;

import pe.idat.ferreteria.infraestructura.persistencia.Repositorio;

/** Dónde se guardan las ventas. Los servicios dependen de esta interfaz, no de la implementación. */
public interface VentaRepositorio extends Repositorio<Venta> {
}
