package pe.idat.ferreteria.dominio.comun;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Valor monetario inmutable: monto con 2 decimales (redondeo HALF_UP) y su moneda.
 * <ul>
 *   <li>Nunca es negativo: un descuento o un vuelto mayor que el total es un error del llamador.</li>
 *   <li>Las operaciones entre monedas distintas lanzan {@link MonedaIncompatibleException}.</li>
 *   <li>{@code 10} y {@code 10.00} son iguales porque el monto se normaliza a escala 2 al crearse.</li>
 * </ul>
 */
public record Dinero(BigDecimal monto, Currency moneda) implements Comparable<Dinero> {

    public static final int ESCALA = 2;
    public static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

    public Dinero {
        Objects.requireNonNull(monto, "El monto es obligatorio");
        Objects.requireNonNull(moneda, "La moneda es obligatoria");
        monto = monto.setScale(ESCALA, REDONDEO);
        if (monto.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo: " + monto);
        }
    }

    public static Dinero de(BigDecimal monto, Currency moneda) {
        return new Dinero(monto, moneda);
    }

    /** Atajo para literales: {@code Dinero.de("39.90", PEN)}. */
    public static Dinero de(String monto, Currency moneda) {
        Objects.requireNonNull(monto, "El monto es obligatorio");
        return new Dinero(new BigDecimal(monto), moneda);
    }

    public static Dinero cero(Currency moneda) {
        return new Dinero(BigDecimal.ZERO, moneda);
    }

    /** Moneda de la ferretería. */
    public static final Currency SOLES = Currency.getInstance("PEN");

    /** Atajo para montos en soles: {@code Dinero.soles("39.90")}. */
    public static Dinero soles(String monto) {
        return de(monto, SOLES);
    }

    /** Atajo para montos en soles a partir de un BigDecimal (por ejemplo, el valor de un formulario). */
    public static Dinero soles(BigDecimal monto) {
        return de(monto, SOLES);
    }

    public Dinero sumar(Dinero otro) {
        validarMisma(otro);
        return new Dinero(monto.add(otro.monto), moneda);
    }

    /** Lanza {@link IllegalArgumentException} si el resultado sería negativo. */
    public Dinero restar(Dinero otro) {
        validarMisma(otro);
        return new Dinero(monto.subtract(otro.monto), moneda);
    }

    public Dinero multiplicar(int cantidad) {
        return multiplicar(BigDecimal.valueOf(cantidad));
    }

    /** El resultado se redondea a 2 decimales con HALF_UP. */
    public Dinero multiplicar(BigDecimal factor) {
        Objects.requireNonNull(factor, "El factor es obligatorio");
        return new Dinero(monto.multiply(factor), moneda);
    }

    public boolean esMayorOIgualQue(Dinero otro) {
        return compareTo(otro) >= 0;
    }

    public boolean esCero() {
        return monto.signum() == 0;
    }

    @Override
    public int compareTo(Dinero otro) {
        validarMisma(otro);
        return monto.compareTo(otro.monto);
    }

    private void validarMisma(Dinero otro) {
        Objects.requireNonNull(otro, "El otro monto es obligatorio");
        if (!moneda.equals(otro.moneda)) {
            throw new MonedaIncompatibleException(moneda, otro.moneda);
        }
    }

    @Override
    public String toString() {
        return moneda.getCurrencyCode() + " " + monto.toPlainString();
    }
}
