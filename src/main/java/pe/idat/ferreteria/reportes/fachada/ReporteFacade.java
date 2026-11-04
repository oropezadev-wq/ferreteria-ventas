package pe.idat.ferreteria.reportes.fachada;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.reportes.builder.DirectorReporte;
import pe.idat.ferreteria.reportes.decorador.FirmaDigitalDecorador;
import pe.idat.ferreteria.reportes.decorador.MarcaAguaDecorador;
import pe.idat.ferreteria.reportes.decorador.SelloFerreteriaDecorador;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;
import pe.idat.ferreteria.reportes.prototipo.RegistroPlantillas;

import java.time.LocalDate;

// Patrón Facade
@Service
public class ReporteFacade {
    private final RegistroPlantillas plantillas;
    private final DirectorReporte director;

    public ReporteFacade(RegistroPlantillas plantillas, DirectorReporte director) {
        this.plantillas = plantillas;
        this.director = director;
    }

    public Reporte generarReporte(TipoReporte tipo, LocalDate fecha, boolean conSello, String marcaAgua,
                                  boolean conFirma) {
        if (tipo == null || fecha == null) {
            throw new IllegalArgumentException("Elija el tipo de reporte y la fecha");
        }

        // prototype
        PlantillaReporte plantilla = plantillas.obtener(tipo);

        // builder y composite
        Reporte reporte = director.construir(plantilla, fecha);

        // decorator
        if (conSello) {
            reporte = new SelloFerreteriaDecorador(reporte);
        }
        if (marcaAgua != null && !marcaAgua.isBlank()) {
            reporte = new MarcaAguaDecorador(reporte, marcaAgua.trim());
        }
        if (conFirma) {
            reporte = new FirmaDigitalDecorador(reporte);
        }
        return reporte;
    }
}
