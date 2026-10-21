package pe.idat.ferreteria.reportes.builder;

import pe.idat.ferreteria.reportes.modelo.NivelDetalle;
import pe.idat.ferreteria.reportes.modelo.Periodo;
import pe.idat.ferreteria.reportes.modelo.TipoContenido;

/**
 * Director del patrón Builder: sabe qué secciones y elementos lleva un tipo de contenido y se los pide al builder.
 * El Facade recibe todos los ensambladores y usa el que soporta el contenido de la plantilla.
 * <p>
 * Lo implementan EnsambladorVentas y EnsambladorInventario (Integrante 3).
 */
public interface EnsambladorContenido {

    boolean soporta(TipoContenido contenido);

    void ensamblar(ReporteBuilder builder, Periodo periodo, NivelDetalle detalle);
}
