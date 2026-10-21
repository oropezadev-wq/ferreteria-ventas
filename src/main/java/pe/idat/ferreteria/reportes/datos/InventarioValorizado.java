package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.util.List;
import java.util.Objects;

/** Productos con su valorización y el valor total del inventario. */
public record InventarioValorizado(List<LineaInventario> lineas, Dinero total) {

    public InventarioValorizado {
        lineas = List.copyOf(lineas);
        Objects.requireNonNull(total, "El total es obligatorio");
    }

    /** Un producto del inventario; {@code valorizado} = stock x costo unitario. */
    public record LineaInventario(String codigo, String descripcion, String categoria, int stock, int stockMinimo,
                                  Dinero costoUnitario, Dinero valorizado, boolean bajoMinimo) {

        public LineaInventario {
            Objects.requireNonNull(codigo, "El código es obligatorio");
            Objects.requireNonNull(descripcion, "La descripción es obligatoria");
            Objects.requireNonNull(categoria, "La categoría es obligatoria");
            Objects.requireNonNull(costoUnitario, "El costo unitario es obligatorio");
            Objects.requireNonNull(valorizado, "El valorizado es obligatorio");
        }
    }
}
