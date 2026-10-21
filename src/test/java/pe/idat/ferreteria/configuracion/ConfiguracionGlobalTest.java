package pe.idat.ferreteria.configuracion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.dominio.comun.Dinero;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfiguracionGlobalTest {

    private final ConfiguracionGlobal configuracion = ConfiguracionGlobal.getInstancia();

    /** El Singleton vive todo lo que dura la JVM de pruebas: cada prueba deja los valores iniciales. */
    @AfterEach
    void restaurar() {
        configuracion.actualizar(ParametrosTienda.porDefecto());
    }

    @Test
    void getInstanciaDevuelveSiempreLaMismaReferencia() {
        assertThat(ConfiguracionGlobal.getInstancia()).isSameAs(ConfiguracionGlobal.getInstancia());
    }

    @Test
    void cienHilosObtienenUnaSolaInstancia() throws InterruptedException {
        int hilos = 100;
        Set<ConfiguracionGlobal> instancias = ConcurrentHashMap.newKeySet();
        CountDownLatch largada = new CountDownLatch(1);
        CountDownLatch terminados = new CountDownLatch(hilos);
        ExecutorService ejecutor = Executors.newFixedThreadPool(hilos);
        try {
            for (int i = 0; i < hilos; i++) {
                ejecutor.submit(() -> {
                    try {
                        largada.await();
                        instancias.add(ConfiguracionGlobal.getInstancia());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        terminados.countDown();
                    }
                });
            }
            largada.countDown();
            assertThat(terminados.await(10, TimeUnit.SECONDS)).isTrue();
        } finally {
            ejecutor.shutdownNow();
        }

        assertThat(instancias).hasSize(1).containsExactly(configuracion);
    }

    @Test
    void claseFinalConUnicoConstructorPrivado() {
        Constructor<?>[] constructores = ConfiguracionGlobal.class.getDeclaredConstructors();

        assertThat(Modifier.isFinal(ConfiguracionGlobal.class.getModifiers())).isTrue();
        assertThat(constructores).hasSize(1);
        assertThat(Modifier.isPrivate(constructores[0].getModifiers())).isTrue();
        assertThat(java.io.Serializable.class.isAssignableFrom(ConfiguracionGlobal.class)).isFalse();
    }

    @Test
    void crearOtraInstanciaPorReflexionLanzaExcepcion() throws NoSuchMethodException {
        Constructor<ConfiguracionGlobal> constructor = ConfiguracionGlobal.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .cause().isInstanceOf(IllegalStateException.class).hasMessageContaining("getInstancia");
    }

    @Test
    void valoresPorDefecto() {
        assertThat(configuracion.getRazonSocial()).isEqualTo("Ferretería El Constructor S.A.C.");
        assertThat(configuracion.getMoneda().getCurrencyCode()).isEqualTo("PEN");
        assertThat(configuracion.getTasaIgv()).isEqualByComparingTo("0.18");
        assertThat(configuracion.getLocale().toLanguageTag()).isEqualTo("es-PE");
        assertThat(configuracion.getRuc()).matches("\\d{11}");
    }

    @Test
    void actualizarEsVisibleDesdeOtroHilo() throws Exception {
        ParametrosTienda nuevos = ParametrosTienda.porDefecto().conDatosEditables(
                "Ferretería Los Andes S.A.C.", "20888888881", "Jr. Unión 456, Lima", "999 888 777",
                new BigDecimal("0.10"), 8);

        configuracion.actualizar(nuevos);
        String leidoEnOtroHilo;
        ExecutorService otroHilo = Executors.newSingleThreadExecutor();
        try {
            leidoEnOtroHilo = otroHilo.submit(() -> ConfiguracionGlobal.getInstancia().getRazonSocial())
                    .get(5, TimeUnit.SECONDS);
        } finally {
            otroHilo.shutdownNow();
        }

        assertThat(leidoEnOtroHilo).isEqualTo("Ferretería Los Andes S.A.C.");
        assertThat(configuracion.getParametros()).isEqualTo(nuevos);
        assertThat(configuracion.getTasaIgv()).isEqualByComparingTo("0.10");
        assertThat(configuracion.getUmbralStockBajo()).isEqualTo(8);
    }

    @Test
    void parametrosInvalidosSeRechazanYNoCambianElEstado() {
        ParametrosTienda base = ParametrosTienda.porDefecto();

        assertThatThrownBy(() -> base.conDatosEditables(" ", "20999999991", "Lima", "999888777", new BigDecimal("0.18"), 5))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> base.conDatosEditables("Tienda", "12345", "Lima", "999888777", new BigDecimal("0.18"), 5))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("RUC");
        assertThatThrownBy(() -> base.conDatosEditables("Tienda", "20999999991", "Lima", "999888777", new BigDecimal("0.50"), 5))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("IGV");
        assertThatThrownBy(() -> base.conDatosEditables("Tienda", "20999999991", "Lima", "999888777", new BigDecimal("0.18"), -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> configuracion.actualizar(null)).isInstanceOf(NullPointerException.class);

        assertThat(configuracion.getParametros()).isEqualTo(base);
    }

    @Test
    void noExponeViasParaDuplicarse() {
        assertThat(ConfiguracionGlobal.class.getConstructors()).isEmpty();
        assertThat(Cloneable.class.isAssignableFrom(ConfiguracionGlobal.class)).isFalse();
        assertThat(java.util.Arrays.stream(ConfiguracionGlobal.class.getDeclaredFields())
                .allMatch(f -> Modifier.isPrivate(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))).isTrue();
    }

    @Test
    void lectoresConcurrentesNuncaVenUnaFotoMezclada() throws Exception {
        ParametrosTienda a = ParametrosTienda.porDefecto();
        ParametrosTienda b = a.conDatosEditables("Ferretería Los Andes S.A.C.", "20888888881", "Jr. Unión 456, Lima",
                "999 888 777", new BigDecimal("0.10"), 8);
        AtomicBoolean seguir = new AtomicBoolean(true);
        AtomicInteger inconsistentes = new AtomicInteger();
        AtomicInteger lecturas = new AtomicInteger();
        CountDownLatch listos = new CountDownLatch(4);
        ExecutorService ejecutor = Executors.newFixedThreadPool(5);
        try {
            ejecutor.submit(() -> {
                listos.await();   // en máquinas con pocos núcleos el escritor podría terminar antes de que lean
                for (int i = 0; i < 20_000; i++) {
                    configuracion.actualizar(i % 2 == 0 ? b : a);
                }
                seguir.set(false);
                return null;
            });
            for (int r = 0; r < 4; r++) {
                ejecutor.submit(() -> {
                    listos.countDown();
                    do {
                        ParametrosTienda foto = configuracion.getParametros();
                        lecturas.incrementAndGet();
                        boolean esA = foto.ruc().equals(a.ruc()) && foto.tasaIgv().equals(a.tasaIgv())
                                && foto.razonSocial().equals(a.razonSocial());
                        boolean esB = foto.ruc().equals(b.ruc()) && foto.tasaIgv().equals(b.tasaIgv())
                                && foto.razonSocial().equals(b.razonSocial());
                        if (!esA && !esB) {
                            inconsistentes.incrementAndGet();
                        }
                    } while (seguir.get());
                });
            }
            ejecutor.shutdown();
            assertThat(ejecutor.awaitTermination(20, TimeUnit.SECONDS)).isTrue();
        } finally {
            ejecutor.shutdownNow();
        }

        assertThat(lecturas.get()).isPositive();
        assertThat(inconsistentes.get()).isZero();
    }

    @Test
    void actualizarReemplazaLaFotoSinAlterarLaAnterior() {
        ParametrosTienda antes = configuracion.getParametros();
        ParametrosTienda nuevos = antes.conDatosEditables("Otra", "10123456789", "Cusco", "984111222",
                new BigDecimal("0.10"), 9);

        configuracion.actualizar(nuevos);

        assertThat(antes).isEqualTo(ParametrosTienda.porDefecto());
        assertThat(configuracion.getParametros()).isSameAs(nuevos);
        assertThat(configuracion.getRuc()).isEqualTo("10123456789");
    }

    @Test
    void formateaCeroMontosGrandesYRechazaNulos() {
        assertThat(configuracion.formatearDinero(Dinero.cero(configuracion.getMoneda()))).isEqualTo("S/ 0.00");
        assertThat(configuracion.formatearDinero(Dinero.de("1234567.891", configuracion.getMoneda())))
                .isEqualTo("S/ 1,234,567.89");
        assertThat(configuracion.formatearFechaHora(LocalDateTime.of(2024, 2, 29, 23, 59)))
                .isEqualTo("29/02/2024 23:59");
        assertThatThrownBy(() -> configuracion.formatearDinero(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> configuracion.formatearFecha(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> configuracion.formatearFechaHora(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void formateaMontosYFechasConElLocaleConfigurado() {
        Dinero monto = Dinero.de("1234.5", configuracion.getMoneda());

        assertThat(configuracion.formatearDinero(monto)).isEqualTo("S/ 1,234.50");
        assertThat(configuracion.formatearFecha(LocalDate.of(2026, 10, 5))).isEqualTo("05/10/2026");
        assertThat(configuracion.formatearFechaHora(LocalDateTime.of(2026, 10, 5, 9, 7))).isEqualTo("05/10/2026 09:07");
    }
}
