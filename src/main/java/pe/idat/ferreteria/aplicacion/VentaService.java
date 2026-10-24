package pe.idat.ferreteria.aplicacion;

import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.dominio.venta.pago.Pago;

import java.util.List;

/** Casos de uso de ventas. Los controladores dependen de esta interfaz, no de la implementación. */
public interface VentaService {

    /** Registra la venta y descuenta el stock. Si un producto no alcanza, no se cambia ningún stock. */
    Venta registrar(List<ItemVenta> items, Pago pago);

    /** Las más recientes primero. */
    List<Venta> listar();

    /** Lanza RecursoNoEncontradoException si no existe. */
    Venta buscar(Long id);
}
