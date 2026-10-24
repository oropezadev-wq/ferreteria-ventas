package pe.idat.ferreteria.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.configuracion.ParametrosTienda;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Codificación de los parámetros del formulario con el Tomcat real. MockMvc no sirve aquí: arma los parámetros
 * sin pasar por el decodificador de Tomcat, que es quien falla ante bytes que no son UTF-8.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfiguracionCodificacionTest {

    private static final String RESTO = "&ruc=20888888881&direccion=Lima&telefono=999888777"
            + "&tasaIgvPorcentaje=18&umbralStockBajo=5";

    @LocalServerPort
    private int puerto;

    private final HttpClient cliente = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

    @AfterEach
    void restaurar() {
        ConfiguracionGlobal.getInstancia().actualizar(ParametrosTienda.porDefecto());
    }

    private HttpResponse<String> enviar(String cuerpo) throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + "/configuracion"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo, StandardCharsets.US_ASCII))
                .build();
        return cliente.send(peticion, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    @Test
    void textoUtf8ConTildesYEnieLlegaIntacto() throws Exception {
        HttpResponse<String> respuesta = enviar("razonSocial=Ferreter%C3%ADa+%C3%91and%C3%BA" + RESTO);

        assertThat(respuesta.statusCode()).isEqualTo(302);
        assertThat(ConfiguracionGlobal.getInstancia().getRazonSocial()).isEqualTo("Ferretería Ñandú");
    }

    @Test
    void parametroMalCodificadoNoCambiaLaConfiguracionNiExponeDetalles() throws Exception {
        HttpResponse<String> respuesta = enviar("razonSocial=Ferreter%E1a" + RESTO);

        assertThat(ConfiguracionGlobal.getInstancia().getParametros()).isEqualTo(ParametrosTienda.porDefecto());
        assertThat(respuesta.body()).doesNotContain("InvalidParameterException").doesNotContain("MalformedInput");
    }

    @Test
    void parametroConBytesQueNoSonUtf8Responde400() throws Exception {
        // %E1 es 'á' en ISO-8859-1: no es una secuencia UTF-8 válida.
        assertThat(enviar("razonSocial=Ferreter%E1a" + RESTO).statusCode()).isEqualTo(400);
    }

    @Test
    void escapePorcentualInvalidoResponde400() throws Exception {
        assertThat(enviar("razonSocial=Ferreter%ZZa" + RESTO).statusCode()).isEqualTo(400);
    }
}
