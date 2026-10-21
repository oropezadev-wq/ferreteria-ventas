package pe.idat.ferreteria.reportes.prototipo;

import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Prototype: plantilla base de un reporte (título, período y qué secciones lleva).
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
        // TODO (issue Prototype):
        //  - copiar titulo y granularidad del original;
        //  - para secciones crear una LISTA NUEVA: this.secciones = new ArrayList<>(original.secciones);
        //    Si se usara la misma lista (this.secciones = original.secciones) la copia sería SUPERFICIAL:
        //    agregar una sección a la copia también la agregaría al original. Eso es lo que la rúbrica castiga.
        throw new UnsupportedOperationException("Por implementar: constructor copia de PlantillaReporte");
    }

    @Override
    public PlantillaReporte copiar() {
        // TODO (issue Prototype): return new PlantillaReporte(this);
        throw new UnsupportedOperationException("Por implementar: PlantillaReporte.copiar");
    }

    /** Cambia el título. Devuelve la propia plantilla para encadenar: {@code base.copiar().renombrar("...")}. */
    public PlantillaReporte renombrar(String nuevoTitulo) {
        // TODO (issue Prototype): validar que no sea null ni vacío (IllegalArgumentException), asignar y return this.
        throw new UnsupportedOperationException("Por implementar: PlantillaReporte.renombrar");
    }

    public PlantillaReporte conGranularidad(Granularidad nueva) {
        // TODO (issue Prototype): validar que no sea null, asignar y return this.
        throw new UnsupportedOperationException("Por implementar: PlantillaReporte.conGranularidad");
    }

    public PlantillaReporte agregarSeccion(SeccionPlantilla seccion) {
        // TODO (issue Prototype): validar que no sea null y que no esté repetida; agregarla a la lista; return this.
        throw new UnsupportedOperationException("Por implementar: PlantillaReporte.agregarSeccion");
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
