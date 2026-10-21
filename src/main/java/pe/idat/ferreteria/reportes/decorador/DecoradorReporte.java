package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;

/**
 * Patrón Decorator: base de los adornos de un reporte (marca de agua, sello, firma).
 * <p>
 * Un decorador ES un {@link Reporte} y además ENVUELVE a otro. Así se pueden poner capas una sobre otra
 * ({@code new FirmaDigitalDecorador(new SelloFerreteriaDecorador(reporte, tienda))}) sin cambiar el reporte original.
 */
public abstract class DecoradorReporte implements Reporte {

    private final Reporte envuelto;

    protected DecoradorReporte(Reporte envuelto) {
        if (envuelto == null) {
            throw new IllegalArgumentException("El decorador necesita un reporte que envolver");
        }
        this.envuelto = envuelto;
    }

    protected Reporte getEnvuelto() {
        return envuelto;
    }

    /** El título no cambia: se toma del reporte envuelto. Cada decorador concreto define su {@code getHtml()}. */
    @Override
    public String getTitulo() {
        return envuelto.getTitulo();
    }
}
