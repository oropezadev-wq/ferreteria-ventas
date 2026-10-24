package pe.idat.ferreteria.reportes.modelo;

/** Tamaño del período de un reporte y de los tramos de sus series. NINGUNA: el reporte no depende de fechas. */
public enum Granularidad {
    DIA,
    MES,
    ANIO,
    NINGUNA
}
