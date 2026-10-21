package pe.idat.ferreteria.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.configuracion.ParametrosTienda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ConfiguracionControllerTest {

    @Autowired
    private MockMvc mvc;

    @AfterEach
    void restaurar() {
        ConfiguracionGlobal.getInstancia().actualizar(ParametrosTienda.porDefecto());
    }

    @Test
    void getMuestraElFormularioConLosDatosVigentes() throws Exception {
        mvc.perform(get("/configuracion"))
                .andExpect(status().isOk())
                .andExpect(view().name("configuracion"))
                .andExpect(content().string(containsString("value=\"20999999991\"")))
                .andExpect(content().string(containsString("value=\"18.00\"")));
    }

    @Test
    void postValidoActualizaYRedirige() throws Exception {
        mvc.perform(post("/configuracion")
                        .param("razonSocial", "Ferretería Los Andes S.A.C.")
                        .param("ruc", "20888888881")
                        .param("direccion", "Jr. Unión 456, Lima")
                        .param("telefono", "999 888 777")
                        .param("tasaIgvPorcentaje", "18")
                        .param("umbralStockBajo", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracion"))
                .andExpect(flash().attribute("mensaje", "Configuración guardada."));

        ConfiguracionGlobal configuracion = ConfiguracionGlobal.getInstancia();
        assertThat(configuracion.getRazonSocial()).isEqualTo("Ferretería Los Andes S.A.C.");
        assertThat(configuracion.getTasaIgv()).isEqualByComparingTo("0.18");
        assertThat(configuracion.getUmbralStockBajo()).isEqualTo(7);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ferretería Los Andes S.A.C.")))
                .andExpect(content().string(containsString("20888888881")));
    }

    @Test
    void postInvalidoMuestraErroresYNoCambiaLaConfiguracion() throws Exception {
        mvc.perform(post("/configuracion")
                        .param("razonSocial", "")
                        .param("ruc", "123")
                        .param("direccion", "Jr. Unión 456, Lima")
                        .param("telefono", "999 888 777")
                        .param("tasaIgvPorcentaje", "45")
                        .param("umbralStockBajo", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("configuracion"))
                .andExpect(model().attributeHasFieldErrors("formulario",
                        "razonSocial", "ruc", "tasaIgvPorcentaje", "umbralStockBajo"))
                .andExpect(content().string(containsString("is-invalid")))
                .andExpect(content().string(containsString("Ingrese la razón social")));

        assertThat(ConfiguracionGlobal.getInstancia().getParametros()).isEqualTo(ParametrosTienda.porDefecto());
    }
}
