package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.util.Objects;

/** Un punto de la serie de ventas: etiqueta del tramo ("2026-10-20", "octubre"...), cantidad de ventas y total. */
public record TotalPorTramo(String etiqueta, int cantidadVentas, Dinero total) {

    public TotalPorTramo {
        Objects.requireNonNull(etiqueta, "La etiqueta es obligatoria");
        Objects.requireNonNull(total, "El total es obligatorio");
        if (cantidadVentas < 0) {
            throw new IllegalArgumentException("La cantidad de ventas no puede ser negativa");
        }
    }
}
