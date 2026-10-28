package pe.idat.ferreteria.reportes.datos;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.dominio.producto.ProductoRepositorio;
import pe.idat.ferreteria.dominio.venta.VentaRepositorio;
import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.dominio.venta.LineaVenta;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.reportes.modelo.Periodo;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Objects;

/** Lee las ventas y los productos guardados en memoria y los convierte en filas para los reportes. */
@Component
public class ConsultaDatosReporteEnMemoria implements ConsultaDatosReporte {

    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;

    public ConsultaDatosReporteEnMemoria(VentaRepositorio ventas, ProductoRepositorio productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @Override
    public List<FilaVenta> ventasDelPeriodo(Periodo periodo) {
        Objects.requireNonNull(periodo, "El período es obligatorio");
        List<FilaVenta> filas = new ArrayList<>();
        for (Venta venta : ventas.listar()) {
            if (periodo.contiene(venta.getFechaHora().toLocalDate())) {
                for (LineaVenta linea : venta.getLineas()) {
                    filas.add(new FilaVenta(venta.getFechaHora().toLocalDate(), venta.getNumero(),
                            linea.producto(), linea.categoria().getNombre(), linea.cantidad(), linea.subtotal()));
                }
            }
        }
        return filas.stream().sorted(Comparator.comparing(FilaVenta::fecha)
                .thenComparing(FilaVenta::numeroVenta)).toList();
    }

    @Override
    public Map<String, Dinero> totalPorCategoria(Periodo periodo) {
        Map<String, Dinero> totales = new LinkedHashMap<>();
        for (FilaVenta fila : ventasDelPeriodo(periodo)) {
            totales.merge(fila.categoria(), fila.total(), Dinero::sumar);
        }
        Map<String, Dinero> ordenados = new LinkedHashMap<>();
        totales.entrySet().stream()
                .sorted(Map.Entry.<String, Dinero>comparingByValue().reversed())
                .forEach(entrada -> ordenados.put(entrada.getKey(), entrada.getValue()));
        return ordenados;
    }

    @Override
    public List<FilaInventario> inventario() {
        List<FilaInventario> filas = new ArrayList<>();
        for (Producto producto : productos.listar()) {
            filas.add(new FilaInventario(producto.getCodigo(), producto.getNombre(), producto.getStock(),
                    producto.getPrecio(), producto.getPrecio().multiplicar(producto.getStock()),
                    producto.estaBajoMinimo()));
        }
        return filas;
    }
}
