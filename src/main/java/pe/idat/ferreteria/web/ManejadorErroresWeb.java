package pe.idat.ferreteria.web;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import pe.idat.ferreteria.dominio.comun.RecursoNoEncontradoException;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;

/**
 * Errores de las pantallas Thymeleaf: muestra la vista {@code error} con un mensaje entendible.
 * <p>
 * Los controladores REST tienen su propio manejador ({@code web.api.GlobalExceptionHandler}), que se evalúa
 * antes por su {@link Order}; este solo termina atendiendo a los {@code @Controller} de pantallas.
 * Las reglas de negocio que se pueden corregir en un formulario (stock, pago) conviene capturarlas en
 * el controlador y mostrarlas en el mismo formulario; este manejador es la red de seguridad.
 */
@ControllerAdvice(annotations = Controller.class)
@Order(Ordered.LOWEST_PRECEDENCE)
public class ManejadorErroresWeb {

    private static final Logger log = LoggerFactory.getLogger(ManejadorErroresWeb.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ModelAndView noEncontrado(RecursoNoEncontradoException ex) {
        return vista(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ModelAndView reglaNegocio(ReglaNegocioException ex) {
        return vista(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Un id de la URL que no es número, por ejemplo /ventas/abc. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return vista(HttpStatus.BAD_REQUEST, "El valor '" + ex.getValue() + "' no es válido para " + ex.getName());
    }

    /** Parámetros que no se pudieron decodificar (no UTF-8 o un escape % inválido): es un error del cliente. */
    @ExceptionHandler(InvalidParameterException.class)
    public ModelAndView parametroMalCodificado(InvalidParameterException ex) {
        return vista(HttpStatus.BAD_REQUEST, "Los datos enviados no tienen una codificación válida (UTF-8).");
    }

    /** Error no previsto: se registra completo en el log y en pantalla no se muestran detalles internos. */
    @ExceptionHandler(Exception.class)
    public ModelAndView inesperado(Exception ex, HttpServletRequest req) {
        log.error("Error no controlado en {}", req.getRequestURI(), ex);
        return vista(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno. Intente nuevamente.");
    }

    private ModelAndView vista(HttpStatus estado, String mensaje) {
        ModelAndView mav = new ModelAndView("error", estado);
        mav.addObject("status", estado.value());
        mav.addObject("error", estado.getReasonPhrase());
        mav.addObject("mensaje", mensaje);
        return mav;
    }
}
