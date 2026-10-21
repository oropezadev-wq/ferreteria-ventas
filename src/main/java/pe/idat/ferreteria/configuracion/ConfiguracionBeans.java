package pe.idat.ferreteria.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/** Beans de infraestructura compartidos por toda la aplicación. */
@Configuration
public class ConfiguracionBeans {

    /** Zona horaria de la tienda: todas las fechas de ventas y reportes se calculan en hora de Lima. */
    public static final ZoneId ZONA_TIENDA = ZoneId.of("America/Lima");

    /** Reloj inyectable: en las pruebas se reemplaza por {@link Clock#fixed} para obtener resultados deterministas. */
    @Bean
    public Clock reloj() {
        return Clock.system(ZONA_TIENDA);
    }

    /** Datos generales que muestra Swagger UI en /swagger-ui.html. */
    @Bean
    public OpenAPI documentacion() {
        return new OpenAPI().info(new Info()
                .title("API del gestor de ventas de ferretería")
                .version("0.1.0")
                .description("Consulta de productos, ventas y generación de reportes. Los datos se guardan en memoria."));
    }
}
