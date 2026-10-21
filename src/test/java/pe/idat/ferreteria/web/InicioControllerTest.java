package pe.idat.ferreteria.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/** Comprueba que Thymeleaf renderiza con Spring Boot 4 y que Bootstrap se sirve desde static/vendor. */
@SpringBootTest
@AutoConfigureMockMvc
class InicioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void inicioRenderizaConElLayout() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("inicio"))
                .andExpect(content().string(containsString("Gestor de ventas")))
                .andExpect(content().string(containsString("/vendor/bootstrap/css/bootstrap.min.css")))
                .andExpect(content().string(containsString("navbar")));
    }

    @Test
    void bootstrapLocalSeSirve() throws Exception {
        mvc.perform(get("/vendor/bootstrap/css/bootstrap.min.css")).andExpect(status().isOk());
        mvc.perform(get("/vendor/bootstrap/js/bootstrap.bundle.min.js")).andExpect(status().isOk());
    }
}
