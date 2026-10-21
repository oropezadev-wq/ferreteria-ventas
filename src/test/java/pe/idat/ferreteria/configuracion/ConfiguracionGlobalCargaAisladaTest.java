package pe.idat.ferreteria.configuracion;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * En las demás pruebas el Singleton ya está creado, así que la carrera de la primera llamada no se ejercita.
 * Aquí se carga ConfiguracionGlobal en un ClassLoader nuevo (sin delegar en el de la aplicación) para probar
 * el estado "todavía no existe la instancia": primera llamada concurrente y ataque por reflexión antes de
 * cualquier getInstancia().
 */
class ConfiguracionGlobalCargaAisladaTest {

    private static final String CLASE = "pe.idat.ferreteria.configuracion.ConfiguracionGlobal";

    private static URLClassLoader cargadorNuevo() {
        URL clases = ConfiguracionGlobal.class.getProtectionDomain().getCodeSource().getLocation();
        // Padre = cargador de plataforma: las clases del proyecto se vuelven a cargar desde target/classes.
        return new URLClassLoader(new URL[]{clases}, ClassLoader.getPlatformClassLoader());
    }

    @Test
    void elCargadorAisladoTieneSuPropiaClase() throws Exception {
        try (URLClassLoader cargador = cargadorNuevo()) {
            Class<?> aislada = Class.forName(CLASE, false, cargador);

            assertThat(aislada).isNotSameAs(ConfiguracionGlobal.class);
            assertThat(aislada.getClassLoader()).isSameAs(cargador);
        }
    }

    @Test
    void cienHilosEnLaPrimeraLlamadaObtienenUnaSolaInstancia() throws Exception {
        int hilos = 100;
        try (URLClassLoader cargador = cargadorNuevo()) {
            Method getInstancia = Class.forName(CLASE, false, cargador).getMethod("getInstancia");
            CountDownLatch largada = new CountDownLatch(1);
            ExecutorService ejecutor = Executors.newFixedThreadPool(hilos);
            List<Future<Object>> resultados = new ArrayList<>();
            try {
                for (int i = 0; i < hilos; i++) {
                    resultados.add(ejecutor.submit(() -> {
                        largada.await();
                        return getInstancia.invoke(null);
                    }));
                }
                largada.countDown();
                Set<Object> instancias = Collections.newSetFromMap(new IdentityHashMap<>());
                for (Future<Object> resultado : resultados) {
                    instancias.add(resultado.get(10, TimeUnit.SECONDS));
                }

                assertThat(instancias).hasSize(1);
                assertThat(instancias.iterator().next()).isNotNull();
            } finally {
                ejecutor.shutdownNow();
            }
        }
    }

    @Test
    void reflexionAntesDeLaPrimeraLlamadaTambienSeBloquea() throws Exception {
        try (URLClassLoader cargador = cargadorNuevo()) {
            Class<?> aislada = Class.forName(CLASE, true, cargador);
            Constructor<?> constructor = aislada.getDeclaredConstructor();
            constructor.setAccessible(true);

            assertThatThrownBy(constructor::newInstance)
                    .isInstanceOf(InvocationTargetException.class)
                    .cause().isInstanceOf(IllegalStateException.class);

            // El intento fallido no deja el Singleton roto: getInstancia sigue funcionando y es única.
            Method getInstancia = aislada.getMethod("getInstancia");
            Object primera = getInstancia.invoke(null);
            assertThat(primera).isNotNull().isSameAs(getInstancia.invoke(null));
            assertThat(aislada.getMethod("getRazonSocial").invoke(primera))
                    .isEqualTo("Ferretería El Constructor S.A.C.");
        }
    }
}
