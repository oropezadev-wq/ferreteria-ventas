package pe.idat.ferreteria.configuracion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Estado inmutable de {@link ConfiguracionGlobal}. Se valida al construirse: una instancia existente
 * siempre es válida, así que reemplazarla completa es seguro entre hilos.
 */
public record ParametrosTienda(String razonSocial, String ruc, String direccion, String telefono,
                               Currency moneda, BigDecimal tasaIgv, RoundingMode redondeo, Locale locale,
                               int umbralStockBajo) {

    public static final int LARGO_MAXIMO_RAZON_SOCIAL = 100;
    public static final int LARGO_MAXIMO_DIRECCION = 150;
    public static final BigDecimal TASA_IGV_MAXIMA = new BigDecimal("0.30");
    public static final int UMBRAL_STOCK_MAXIMO = 1000;

    /** 11 dígitos con un prefijo de contribuyente válido (10, 15, 17 o 20). No se verifica el dígito de control. */
    private static final Pattern RUC = Pattern.compile("^(10|15|17|20)\\d{9}$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9 +()-]{6,20}$");

    public ParametrosTienda {
        razonSocial = textoObligatorio(razonSocial, "razón social", LARGO_MAXIMO_RAZON_SOCIAL);
        direccion = textoObligatorio(direccion, "dirección", LARGO_MAXIMO_DIRECCION);
        ruc = textoObligatorio(ruc, "RUC", 11);
        if (!RUC.matcher(ruc).matches()) {
            throw new IllegalArgumentException("El RUC debe tener 11 dígitos y empezar con 10, 15, 17 o 20");
        }
        telefono = textoObligatorio(telefono, "teléfono", 20);
        if (!TELEFONO.matcher(telefono).matches()) {
            throw new IllegalArgumentException("El teléfono solo admite dígitos, espacios, +, - y paréntesis");
        }
        Objects.requireNonNull(moneda, "La moneda es obligatoria");
        Objects.requireNonNull(locale, "El locale es obligatorio");
        Objects.requireNonNull(redondeo, "El modo de redondeo es obligatorio");
        if (redondeo == RoundingMode.UNNECESSARY) {
            throw new IllegalArgumentException("El modo de redondeo debe permitir redondear");
        }
        Objects.requireNonNull(tasaIgv, "La tasa de IGV es obligatoria");
        if (tasaIgv.signum() < 0 || tasaIgv.compareTo(TASA_IGV_MAXIMA) > 0) {
            throw new IllegalArgumentException("La tasa de IGV debe estar entre 0 y " + TASA_IGV_MAXIMA);
        }
        if (umbralStockBajo < 0 || umbralStockBajo > UMBRAL_STOCK_MAXIMO) {
            throw new IllegalArgumentException("El umbral de stock bajo debe estar entre 0 y " + UMBRAL_STOCK_MAXIMO);
        }
    }

    /**
     * Valores iniciales de la tienda. Todos los datos son ficticios: el RUC 20999999991 no corresponde
     * a ningún contribuyente y se muestra marcado como ficticio en las pantallas.
     */
    public static ParametrosTienda porDefecto() {
        return new ParametrosTienda(
                "Ferretería El Constructor S.A.C.",
                "20999999991",
                "Av. Los Constructores 123, Lima",
                "(01) 555-0123",
                Currency.getInstance("PEN"),
                new BigDecimal("0.18"),
                RoundingMode.HALF_UP,
                Locale.forLanguageTag("es-PE"),
                5);
    }

    /** Copia con los datos editables desde /configuracion; moneda, redondeo y locale se conservan. */
    public ParametrosTienda conDatosEditables(String razonSocial, String ruc, String direccion, String telefono,
                                              BigDecimal tasaIgv, int umbralStockBajo) {
        return new ParametrosTienda(razonSocial, ruc, direccion, telefono, moneda, tasaIgv, redondeo, locale,
                umbralStockBajo);
    }

    private static String textoObligatorio(String valor, String campo, int largoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Campo obligatorio: " + campo);
        }
        String limpio = valor.strip();
        if (limpio.length() > largoMaximo) {
            throw new IllegalArgumentException("El campo " + campo + " admite hasta " + largoMaximo + " caracteres");
        }
        return limpio;
    }
}
