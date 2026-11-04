package pe.idat.ferreteria.reportes.modelo;

public enum TipoReporte {
    VENTAS_DIARIO("Ventas del día"),
    VENTAS_MENSUAL("Ingresos del mes"),
    VENTAS_TRIMESTRAL("Ingresos y gastos del trimestre"),
    VENTAS_ANUAL("Reporte anual consolidado"),
    INVENTARIO_VALORIZADO("Inventario valorizado");

    private final String descripcion;

    TipoReporte(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
