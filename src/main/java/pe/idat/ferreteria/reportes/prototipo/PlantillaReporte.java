package pe.idat.ferreteria.reportes.prototipo;

import pe.idat.ferreteria.reportes.modelo.Granularidad;
import pe.idat.ferreteria.reportes.modelo.SeccionPlantilla;

import java.util.ArrayList;
import java.util.List;

public class PlantillaReporte implements Prototipo<PlantillaReporte> {
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

    private PlantillaReporte(PlantillaReporte original) {
        this.titulo = original.titulo;
        this.granularidad = original.granularidad;
        // lista nueva para que la copia no cambie el original
        this.secciones = new ArrayList<>(original.secciones);
    }

    @Override
    public PlantillaReporte copiar() {
        return new PlantillaReporte(this);
    }

    public PlantillaReporte renombrar(String nuevoTitulo) {
        if (nuevoTitulo == null || nuevoTitulo.isBlank()) {
            throw new IllegalArgumentException("El nuevo título no puede estar vacío");
        }
        this.titulo = nuevoTitulo;
        return this;
    }

    public PlantillaReporte conGranularidad(Granularidad nueva) {
        if (nueva == null) {
            throw new IllegalArgumentException("La granularidad no puede ser nula");
        }
        this.granularidad = nueva;
        return this;
    }

    public PlantillaReporte agregarSeccion(SeccionPlantilla seccion) {
        if (seccion == null) {
            throw new IllegalArgumentException("La sección no puede ser nula");
        }
        if (!secciones.contains(seccion)) {
            secciones.add(seccion);
        }
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public Granularidad getGranularidad() {
        return granularidad;
    }

    public List<SeccionPlantilla> getSecciones() {
        return new ArrayList<>(secciones);
    }
}
