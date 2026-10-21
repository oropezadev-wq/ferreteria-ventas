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
    void comparaYDetectaCero() {
        assertThat(Dinero.de("10", PEN).esMayorOIgualQue(Dinero.de("10.00", PEN))).isTrue();
        assertThat(Dinero.de("9.99", PEN).esMayorOIgualQue(Dinero.de("10", PEN))).isFalse();
        assertThat(Dinero.cero(PEN).esCero()).isTrue();
        assertThat(Dinero.cero(PEN).sumar(Dinero.de("1", PEN))).isEqualTo(Dinero.de("1.00", PEN));
    }
}
