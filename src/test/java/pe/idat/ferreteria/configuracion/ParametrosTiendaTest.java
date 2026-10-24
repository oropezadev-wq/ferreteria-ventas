package pe.idat.ferreteria.configuracion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Encapsulamiento del estado del Singleton: un ParametrosTienda inválido no se puede construir. */
class ParametrosTiendaTest {

    private static final ParametrosTienda BASE = ParametrosTienda.porDefecto();
    private static final BigDecimal IGV = new BigDecimal("0.18");

    private static ParametrosTienda conRazon(String razon) {
        return BASE.conDatosEditables(razon, "20999999991", "Lima", "999888777", IGV, 5);
    }

    private static ParametrosTienda conRuc(String ruc) {
        return BASE.conDatosEditables("Tienda", ruc, "Lima", "999888777", IGV, 5);
    }

    private static ParametrosTienda conDireccion(String direccion) {
        return BASE.conDatosEditables("Tienda", "20999999991", direccion, "999888777", IGV, 5);
    }

    private static ParametrosTienda conTelefono(String telefono) {
        return BASE.conDatosEditables("Tienda", "20999999991", "Lima", telefono, IGV, 5);
    }

    private static ParametrosTienda conTasa(String tasa) {
        return BASE.conDatosEditables("Tienda", "20999999991", "Lima", "999888777", new BigDecimal(tasa), 5);
    }

    private static ParametrosTienda conUmbral(int umbral) {
        return BASE.conDatosEditables("Tienda", "20999999991", "Lima", "999888777", IGV, umbral);
    }

    @Test
    void porDefectoEsValidoYConsistenteConLaConfiguracionInicial() {
        assertThat(BASE.ruc()).isEqualTo("20999999991");
        assertThat(BASE.moneda()).isEqualTo(Currency.getInstance("PEN"));
        assertThat(BASE.redondeo()).isEqualTo(RoundingMode.HALF_UP);
        assertThat(BASE.umbralStockBajo()).isEqualTo(5);
        assertThat(ParametrosTienda.porDefecto()).isEqualTo(BASE);
    }

    @Test
    void razonSocialRespetaElLargoMaximoDespuesDeQuitarEspacios() {
        String cien = "Ñ".repeat(ParametrosTienda.LARGO_MAXIMO_RAZON_SOCIAL);

        assertThat(conRazon(cien).razonSocial()).hasSize(100);
        assertThat(conRazon("  " + cien + "  ").razonSocial()).isEqualTo(cien);
        assertThatThrownBy(() -> conRazon(cien + "Ñ"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("100");
    }

    @Test
    void textoConTildesYEspaciosSeConservaRecortado() {
        ParametrosTienda p = BASE.conDatosEditables("  Ferretería Ñandú E.I.R.L. ", " 10456789012 ",
                "\tJr. Ayacucho 789, Cañete\n", " +51 (01) 555-0123 ", IGV, 5);

        assertThat(p.razonSocial()).isEqualTo("Ferretería Ñandú E.I.R.L.");
        assertThat(p.ruc()).isEqualTo("10456789012");
        assertThat(p.direccion()).isEqualTo("Jr. Ayacucho 789, Cañete");
        assertThat(p.telefono()).isEqualTo("+51 (01) 555-0123");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t\n"})
    void camposDeTextoEnBlancoSeRechazan(String blanco) {
        assertThatThrownBy(() -> conRazon(blanco)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conRuc(blanco)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conDireccion(blanco)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTelefono(blanco)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void camposDeTextoNulosSeRechazan() {
        assertThatThrownBy(() -> conRazon(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conRuc(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conDireccion(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTelefono(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void direccionAdmiteHasta150Caracteres() {
        assertThat(conDireccion("a".repeat(150)).direccion()).hasSize(150);
        assertThatThrownBy(() -> conDireccion("a".repeat(151))).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10123456789", "15123456789", "17123456789", "20123456789"})
    void rucConPrefijoValidoSeAcepta(String ruc) {
        assertThat(conRuc(ruc).ruc()).isEqualTo(ruc);
    }

    @ParameterizedTest
    @ValueSource(strings = {"30123456789", "2012345678", "201234567890", "2012345678A", "20 12345678",
            "２０１２３４５６７８９", "11123456789"})
    void rucInvalidoSeRechaza(String ruc) {
        assertThatThrownBy(() -> conRuc(ruc)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void telefonoEntre6Y20CaracteresPermitidos() {
        assertThat(conTelefono("555012").telefono()).isEqualTo("555012");
        assertThat(conTelefono("1".repeat(20)).telefono()).hasSize(20);
        assertThatThrownBy(() -> conTelefono("55501")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTelefono("1".repeat(21))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTelefono("555-ABCD")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTelefono("555 0123 ext.4")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tasaIgvEntreCeroYTreintaPorCientoInclusive() {
        assertThat(conTasa("0").tasaIgv()).isEqualByComparingTo("0");
        assertThat(conTasa("0.30").tasaIgv()).isEqualByComparingTo("0.30");
        assertThat(conTasa("0.3000").tasaIgv()).isEqualByComparingTo("0.30");
        assertThatThrownBy(() -> conTasa("0.3001")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conTasa("-0.0001")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void umbralDeStockEntreCeroYMil() {
        assertThat(conUmbral(0).umbralStockBajo()).isZero();
        assertThat(conUmbral(1000).umbralStockBajo()).isEqualTo(1000);
        assertThatThrownBy(() -> conUmbral(1001)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> conUmbral(Integer.MIN_VALUE)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void monedaLocaleRedondeoYTasaNulosSeRechazan() {
        Currency pen = BASE.moneda();
        Locale locale = BASE.locale();

        assertThatThrownBy(() -> new ParametrosTienda("T", "20999999991", "Lima", "999888777", null, IGV,
                RoundingMode.HALF_UP, locale, 5)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ParametrosTienda("T", "20999999991", "Lima", "999888777", pen, IGV,
                RoundingMode.HALF_UP, null, 5)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ParametrosTienda("T", "20999999991", "Lima", "999888777", pen, IGV,
                null, locale, 5)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ParametrosTienda("T", "20999999991", "Lima", "999888777", pen, null,
                RoundingMode.HALF_UP, locale, 5)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void redondeoUnnecessaryNoSePermite() {
        assertThatThrownBy(() -> new ParametrosTienda("T", "20999999991", "Lima", "999888777", BASE.moneda(), IGV,
                RoundingMode.UNNECESSARY, BASE.locale(), 5))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("redondeo");
    }

    @Test
    void conDatosEditablesConservaMonedaLocaleYRedondeoYNoModificaElOriginal() {
        ParametrosTienda usd = new ParametrosTienda("T", "20999999991", "Lima", "999888777",
                Currency.getInstance("USD"), IGV, RoundingMode.HALF_EVEN, Locale.US, 5);

        ParametrosTienda copia = usd.conDatosEditables("Otra", "10123456789", "Cusco", "984111222",
                new BigDecimal("0.10"), 9);

        assertThat(copia.moneda()).isEqualTo(Currency.getInstance("USD"));
        assertThat(copia.redondeo()).isEqualTo(RoundingMode.HALF_EVEN);
        assertThat(copia.locale()).isEqualTo(Locale.US);
        assertThat(copia).isNotSameAs(usd);
        assertThat(usd.razonSocial()).isEqualTo("T");
    }
}
