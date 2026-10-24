package pe.idat.ferreteria.web.api;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.idat.ferreteria.dominio.comun.RecursoNoEncontradoException;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones de los controladores REST ({@code /api/...}) a respuestas {@link ErrorResponse}.
 * Solo aplica a clases con {@link RestController}; las pantallas usan {@code web.ManejadorErroresWeb}.
 * Tiene la mayor precedencia para que un {@code @RestController} nunca reciba una vista HTML de error.
 */
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Clock reloj;

    public GlobalExceptionHandler(Clock reloj) {
        this.reloj = reloj;
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), req, Map.of());
    }

    /** Invariantes de records y objetos de valor (SolicitudReporte, Dinero...) con datos de entrada inválidos. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> argumentoInvalido(IllegalArgumentException ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), req, Map.of());
    }

    /** Parámetros que no se pudieron decodificar (no UTF-8 o un escape % inválido). */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> parametroMalCodificado(InvalidParameterException ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST, "Los datos enviados no tienen una codificación válida (UTF-8)",
                req, Map.of());
    }

    /** Errores de @Valid: devuelve el mensaje de cada campo inválido. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return responder(HttpStatus.BAD_REQUEST, "Hay datos inválidos en la solicitud", req, errores);
    }

    /** JSON mal formado o un valor que no existe en un enum. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> cuerpoIlegible(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o tiene valores no permitidos", req, Map.of());
    }

    /** Un id que no es número o un filtro con un valor desconocido. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST,
                "El valor '" + ex.getValue() + "' no es válido para el parámetro " + ex.getName(), req, Map.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> metodoNoPermitido(HttpRequestMethodNotSupportedException ex,
                                                           HttpServletRequest req) {
        return responder(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no está permitido en esta ruta", req, Map.of());
    }

    /** El cuerpo no viene como application/json. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> formatoNoSoportado(HttpMediaTypeNotSupportedException ex,
                                                            HttpServletRequest req) {
        return responder(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "El cuerpo de la petición debe enviarse como application/json", req, Map.of());
    }

    /** El cliente solo acepta un formato que la API no produce; no se puede enviar cuerpo. */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Void> formatoNoAceptable() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /** Cualquier error no previsto: se registra completo en el log y al cliente no se le exponen detalles internos. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Error no controlado en {}", req.getRequestURI(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno", req, Map.of());
    }

    private ResponseEntity<ErrorResponse> responder(HttpStatus estado, String mensaje, HttpServletRequest req,
                                                    Map<String, String> errores) {
        ErrorResponse cuerpo = new ErrorResponse(LocalDateTime.now(reloj), estado.value(),
                estado.getReasonPhrase(), mensaje, req.getRequestURI(), errores);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
