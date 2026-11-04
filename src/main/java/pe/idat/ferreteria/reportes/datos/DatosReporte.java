package pe.idat.ferreteria.reportes.datos;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.LineaVenta;
import pe.idat.ferreteria.modelo.Producto;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.repositorio.ProductoRepositorio;
import pe.idat.ferreteria.repositorio.VentaRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatosReporte {
    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;

    public DatosReporte(VentaRepositorio ventas, ProductoRepositorio productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    public List<Venta> ventasEntre(LocalDate desde, LocalDate hasta) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta venta : ventas.listar()) {
            LocalDate fecha = venta.getFechaHora().toLocalDate();
            if (!fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                resultado.add(venta);
            }
        }
        resultado.sort(Comparator.comparing(Venta::getFechaHora));
        return resultado;
    }

    public Map<String, BigDecimal> totalPorCategoria(List<Venta> lista) {
        Map<String, BigDecimal> totales = new LinkedHashMap<>();
        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            totales.put(categoria.getNombre(), new BigDecimal("0.00"));
        }
        for (Venta venta : lista) {
            for (LineaVenta linea : venta.getLineas()) {
                String nombre = linea.getCategoria().getNombre();
                totales.put(nombre, totales.get(nombre).add(linea.getSubtotal()));
            }
        }
        return totales;
    }

    public List<Producto> productos() {
        return productos.listar();
    }
}
