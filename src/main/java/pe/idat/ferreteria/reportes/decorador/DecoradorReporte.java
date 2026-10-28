package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;

// Patrón Decorator: envuelve un reporte y le agrega algo sin modificar su clase.
public abstract class DecoradorReporte implements Reporte {

    protected final Reporte reporte;

    protected DecoradorReporte(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El decorador necesita un reporte");
        }
        this.reporte = reporte;
    }

    @Override
    public String getTitulo() {
        return reporte.getTitulo();
    }
}
