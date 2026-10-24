package pe.idat.ferreteria.reportes.fachada;

import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;

/**
 * Lo que se le pide al {@link ReporteFacade}: qué reporte, de qué fecha y con qué adornos.
 *
 * @param marcaAgua texto de la marca de agua; null o vacío = sin marca de agua
 */
public record SolicitudReporte(TipoReporte tipo, LocalDate fecha, boolean conSello, String marcaAgua,
                               boolean conFirma) {

    public static final int LARGO_MAXIMO_MARCA_AGUA = 30;

    public SolicitudReporte {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de reporte es obligatorio");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        marcaAgua = marcaAgua == null || marcaAgua.isBlank() ? null : marcaAgua.strip();
        if (marcaAgua != null && marcaAgua.length() > LARGO_MAXIMO_MARCA_AGUA) {
            throw new IllegalArgumentException(
                    "La marca de agua admite hasta " + LARGO_MAXIMO_MARCA_AGUA + " caracteres");
        }
    }

    public boolean conMarcaAgua() {
        return marcaAgua != null;
    }
}
