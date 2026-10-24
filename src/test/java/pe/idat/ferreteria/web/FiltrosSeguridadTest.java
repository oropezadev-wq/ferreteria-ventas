package pe.idat.ferreteria.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.configuracion.ParametrosTienda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FiltrosSeguridadTest {

    @Autowired
    private MockMvc mvc;

    @AfterEach
    void restaurar() {
        ConfiguracionGlobal.getInstancia().actualizar(ParametrosTienda.porDefecto());
    }

    private MockHttpServletRequestBuilder postValido() {
        return post("/configuracion")
                .param("razonSocial", "Ferretería Prueba S.A.C.")
                .param("ruc", "20999999991")
                .param("direccion", "Av. Prueba 123")
                .param("telefono", "014445555")
                .param("tasaIgvPorcentaje", "18")
                .param("umbralStockBajo", "5");
    }

    @Test
    void postDesdeOtroOrigenSeRechazaYNoCambiaLaConfiguracion() throws Exception {
        mvc.perform(postValido().header("Origin", "http://malicioso.example"))
                .andExpect(status().isForbidden());

        assertThat(ConfiguracionGlobal.getInstancia().getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }

    @Test
    void postMarcadoComoEntreSitiosSeRechaza() throws Exception {
        mvc.perform(postValido().header("Sec-Fetch-Site", "cross-site"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDelMismoOrigenSeAcepta() throws Exception {
        mvc.perform(postValido().header("Host", "localhost").header("Origin", "http://localhost")
                        .header("Sec-Fetch-Site", "same-origin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion"));
    }

    @Test
    void postSinCabecerasDeNavegadorSeAcepta() throws Exception {
        mvc.perform(postValido()).andExpect(status().is3xxRedirection());
    }

    @Test
    void lasPantallasEnvianCabecerasDeSeguridad() throws Exception {
        mvc.perform(get("/"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("Content-Security-Policy", CabecerasSeguridadFilter.CSP));
    }
}
