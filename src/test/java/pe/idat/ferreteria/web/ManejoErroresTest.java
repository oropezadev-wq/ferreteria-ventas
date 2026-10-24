package pe.idat.ferreteria.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import pe.idat.ferreteria.dominio.comun.RecursoNoEncontradoException;
import pe.idat.ferreteria.dominio.comun.StockInsuficienteException;
import pe.idat.ferreteria.web.api.ErrorResponse;
import pe.idat.ferreteria.web.api.GlobalExceptionHandler;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class ManejoErroresTest {

    private final ManejadorErroresWeb web = new ManejadorErroresWeb();
    private final GlobalExceptionHandler api = new GlobalExceptionHandler(
            Clock.fixed(Instant.parse("2026-10-20T15:00:00Z"), ZoneId.of("America/Lima")));
    private final MockHttpServletRequest peticion = new MockHttpServletRequest("GET", "/api/productos/9");

    @Test
    void pantallasMuestranLaVistaDeErrorConElEstadoAdecuado() {
        ModelAndView noEncontrado = web.noEncontrado(new RecursoNoEncontradoException("Producto", 9L));
        ModelAndView regla = web.reglaNegocio(new StockInsuficienteException("Stock insuficiente de ELE-0012"));
        ModelAndView inesperado = web.inesperado(new IllegalStateException("detalle interno"), peticion);

        assertThat(noEncontrado.getViewName()).isEqualTo("error");
        assertThat(noEncontrado.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(noEncontrado.getModel()).containsEntry("mensaje", "Producto con id 9 no existe");
        assertThat(regla.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(inesperado.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(inesperado.getModel().get("mensaje").toString()).doesNotContain("detalle interno");
    }

    @Test
    void apiRespondeConErrorResponse() {
        ResponseEntity<ErrorResponse> noEncontrado = api.noEncontrado(new RecursoNoEncontradoException("Producto", 9L), peticion);
        ResponseEntity<ErrorResponse> argumento = api.argumentoInvalido(new IllegalArgumentException("Dato inválido"), peticion);
        ResponseEntity<ErrorResponse> inesperado = api.inesperado(new IllegalStateException("detalle interno"), peticion);

        assertThat(noEncontrado.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(noEncontrado.getBody()).isNotNull();
        assertThat(noEncontrado.getBody().ruta()).isEqualTo("/api/productos/9");
        assertThat(noEncontrado.getBody().fecha()).isEqualTo(LocalDateTime.of(2026, 10, 20, 10, 0));
        assertThat(argumento.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(inesperado.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(inesperado.getBody().mensaje()).doesNotContain("detalle interno");
    }
}
