package pe.idat.ferreteria.configuracion;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;

/** El bean que inyecta Spring es el mismo objeto que devuelve el Singleton, sea cual sea la interfaz pedida. */
@SpringBootTest
class ConfiguracionBeansTest {

    @Autowired
    private ParametrosFiscales fiscales;

    @Autowired
    private FormatoRegional formato;

    @Autowired
    private DatosTienda tienda;

    @Autowired
    private ConfiguracionGlobal configuracion;

    @Autowired
    private Clock reloj;

    @Test
    void cadaInterfazInyectadaEsLaInstanciaDelSingleton() {
        ConfiguracionGlobal instancia = ConfiguracionGlobal.getInstancia();

        assertThat(fiscales).isSameAs(instancia);
        assertThat(formato).isSameAs(instancia);
        assertThat(tienda).isSameAs(instancia);
        assertThat(configuracion).isSameAs(instancia);
    }

    @Autowired
    private org.springframework.context.ApplicationContext contexto;

    @Test
    void elContextoTieneUnSoloBeanPorCadaInterfazDeConfiguracion() {
        assertThat(contexto.getBeansOfType(ConfiguracionGlobal.class)).hasSize(1);
        assertThat(contexto.getBeansOfType(ParametrosFiscales.class)).hasSize(1);
        assertThat(contexto.getBeansOfType(FormatoRegional.class)).hasSize(1);
        assertThat(contexto.getBeansOfType(DatosTienda.class)).hasSize(1);
        assertThat(contexto.isSingleton("configuracionGlobal")).isTrue();
        assertThat(contexto.getBean("configuracionGlobal")).isSameAs(ConfiguracionGlobal.getInstancia());
    }

    @Test
    void relojUsaLaZonaDeLima() {
        assertThat(reloj.getZone().getId()).isEqualTo("America/Lima");
    }
}
