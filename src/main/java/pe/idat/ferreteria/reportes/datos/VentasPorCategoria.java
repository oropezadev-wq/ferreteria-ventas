package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.util.Objects;

/** Unidades vendidas y total de una categoría de producto (nombre legible de la categoría). */
public record VentasPorCategoria(String categoria, int unidadesVendidas, Dinero total) {

    public VentasPorCategoria {
        Objects.requireNonNull(categoria, "La categoría es obligatoria");
        Objects.requireNonNull(total, "El total es obligatorio");
        if (unidadesVendidas < 0) {
            throw new IllegalArgumentException("Las unidades vendidas no pueden ser negativas");
        }
    }
}
