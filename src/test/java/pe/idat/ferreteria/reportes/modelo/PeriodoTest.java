package pe.idat.ferreteria.reportes.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PeriodoTest {

    private static final LocalDate REFERENCIA = LocalDate.of(2024, 2, 14);

    @Test
    void calculaElPeriodoSegunLaGranularidad() {
        assertThat(Periodo.de(Granularidad.DIA, REFERENCIA)).isEqualTo(new Periodo(REFERENCIA, REFERENCIA));
        assertThat(Periodo.de(Granularidad.MES, REFERENCIA))
                .isEqualTo(new Periodo(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)));
        assertThat(Periodo.de(Granularidad.ANIO, REFERENCIA))
                .isEqualTo(new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
    }

    @Test
    void incluyeAmbosExtremosYRechazaRangoInvertido() {
        Periodo febrero = Periodo.de(Granularidad.MES, REFERENCIA);

        assertThat(febrero.contiene(LocalDate.of(2024, 2, 1))).isTrue();
        assertThat(febrero.contiene(LocalDate.of(2024, 2, 29))).isTrue();
        assertThat(febrero.contiene(LocalDate.of(2024, 3, 1))).isFalse();
        assertThatThrownBy(() -> new Periodo(REFERENCIA, REFERENCIA.minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
