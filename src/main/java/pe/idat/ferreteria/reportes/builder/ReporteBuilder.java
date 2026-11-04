package pe.idat.ferreteria.reportes.builder;

import pe.idat.ferreteria.reportes.composite.Grafico;
import pe.idat.ferreteria.reportes.composite.Parrafo;
import pe.idat.ferreteria.reportes.composite.Seccion;
import pe.idat.ferreteria.reportes.composite.Tabla;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.ReporteBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Patrón Builder
public class ReporteBuilder {
    private final String titulo;
    private String subtitulo = "";
    private final List<Seccion> secciones = new ArrayList<>();
    private Seccion seccionActual;
    // donde se agregan los párrafos, tablas y gráficos: la sección o su subsección
    private Seccion destino;

    public ReporteBuilder(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El reporte necesita un título");
        }
        this.titulo = titulo;
    }

    public ReporteBuilder subtitulo(String texto) {
        this.subtitulo = texto;
        return this;
    }

    public ReporteBuilder seccion(String tituloSeccion) {
        seccionActual = new Seccion(tituloSeccion);
        secciones.add(seccionActual);
        destino = seccionActual;
        return this;
    }

    // una sección dentro de la sección actual
    public ReporteBuilder subseccion(String tituloSubseccion) {
        Seccion subseccion = new Seccion(tituloSubseccion);
        getSeccionActual().agregar(subseccion);
        destino = subseccion;
        return this;
    }

    public ReporteBuilder parrafo(String texto) {
        getDestino().agregar(new Parrafo(texto));
        return this;
    }

    public ReporteBuilder tabla(List<String> encabezados, List<List<String>> filas) {
        getDestino().agregar(new Tabla(encabezados, filas));
        return this;
    }

    public ReporteBuilder grafico(String tituloGrafico, Map<String, BigDecimal> valores) {
        getDestino().agregar(new Grafico(tituloGrafico, valores));
        return this;
    }

    public Reporte build() {
        return new ReporteBase(titulo, subtitulo, secciones);
    }

    private Seccion getSeccionActual() {
        if (seccionActual == null) {
            throw new IllegalStateException("Primero hay que abrir una sección con seccion()");
        }
        return seccionActual;
    }

    private Seccion getDestino() {
        getSeccionActual();
        return destino;
    }
}
