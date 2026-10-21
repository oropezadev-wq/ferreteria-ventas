package pe.idat.ferreteria.reportes.modelo;

/** RESUMEN arma indicadores y gráficos; DETALLADO agrega además las tablas. */
public enum NivelDetalle {

    RESUMEN("Resumen"),
    DETALLADO("Detallado");

    private final String descripcion;

    NivelDetalle(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
