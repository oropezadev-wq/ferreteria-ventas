package pe.idat.ferreteria.reportes.prototipo;

import org.springframework.stereotype.Component;
import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegistroPlantillas {
    private final Map<TipoReporte, PlantillaReporte> plantillas = new HashMap<>();

    public RegistroPlantillas() {
        PlantillaReporte base = new PlantillaReporte("Reporte de ventas", Granularidad.DIA)
                .agregarSeccion(SeccionPlantilla.RESUMEN);

        plantillas.put(TipoReporte.VENTAS_DIARIO, base.copiar().renombrar("Ventas del día")
                .agregarSeccion(SeccionPlantilla.DETALLE_VENTAS));
        plantillas.put(TipoReporte.VENTAS_MENSUAL, base.copiar().renombrar("Reporte mensual de ingresos")
                .conGranularidad(Granularidad.MES)
                .agregarSeccion(SeccionPlantilla.INGRESOS)
                .agregarSeccion(SeccionPlantilla.DETALLE_VENTAS)
                .agregarSeccion(SeccionPlantilla.CONCLUSIONES));
        plantillas.put(TipoReporte.VENTAS_TRIMESTRAL, base.copiar().renombrar("Reporte trimestral de ingresos y gastos")
                .conGranularidad(Granularidad.TRIMESTRE)
                .agregarSeccion(SeccionPlantilla.INGRESOS)
                .agregarSeccion(SeccionPlantilla.GASTOS)
                .agregarSeccion(SeccionPlantilla.CONCLUSIONES));
        plantillas.put(TipoReporte.VENTAS_ANUAL, base.copiar().renombrar("Reporte anual consolidado")
                .conGranularidad(Granularidad.ANIO)
                .agregarSeccion(SeccionPlantilla.INGRESOS)
                .agregarSeccion(SeccionPlantilla.GASTOS)
                .agregarSeccion(SeccionPlantilla.CONCLUSIONES));
        plantillas.put(TipoReporte.INVENTARIO_VALORIZADO,
                new PlantillaReporte("Inventario valorizado", Granularidad.NINGUNA)
                        .agregarSeccion(SeccionPlantilla.INVENTARIO));
    }

    public PlantillaReporte obtener(TipoReporte tipo) {
        PlantillaReporte plantilla = plantillas.get(tipo);
        if (plantilla == null) {
            throw new IllegalArgumentException("No hay plantilla para el reporte " + tipo);
        }
        return plantilla.copiar();
    }
}
