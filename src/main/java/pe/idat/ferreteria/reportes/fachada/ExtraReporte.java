package pe.idat.ferreteria.reportes.fachada;

/**
 * Decoradores opcionales de un reporte. El orden de las constantes es el orden en que se aplican:
 * la firma va al final para que su huella cubra el sello y la marca de agua.
 */
public enum ExtraReporte {

    SELLO("Sello de la ferretería"),
    MARCA_AGUA("Marca de agua"),
    FIRMA("Huella SHA-256 (firma simulada)");

    private final String descripcion;

    ExtraReporte(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
