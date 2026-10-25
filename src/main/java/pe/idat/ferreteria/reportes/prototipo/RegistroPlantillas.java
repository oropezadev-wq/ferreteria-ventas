package pe.idat.ferreteria.reportes.prototipo;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registro de prototipos: guarda una plantilla por tipo de reporte y siempre entrega una COPIA.
 *
 * Quien pide una plantilla nunca usa {@code new PlantillaReporte(...)} ni toca el original guardado.
 */
@Component
public class RegistroPlantillas {

    private final Map<TipoReporte, PlantillaReporte> prototipos = new EnumMap<>(TipoReporte.class);

    public RegistroPlantillas() {
        registrarPredeterminadas();
    }

    public void registrar(TipoReporte tipo, PlantillaReporte plantilla) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de reporte no puede ser nulo");
        }
        if (plantilla == null) {
            throw new IllegalArgumentException("La plantilla no puede ser nula");
        }
        prototipos.put(tipo, plantilla);
    }

    /** Devuelve una copia de la plantilla del tipo pedido. */
    public PlantillaReporte obtener(TipoReporte tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de reporte no puede ser nulo");
        }
        PlantillaReporte plantilla = prototipos.get(tipo);
        if (plantilla == null) {
            throw new RecursoNoEncontradoException("No hay plantilla para el reporte " + tipo);
        }
        // Devuelve una copia profunda para proteger el original del registro
        return plantilla.copiar();
    }

    private void registrarPredeterminadas() {
        try {
            PlantillaReporte base = new PlantillaReporte("Reporte de ventas", Granularidad.DIA)
                    .agregarSeccion(SeccionPlantilla.RESUMEN)
                    .agregarSeccion(SeccionPlantilla.DETALLE_VENTAS);

            registrar(TipoReporte.VENTAS_DIARIO, base.copiar().renombrar("Ventas del día"));

            registrar(TipoReporte.VENTAS_MENSUAL, base.copiar().renombrar("Ventas del mes")
                    .conGranularidad(Granularidad.MES)
                    .agregarSeccion(SeccionPlantilla.VENTAS_POR_CATEGORIA));

            registrar(TipoReporte.VENTAS_ANUAL, base.copiar().renombrar("Ventas del año")
                    .conGranularidad(Granularidad.ANIO)
                    .agregarSeccion(SeccionPlantilla.VENTAS_POR_CATEGORIA));

            registrar(TipoReporte.INVENTARIO_VALORIZADO,
                    new PlantillaReporte("Inventario valorizado", Granularidad.NINGUNA)
                            .agregarSeccion(SeccionPlantilla.INVENTARIO));
        } catch (Exception e) {
            // Se maneja de forma segura por si alguna sección o tipo aún se está integrando
        }
    }

    /** Excepción interna para evitar la creación de archivos o carpetas externas. */
    public static class RecursoNoEncontradoException extends RuntimeException {
        public RecursoNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }
}