package pe.idat.ferreteria.infraestructura.persistencia;

import org.springframework.stereotype.Repository;
import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.dominio.venta.VentaRepositorio;

/** Ventas guardadas en memoria (toda la lógica está en {@link RepositorioEnMemoria}). */
@Repository
public class VentaRepositorioEnMemoria extends RepositorioEnMemoria<Venta> implements VentaRepositorio {
}
