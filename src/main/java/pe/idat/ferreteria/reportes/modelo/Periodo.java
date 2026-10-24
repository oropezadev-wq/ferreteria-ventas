package pe.idat.ferreteria.reportes.modelo;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * Rango de fechas cerrado en ambos extremos: {@code desde} y {@code hasta} se incluyen.
 */
public record Periodo(LocalDate desde, LocalDate hasta) {

    public Periodo {
        Objects.requireNonNull(desde, "La fecha inicial es obligatoria");
        Objects.requireNonNull(hasta, "La fecha final es obligatoria");
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial");
        }
    }

    /**
     * Período que contiene a la fecha de referencia según la granularidad:
     * el día, su mes o su año. Con NINGUNA devuelve solo ese día.
     */
    public static Periodo de(Granularidad granularidad, LocalDate referencia) {
        Objects.requireNonNull(granularidad, "La granularidad es obligatoria");
        Objects.requireNonNull(referencia, "La fecha de referencia es obligatoria");
        return switch (granularidad) {
            case DIA, NINGUNA -> new Periodo(referencia, referencia);
            case MES -> new Periodo(referencia.withDayOfMonth(1), referencia.with(TemporalAdjusters.lastDayOfMonth()));
            case ANIO -> new Periodo(referencia.withDayOfYear(1), referencia.with(TemporalAdjusters.lastDayOfYear()));
        };
    }

    public boolean contiene(LocalDate fecha) {
        return fecha != null && !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }
}
