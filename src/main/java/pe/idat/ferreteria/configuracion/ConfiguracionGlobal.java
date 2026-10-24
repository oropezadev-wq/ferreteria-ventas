package pe.idat.ferreteria.configuracion;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Patrón Singleton: única configuración de la tienda en toda la aplicación.
 * <ul>
 *   <li>Holder idiom: la JVM crea la instancia la primera vez que se llama a {@link #getInstancia()},
 *       de forma perezosa y segura entre hilos, sin {@code synchronized}.</li>
 *   <li>Clase final y constructor privado con guarda: crear otra instancia por reflexión lanza
 *       {@link IllegalStateException}. No es Serializable, así que tampoco se duplica al deserializar.</li>
 *   <li>El estado es un {@link ParametrosTienda} inmutable dentro de un {@link AtomicReference}:
 *       {@link #actualizar} lo reemplaza de una vez y quien lee {@code getParametros()} nunca ve una mezcla de valores viejos y nuevos
 *       (cada getter individual lee la foto vigente en ese momento).</li>
 * </ul>
 * Spring la publica como bean (ConfiguracionBeans) y los consumidores la reciben por sus interfaces
 * {@link ParametrosFiscales}, {@link FormatoRegional} y {@link DatosTienda}. Ver docs/decisiones/ADR-002.
 */
public final class ConfiguracionGlobal implements ParametrosFiscales, FormatoRegional, DatosTienda {

    private static final DateTimeFormatter PATRON_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PATRON_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final AtomicReference<ParametrosTienda> parametros = new AtomicReference<>(ParametrosTienda.porDefecto());

    private ConfiguracionGlobal() {
        if (Holder.INSTANCIA != null) {
            throw new IllegalStateException("ConfiguracionGlobal es un Singleton: usar getInstancia()");
        }
    }

    private static final class Holder {
        private static final ConfiguracionGlobal INSTANCIA = new ConfiguracionGlobal();
    }

    public static ConfiguracionGlobal getInstancia() {
        return Holder.INSTANCIA;
    }

    /** Reemplaza todos los parámetros de forma atómica. {@link ParametrosTienda} ya llega validado. */
    public void actualizar(ParametrosTienda nuevos) {
        parametros.set(Objects.requireNonNull(nuevos, "Los parámetros son obligatorios"));
    }

    /** Foto de los parámetros vigentes; es inmutable, así que se puede leer sin bloqueos. */
    public ParametrosTienda getParametros() {
        return parametros.get();
    }

    // ParametrosFiscales

    @Override
    public Currency getMoneda() {
        return parametros.get().moneda();
    }

    @Override
    public BigDecimal getTasaIgv() {
        return parametros.get().tasaIgv();
    }

    @Override
    public RoundingMode getRedondeo() {
        return parametros.get().redondeo();
    }

    // FormatoRegional

    @Override
    public String formatearDinero(Dinero dinero) {
        Objects.requireNonNull(dinero, "El monto es obligatorio");
        ParametrosTienda actuales = parametros.get();
        NumberFormat numero = NumberFormat.getNumberInstance(actuales.locale());
        numero.setMinimumFractionDigits(Dinero.ESCALA);
        numero.setMaximumFractionDigits(Dinero.ESCALA);
        numero.setRoundingMode(actuales.redondeo());
        return dinero.moneda().getSymbol(actuales.locale()) + " " + numero.format(dinero.monto());
    }

    @Override
    public String formatearFecha(LocalDate fecha) {
        return PATRON_FECHA.withLocale(getLocale()).format(Objects.requireNonNull(fecha, "La fecha es obligatoria"));
    }

    @Override
    public String formatearFechaHora(LocalDateTime fechaHora) {
        return PATRON_FECHA_HORA.withLocale(getLocale())
                .format(Objects.requireNonNull(fechaHora, "La fecha y hora es obligatoria"));
    }

    @Override
    public Locale getLocale() {
        return parametros.get().locale();
    }

    // DatosTienda

    @Override
    public String getRazonSocial() {
        return parametros.get().razonSocial();
    }

    @Override
    public String getRuc() {
        return parametros.get().ruc();
    }

    @Override
    public String getDireccion() {
        return parametros.get().direccion();
    }

    @Override
    public String getTelefono() {
        return parametros.get().telefono();
    }

    @Override
    public int getUmbralStockBajo() {
        return parametros.get().umbralStockBajo();
    }
}
