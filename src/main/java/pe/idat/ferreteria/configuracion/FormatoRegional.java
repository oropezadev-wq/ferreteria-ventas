package pe.idat.ferreteria.configuracion;

import pe.idat.ferreteria.dominio.comun.Dinero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

/** Cómo se muestran montos y fechas en pantallas, tickets y reportes. */
public interface FormatoRegional {

    /** Por ejemplo "S/ 1,234.50" en es-PE. */
    String formatearDinero(Dinero dinero);

    /** dd/MM/yyyy */
    String formatearFecha(LocalDate fecha);

    /** dd/MM/yyyy HH:mm */
    String formatearFechaHora(LocalDateTime fechaHora);

    Locale getLocale();
}
