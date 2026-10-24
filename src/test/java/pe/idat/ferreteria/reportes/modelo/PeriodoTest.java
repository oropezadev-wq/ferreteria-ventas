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
    void finDeMesConReferenciaEnElUltimoDia() {
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2026, 1, 31)))
                .isEqualTo(new Periodo(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)));
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2026, 4, 30)))
                .isEqualTo(new Periodo(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)));
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2026, 12, 31)))
                .isEqualTo(new Periodo(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31)));
    }

    @Test
    void febreroSegunAnioBisiesto() {
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2023, 2, 10)).hasta()).isEqualTo(LocalDate.of(2023, 2, 28));
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2024, 2, 29)).hasta()).isEqualTo(LocalDate.of(2024, 2, 29));
        // 2100 no es bisiesto (divisible entre 100 y no entre 400); 2000 sí.
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2100, 2, 1)).hasta()).isEqualTo(LocalDate.of(2100, 2, 28));
        assertThat(Periodo.de(Granularidad.MES, LocalDate.of(2000, 2, 1)).hasta()).isEqualTo(LocalDate.of(2000, 2, 29));
    }

    @Test
    void anioBisiestoIncluyeEl29DeFebrero() {
        Periodo anio = Periodo.de(Granularidad.ANIO, LocalDate.of(2024, 12, 31));

        assertThat(anio).isEqualTo(new Periodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        assertThat(anio.contiene(LocalDate.of(2024, 2, 29))).isTrue();
        assertThat(anio.contiene(LocalDate.of(2025, 1, 1))).isFalse();
        assertThat(anio.contiene(LocalDate.of(2023, 12, 31))).isFalse();
    }

    @Test
    void ningunaEquivaleAlDiaYUnSoloDiaEsValido() {
        Periodo dia = Periodo.de(Granularidad.NINGUNA, REFERENCIA);

        assertThat(dia).isEqualTo(Periodo.de(Granularidad.DIA, REFERENCIA));
        assertThat(dia.contiene(REFERENCIA)).isTrue();
        assertThat(dia.contiene(REFERENCIA.plusDays(1))).isFalse();
        assertThat(dia.contiene(REFERENCIA.minusDays(1))).isFalse();
    }

    @Test
    void nulosSeRechazanYContieneNuloEsFalso() {
        assertThatThrownBy(() -> new Periodo(null, REFERENCIA)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Periodo(REFERENCIA, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Periodo.de(null, REFERENCIA)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Periodo.de(Granularidad.MES, null)).isInstanceOf(NullPointerException.class);
        assertThat(Periodo.de(Granularidad.MES, REFERENCIA).contiene(null)).isFalse();
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
