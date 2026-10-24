package pe.idat.ferreteria.reportes.modelo;

/** Secciones que puede incluir una plantilla de reporte. La plantilla dice cuáles van; el Builder las arma. */
public enum SeccionPlantilla {
    RESUMEN("Resumen"),
    VENTAS_POR_CATEGORIA("Ventas por categoría"),
    DETALLE_VENTAS("Detalle de ventas"),
    INVENTARIO("Inventario");

    private final String titulo;

    SeccionPlantilla(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
