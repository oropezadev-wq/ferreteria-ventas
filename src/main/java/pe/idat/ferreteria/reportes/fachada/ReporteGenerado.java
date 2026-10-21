package pe.idat.ferreteria.reportes.fachada;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Salida de ReporteFacade (Integrante 1, etapa 2): documento HTML autocontenido listo para mostrar o descargar.
 * {@code huellaSha256} está presente solo si se pidió el extra FIRMA.
 */
public record ReporteGenerado(String titulo, String html, String nombreArchivo, Optional<String> huellaSha256,
                              List<String> caracteristicas, LocalDateTime generadoEl) {

    public ReporteGenerado {
        Objects.requireNonNull(titulo, "El título es obligatorio");
        Objects.requireNonNull(html, "El HTML es obligatorio");
        Objects.requireNonNull(nombreArchivo, "El nombre de archivo es obligatorio");
        Objects.requireNonNull(huellaSha256, "Usar Optional.empty() cuando no hay huella");
        caracteristicas = List.copyOf(caracteristicas);
        Objects.requireNonNull(generadoEl, "La fecha de generación es obligatoria");
    }
}
