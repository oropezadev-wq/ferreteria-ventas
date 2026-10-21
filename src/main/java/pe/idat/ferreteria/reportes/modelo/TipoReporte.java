package pe.idat.ferreteria.reportes.modelo;

/** Reportes que ofrece la aplicación; cada uno tiene una plantilla registrada en RegistroPlantillas. */
public enum TipoReporte {

    VENTAS_DIARIO("Ventas del día"),
    VENTAS_MENSUAL("Ventas del mes"),
    VENTAS_ANUAL("Ventas del año"),
    INVENTARIO_VALORIZADO("Inventario valorizado");

    private final String descripcion;

    TipoReporte(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
