package pe.idat.ferreteria.reportes.prototipo;

import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Prototype: plantilla base de un reporte (título, período y qué secciones lleva).
 *
 * <p>
 * En vez de crear cada plantilla desde cero, se arma una base y las demás se obtienen clonándola con
 * {@link #copiar()} y ajustando lo que cambia. La copia es PROFUNDA: modificar la copia nunca altera el original.
 */
public final class PlantillaReporte implements Prototipo<PlantillaReporte> {

    private String titulo;
    private Granularidad granularidad;
    private final List<SeccionPlantilla> secciones;

    public PlantillaReporte(String titulo, Granularidad granularidad) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("La plantilla necesita un título");
        }
        if (granularidad == null) {
            throw new IllegalArgumentException("La plantilla necesita una granularidad");
        }
        this.titulo = titulo;
        this.granularidad = granularidad;
        this.secciones = new ArrayList<>();
    }

    /** Constructor copia: es la base de la copia profunda. Solo lo usa {@link #copiar()}. */
    private PlantillaReporte(PlantillaReporte original) {
        this.titulo = original.titulo;
        this.granularidad = original.granularidad;
        // Copia profunda: se crea una lista nueva para que no compartan la misma referencia en memoria
        this.secciones = new ArrayList<>(original.secciones);
    }

    @Override
    public PlantillaReporte copiar() {
        return new PlantillaReporte(this);
    }

    /** Cambia el título. Devuelve la propia plantilla para encadenar: {@code base.copiar().renombrar("...")}. */
    public PlantillaReporte renombrar(String nuevoTitulo) {
        if (nuevoTitulo == null || nuevoTitulo.isBlank()) {
            throw new IllegalArgumentException("El nuevo título no puede ser nulo o vacío");
        }
        this.titulo = nuevoTitulo;
        return this;
    }

    public PlantillaReporte conGranularidad(Granularidad nueva) {
        if (nueva == null) {
            throw new IllegalArgumentException("La nueva granularidad no puede ser nula");
        }
        this.granularidad = nueva;
        return this;
    }

    public PlantillaReporte agregarSeccion(SeccionPlantilla seccion) {
        if (seccion == null) {
            throw new IllegalArgumentException("La sección no puede ser nula");
        }
        if (this.secciones.contains(seccion)) {
            throw new IllegalArgumentException("La sección ya se encuentra agregada en la plantilla");
        }
        this.secciones.add(seccion);
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public Granularidad getGranularidad() {
        return granularidad;
    }

    /** Copia de solo lectura de las secciones, en el orden en que se agregaron. */
    public List<SeccionPlantilla> getSecciones() {
        return List.copyOf(secciones);
    }
}