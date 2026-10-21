package pe.idat.ferreteria.web.api;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato único de error de la API REST.
 * {@code errores} trae el detalle por campo cuando falla una validación; en los demás casos va vacío.
 */
public record ErrorResponse(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> errores
) {
}
