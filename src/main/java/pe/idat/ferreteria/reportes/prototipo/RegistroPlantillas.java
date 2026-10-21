package pe.idat.ferreteria.reportes.prototipo;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registro de prototipos: guarda una plantilla por tipo de reporte y siempre entrega una COPIA.
 * Quien pide una plantilla nunca usa {@code new PlantillaReporte(...)} ni toca el original guardado.
 */
@Component
public class RegistroPlantillas {

    private final Map<TipoReporte, PlantillaReporte> prototipos = new EnumMap<>(TipoReporte.class);

    public RegistroPlantillas() {
        registrarPredeterminadas();
    }

    public void registrar(TipoReporte tipo, PlantillaReporte plantilla) {
        // TODO (issue Prototype): validar que tipo y plantilla no sean null y guardarla en el mapa.
        throw new UnsupportedOperationException("Por implementar: RegistroPlantillas.registrar");
    }

    /** Devuelve una copia de la plantilla del tipo pedido. */
    public PlantillaReporte obtener(TipoReporte tipo) {
        // TODO (issue Prototype):
        //  1. Buscar en el mapa. Si no hay, lanzar
        //     new RecursoNoEncontradoException("No hay plantilla para el reporte " + tipo)
        //  2. return plantilla.copiar();   <- nunca el original
        throw new UnsupportedOperationException("Por implementar: RegistroPlantillas.obtener");
    }

    private void registrarPredeterminadas() {
        // TODO (issue Prototype): crear UNA plantilla base y obtener las demás clonándola. Ejemplo:
        //
        //   PlantillaReporte base = new PlantillaReporte("Reporte de ventas", Granularidad.DIA)
        //           .agregarSeccion(SeccionPlantilla.RESUMEN)
        //           .agregarSeccion(SeccionPlantilla.DETALLE_VENTAS);
        //   registrar(TipoReporte.VENTAS_DIARIO, base.copiar().renombrar("Ventas del día"));
        //   registrar(TipoReporte.VENTAS_MENSUAL, base.copiar().renombrar("Ventas del mes")
        //           .conGranularidad(Granularidad.MES)
        //           .agregarSeccion(SeccionPlantilla.VENTAS_POR_CATEGORIA));
        //   (el anual igual que el mensual, con Granularidad.ANIO)
        //   registrar(TipoReporte.INVENTARIO_VALORIZADO,
        //           new PlantillaReporte("Inventario valorizado", Granularidad.NINGUNA)
        //                   .agregarSeccion(SeccionPlantilla.INVENTARIO));
        //
        // OJO: este método se ejecuta al arrancar la aplicación. Mientras no esté listo, dejarlo vacío
        // (sin lanzar excepciones), o la aplicación no arranca.
    }
}
