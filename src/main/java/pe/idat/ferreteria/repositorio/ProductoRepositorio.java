package pe.idat.ferreteria.repositorio;

import org.springframework.stereotype.Repository;
import pe.idat.ferreteria.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepositorio {
    private final List<Producto> productos = new ArrayList<>();
    private long ultimoId = 0;

    public synchronized Long siguienteId() {
        ultimoId++;
        return ultimoId;
    }

    public synchronized void guardar(Producto producto) {
        if (!productos.contains(producto)) {
            productos.add(producto);
        }
    }

    public synchronized Producto buscarPorId(Long id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }

    public synchronized List<Producto> listar() {
        return new ArrayList<>(productos);
    }
}
