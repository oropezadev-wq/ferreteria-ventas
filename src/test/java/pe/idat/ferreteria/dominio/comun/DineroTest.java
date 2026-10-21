package pe.idat.ferreteria.dominio.comun;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DineroTest {

    private static final Currency PEN = Currency.getInstance("PEN");
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void redondeaAEscalaDosConHalfUp() {
        assertThat(Dinero.de("10.005", PEN).monto()).isEqualByComparingTo("10.01");
        assertThat(Dinero.de("10.004", PEN).monto()).isEqualByComparingTo("10.00");
        assertThat(Dinero.de("10.005", PEN).monto().scale()).isEqualTo(2);
    }

    @Test
    void multiplicarRedondeaElResultado() {
        Dinero precio = Dinero.de("2.10", PEN);

        assertThat(precio.multiplicar(3)).isEqualTo(Dinero.de("6.30", PEN));
        assertThat(precio.multiplicar(new BigDecimal("0.18"))).isEqualTo(Dinero.de("0.38", PEN));
    }

    @Test
    void sumarYRestarEnLaMismaMoneda() {
        Dinero a = Dinero.de("39.90", PEN);
        Dinero b = Dinero.de("10.10", PEN);

        assertThat(a.sumar(b)).isEqualTo(Dinero.de("50.00", PEN));
        assertThat(a.restar(b)).isEqualTo(Dinero.de("29.80", PEN));
    }

    @Test
    void operarConOtraMonedaLanzaMonedaIncompatible() {
        Dinero soles = Dinero.de("10", PEN);
        Dinero dolares = Dinero.de("10", USD);

        assertThatThrownBy(() -> soles.sumar(dolares))
                .isInstanceOf(MonedaIncompatibleException.class)
                .hasMessageContaining("PEN").hasMessageContaining("USD");
        assertThatThrownBy(() -> soles.restar(dolares)).isInstanceOf(MonedaIncompatibleException.class);
        assertThatThrownBy(() -> soles.esMayorOIgualQue(dolares)).isInstanceOf(MonedaIncompatibleException.class);
    }

    @Test
    void noAdmiteMontosNegativosNiNulos() {
        assertThatThrownBy(() -> Dinero.de("-0.01", PEN)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Dinero.de("5", PEN).restar(Dinero.de("5.01", PEN)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Dinero(null, PEN)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Dinero(BigDecimal.ONE, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void igualdadIgnoraLaEscalaDeEntradaPeroNoLaMoneda() {
        assertThat(Dinero.de("10", PEN)).isEqualTo(Dinero.de("10.00", PEN));
        assertThat(Dinero.de("10", PEN)).hasSameHashCodeAs(Dinero.de("10.00", PEN));
        assertThat(Dinero.de("10", PEN)).isNotEqualTo(Dinero.de("10", USD));
    }

    @Test
    void redondeoEsHalfUpYNoHalfEven() {
        // HALF_EVEN daría 10.02 y 0.12: estos casos distinguen el modo de redondeo.
        assertThat(Dinero.de("10.025", PEN).monto()).isEqualByComparingTo("10.03");
        assertThat(Dinero.de("0.125", PEN).monto()).isEqualByComparingTo("0.13");
        assertThat(Dinero.de("2.50", PEN).multiplicar(new BigDecimal("0.15"))).isEqualTo(Dinero.de("0.38", PEN));
        assertThat(Dinero.de("10.0049999", PEN).monto()).isEqualByComparingTo("10.00");
    }

    @Test
    void igvAlCentimo() {
        assertThat(Dinero.de("39.90", PEN).multiplicar(new BigDecimal("0.18"))).isEqualTo(Dinero.de("7.18", PEN));
        assertThat(Dinero.de("32.50", PEN).multiplicar(new BigDecimal("0.18"))).isEqualTo(Dinero.de("5.85", PEN));
    }

    @Test
    void negativoQueRedondeaACeroSeAceptaComoCero() {
        // -0.004 redondea a 0.00 antes de validar el signo: se acepta como cero (comportamiento documentado aquí).
        assertThat(Dinero.de("-0.004", PEN)).isEqualTo(Dinero.cero(PEN));
        assertThat(Dinero.de("-0.004", PEN).esCero()).isTrue();
        assertThatThrownBy(() -> Dinero.de("-0.005", PEN)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void multiplicarPorCeroNegativoONulo() {
        Dinero precio = Dinero.de("8.50", PEN);

        assertThat(precio.multiplicar(0)).isEqualTo(Dinero.cero(PEN));
        assertThatThrownBy(() -> precio.multiplicar(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> precio.multiplicar((BigDecimal) null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void restarHastaCeroEsValido() {
        assertThat(Dinero.de("5", PEN).restar(Dinero.de("5.00", PEN)).esCero()).isTrue();
    }

    @Test
    void sumasRepetidasNoAcumulanErrorDeComaFlotante() {
        Dinero total = Dinero.cero(PEN);
        for (int i = 0; i < 10; i++) {
            total = total.sumar(Dinero.de("0.10", PEN));
        }

        assertThat(total).isEqualTo(Dinero.de("1.00", PEN));
    }

    @Test
    void escalaSeNormalizaAunConNotacionCientifica() {
        assertThat(new Dinero(new BigDecimal("1E+1"), PEN)).isEqualTo(Dinero.de("10.00", PEN));
        assertThat(new Dinero(new BigDecimal("1E+1"), PEN).monto().scale()).isEqualTo(2);
        assertThat(Dinero.de("99999999999.99", PEN).sumar(Dinero.de("0.01", PEN)))
                .isEqualTo(Dinero.de("100000000000", PEN));
    }

    @Test
    void textoNoNumericoONuloSeRechaza() {
        assertThatThrownBy(() -> Dinero.de("abc", PEN)).isInstanceOf(NumberFormatException.class)
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Dinero.de((String) null, PEN)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Dinero.de("10", PEN).sumar(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Dinero.de("10", PEN).compareTo(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void toStringUsaCodigoDeMonedaYDosDecimales() {
        assertThat(Dinero.de("39.9", PEN)).hasToString("PEN 39.90");
        assertThat(Dinero.de("1E+3", PEN)).hasToString("PEN 1000.00");
    }

    @Test
    void comparaYDetectaCero() {
        assertThat(Dinero.de("10", PEN).esMayorOIgualQue(Dinero.de("10.00", PEN))).isTrue();
        assertThat(Dinero.de("9.99", PEN).esMayorOIgualQue(Dinero.de("10", PEN))).isFalse();
        assertThat(Dinero.cero(PEN).esCero()).isTrue();
        assertThat(Dinero.cero(PEN).sumar(Dinero.de("1", PEN))).isEqualTo(Dinero.de("1.00", PEN));
    }

    @Test
    void atajoSolesUsaLaMonedaPeruana() {
        assertThat(Dinero.soles("39.9")).isEqualTo(Dinero.de("39.90", PEN));
        assertThat(Dinero.soles(new java.math.BigDecimal("10"))).isEqualTo(Dinero.de("10.00", PEN));
        assertThat(Dinero.SOLES).isEqualTo(PEN);
    }
}
