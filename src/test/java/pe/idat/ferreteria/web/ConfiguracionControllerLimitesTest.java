package pe.idat.ferreteria.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.configuracion.ParametrosTienda;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/** POST /configuracion con valores límite, textos con tildes, datos que no se pueden convertir y HTML. */
@SpringBootTest
@AutoConfigureMockMvc
class ConfiguracionControllerLimitesTest {

    @Autowired
    private MockMvc mvc;

    private final ConfiguracionGlobal configuracion = ConfiguracionGlobal.getInstancia();

    @AfterEach
    void restaurar() {
        configuracion.actualizar(ParametrosTienda.porDefecto());
    }

    private static Map<String, String> validos() {
        Map<String, String> datos = new LinkedHashMap<>();
        datos.put("razonSocial", "Ferretería Los Andes S.A.C.");
        datos.put("ruc", "20888888881");
        datos.put("direccion", "Jr. Unión 456, Lima");
        datos.put("telefono", "999 888 777");
        datos.put("tasaIgvPorcentaje", "18");
        datos.put("umbralStockBajo", "7");
        return datos;
    }

    private static MockHttpServletRequestBuilder postCon(String campo, String valor) {
        Map<String, String> datos = validos();
        datos.put(campo, valor);
        MockHttpServletRequestBuilder peticion = post("/configuracion");
        datos.forEach(peticion::param);
        return peticion;
    }

