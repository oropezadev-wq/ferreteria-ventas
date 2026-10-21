package pe.idat.ferreteria.reportes.fachada;

import pe.idat.ferreteria.reportes.modelo.NivelDetalle;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Entrada única de ReporteFacade (Integrante 1, etapa 2). Se valida al construirse, así el Facade
 * siempre recibe una solicitud coherente.
 * <ul>
 *   <li>{@code extras} se copia a un conjunto inmodificable que se recorre en el orden de {@link ExtraReporte}.</li>
 *   <li>Con MARCA_AGUA el texto es obligatorio (hasta {@value #LARGO_MAXIMO_MARCA_AGUA} caracteres);
 *       sin MARCA_AGUA el texto se descarta y queda en {@code null}.</li>
 * </ul>
 */
public record SolicitudReporte(TipoReporte tipo, LocalDate fechaReferencia, NivelDetalle detalle,
                               Set<ExtraReporte> extras, String textoMarcaAgua) {

    public static final int LARGO_MAXIMO_MARCA_AGUA = 30;

    public SolicitudReporte {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de reporte es obligatorio");
        }
        if (fechaReferencia == null) {
            throw new IllegalArgumentException("La fecha de referencia es obligatoria");
        }
        if (detalle == null) {
            throw new IllegalArgumentException("El nivel de detalle es obligatorio");
        }
        extras = copiarExtras(extras);
        if (extras.contains(ExtraReporte.MARCA_AGUA)) {
            if (textoMarcaAgua == null || textoMarcaAgua.isBlank()) {
                throw new IllegalArgumentException("La marca de agua necesita un texto");
            }
            textoMarcaAgua = textoMarcaAgua.strip();
            if (textoMarcaAgua.length() > LARGO_MAXIMO_MARCA_AGUA) {
                throw new IllegalArgumentException(
                        "El texto de la marca de agua admite hasta " + LARGO_MAXIMO_MARCA_AGUA + " caracteres");
            }
        } else {
            textoMarcaAgua = null;
        }
    }

    private static Set<ExtraReporte> copiarExtras(Collection<ExtraReporte> extras) {
        EnumSet<ExtraReporte> copia = EnumSet.noneOf(ExtraReporte.class);
        if (extras != null) {
            for (ExtraReporte extra : extras) {
                if (extra == null) {
                    throw new IllegalArgumentException("Los extras no pueden contener valores nulos");
                }
                copia.add(extra);
            }
        }
        return Collections.unmodifiableSet(copia);
    }

    public boolean tieneExtra(ExtraReporte extra) {
        return extras.contains(extra);
    }
}
