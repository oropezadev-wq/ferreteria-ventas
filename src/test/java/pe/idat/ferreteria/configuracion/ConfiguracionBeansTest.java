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

    @Test
    void relojUsaLaZonaDeLima() {
        assertThat(reloj.getZone().getId()).isEqualTo("America/Lima");
    }
}
