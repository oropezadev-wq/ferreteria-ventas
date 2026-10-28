package pe.idat.ferreteria.repositorio;

import org.springframework.stereotype.Repository;
import pe.idat.ferreteria.modelo.Venta;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VentaRepositorio {
    private final List<Venta> ventas = new ArrayList<>();
    private long ultimoId = 0;

    public synchronized Long siguienteId() {
        ultimoId++;
        return ultimoId;
    }

    public synchronized void guardar(Venta venta) {
        ventas.add(venta);
    }

    public synchronized Venta buscarPorId(Long id) {
        for (Venta venta : ventas) {
            if (venta.getId().equals(id)) {
                return venta;
            }
        }
        return null;
    }

    public synchronized List<Venta> listar() {
        return new ArrayList<>(ventas);
    }
}
