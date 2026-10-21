package pe.idat.ferreteria.dominio.comun;

import java.util.Currency;

/** Se opera o compara {@link Dinero} de monedas distintas. */
public class MonedaIncompatibleException extends ReglaNegocioException {

    public MonedaIncompatibleException(Currency esperada, Currency recibida) {
        super("No se pueden combinar montos en " + esperada.getCurrencyCode()
                + " y " + recibida.getCurrencyCode());
    }
}