    @ParameterizedTest(name = "{0} = ''{1}'' se acepta")
    @CsvSource({
            "tasaIgvPorcentaje, 0",
            "tasaIgvPorcentaje, 30",
            "tasaIgvPorcentaje, 30.00",
            "tasaIgvPorcentaje, 18.5",
            "umbralStockBajo, 0",
            "umbralStockBajo, 1000",
            "ruc, 10456789012",
            "ruc, 15123456789",
            "ruc, 17123456789",
            "telefono, 555012",
            "telefono, 12345678901234567890"
    })
    void valoresEnElLimiteSeAceptan(String campo, String valor) throws Exception {
        mvc.perform(postCon(campo, valor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion"));

        assertThat(configuracion.getParametros()).isNotEqualTo(ParametrosTienda.porDefecto());
    }

    @ParameterizedTest(name = "{0} = ''{1}'' se rechaza")
    @CsvSource({
            "tasaIgvPorcentaje, 30.01",
            "tasaIgvPorcentaje, -0.01",
            "tasaIgvPorcentaje, 18.123",
            "tasaIgvPorcentaje, 100",
            "umbralStockBajo, 1001",
            "umbralStockBajo, -1",
            "ruc, 30123456789",
            "ruc, 2088888888",
            "ruc, 208888888811",
            "telefono, 55501",
            "telefono, 123456789012345678901",
            "telefono, 999-ABC-777"
    })
    void valoresFueraDelLimiteSeRechazanSinCambiarLaConfiguracion(String campo, String valor) throws Exception {
        mvc.perform(postCon(campo, valor))
                .andExpect(status().isOk())
                .andExpect(view().name("configuracion"))
                .andExpect(model().attributeHasFieldErrors("formulario", campo));

        assertThat(configuracion.getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }

    @Test
    void razonSocialDe100CaracteresSeAceptaY101SeRechaza() throws Exception {
        mvc.perform(postCon("razonSocial", "Ñ".repeat(100))).andExpect(status().is3xxRedirection());
        assertThat(configuracion.getRazonSocial()).isEqualTo("Ñ".repeat(100));

        configuracion.actualizar(ParametrosTienda.porDefecto());
        mvc.perform(postCon("razonSocial", "Ñ".repeat(101)))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "razonSocial"));
        assertThat(configuracion.getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }

    @Test
    void direccionDe150CaracteresSeAceptaY151SeRechaza() throws Exception {
        mvc.perform(postCon("direccion", "á".repeat(150))).andExpect(status().is3xxRedirection());
        assertThat(configuracion.getDireccion()).hasSize(150);

        configuracion.actualizar(ParametrosTienda.porDefecto());
        mvc.perform(postCon("direccion", "á".repeat(151)))
                .andExpect(model().attributeHasFieldErrors("formulario", "direccion"));
    }

    @Test
    void tasaConDecimalesSeGuardaComoFraccion() throws Exception {
        mvc.perform(postCon("tasaIgvPorcentaje", "18.5")).andExpect(status().is3xxRedirection());

        assertThat(configuracion.getTasaIgv()).isEqualByComparingTo("0.185");
        mvc.perform(get("/configuracion")).andExpect(content().string(containsString("value=\"18.50\"")));
    }

    @Test
    void espaciosAlrededorSeRecortanAlGuardar() throws Exception {
        mvc.perform(postCon("razonSocial", "   Ferretería Ñandú   ")).andExpect(status().is3xxRedirection());

        assertThat(configuracion.getRazonSocial()).isEqualTo("Ferretería Ñandú");
    }

    @Test
    void textoConTildesEnviadoComoFormularioUtf8SeConservaYSeMuestra() throws Exception {
        String cuerpo = "razonSocial=Ferreter%C3%ADa+%C3%91and%C3%BA+%26+Cia&ruc=20888888881"
                + "&direccion=Av.+Jos%C3%A9+G%C3%A1lvez+120%2C+Ca%C3%B1ete&telefono=999+888+777"
                + "&tasaIgvPorcentaje=18&umbralStockBajo=7";

        mvc.perform(post("/configuracion")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(cuerpo))
                .andExpect(status().is3xxRedirection());

        assertThat(configuracion.getRazonSocial()).isEqualTo("Ferretería Ñandú & Cia");
        assertThat(configuracion.getDireccion()).isEqualTo("Av. José Gálvez 120, Cañete");
        mvc.perform(get("/"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(content().string(containsString("Ferretería Ñandú &amp; Cia")))
                .andExpect(content().string(containsString("Av. José Gálvez 120, Cañete")));
    }

    @Test
    void htmlEnLaRazonSocialSeEscapaEnElLayout() throws Exception {
        mvc.perform(postCon("razonSocial", "<script>alert(1)</script>")).andExpect(status().is3xxRedirection());

        mvc.perform(get("/configuracion"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("<script>alert(1)</script>"))))
                .andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")));
    }

    @Test
    void valoresNoNumericosSonErrorDeCampoYNo500() throws Exception {
        mvc.perform(postCon("tasaIgvPorcentaje", "abc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "tasaIgvPorcentaje"));
        mvc.perform(postCon("umbralStockBajo", "99999999999"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "umbralStockBajo"));
        mvc.perform(postCon("umbralStockBajo", "7.5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "umbralStockBajo"));

        assertThat(configuracion.getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }

    @Test
    void valorNoNumericoMuestraUnMensajeEntendible() throws Exception {
        mvc.perform(postCon("tasaIgvPorcentaje", "abc"))
                .andExpect(content().string(not(containsString("Failed to convert"))))
                .andExpect(content().string(not(containsString("java.math.BigDecimal"))));
    }

    @Test
    void camposAusentesSonErroresDeCampo() throws Exception {
        mvc.perform(post("/configuracion"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "razonSocial", "ruc", "direccion",
                        "telefono", "tasaIgvPorcentaje", "umbralStockBajo"));

        assertThat(configuracion.getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }

    @Test
    void soloEspaciosEnCamposDeTextoSeRechaza() throws Exception {
        mvc.perform(postCon("direccion", "    "))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("formulario", "direccion"));
    }

    @Test
    void campoNoEditableEnviadoPorElClienteSeIgnora() throws Exception {
        mvc.perform(postCon("razonSocial", "Tienda X").param("moneda", "USD").param("locale", "en-US"))
                .andExpect(status().is3xxRedirection());

        assertThat(configuracion.getMoneda().getCurrencyCode()).isEqualTo("PEN");
        assertThat(configuracion.getLocale().toLanguageTag()).isEqualTo("es-PE");
    }
}
