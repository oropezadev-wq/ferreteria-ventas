package pe.idat.ferreteria.reportes.datos;

import pe.idat.ferreteria.dominio.comun.Dinero;
import pe.idat.ferreteria.reportes.modelo.Periodo;

import java.util.List;
import java.util.Objects;

/** Totales de ventas de un período; {@code serie} viene ordenada cronológicamente. */
public record ResumenVentas(Periodo periodo, int cantidadVentas, Dinero total, Dinero ticketPromedio,
                            List<TotalPorTramo> serie) {

    public ResumenVentas {
        Objects.requireNonNull(periodo, "El período es obligatorio");
        Objects.requireNonNull(total, "El total es obligatorio");
        Objects.requireNonNull(ticketPromedio, "El ticket promedio es obligatorio");
        if (cantidadVentas < 0) {
            throw new IllegalArgumentException("La cantidad de ventas no puede ser negativa");
        }
        serie = List.copyOf(serie);
    }
}
