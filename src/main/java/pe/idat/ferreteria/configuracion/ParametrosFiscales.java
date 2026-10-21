package pe.idat.ferreteria.configuracion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/** Parámetros para calcular montos: moneda de la tienda, tasa de IGV y redondeo. Lo usa VentaServiceImpl. */
public interface ParametrosFiscales {

    Currency getMoneda();

    /** Tasa como fracción: 0.18 equivale a 18 %. */
    BigDecimal getTasaIgv();

    RoundingMode getRedondeo();
}
