package pe.idat.ferreteria.reportes.fachada;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.configuracion.DatosTienda;
import pe.idat.ferreteria.reportes.builder.DirectorReporte;
import pe.idat.ferreteria.reportes.prototipo.RegistroPlantillas;
import pe.idat.ferreteria.reportes.prototipo.PlantillaReporte;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.decorador.SelloFerreteriaDecorador;
import pe.idat.ferreteria.reportes.decorador.MarcaAguaDecorador;
import pe.idat.ferreteria.reportes.decorador.FirmaDigitalDecorador;

/**
 * Patrón Facade: un único método, {@link #generarReporte}, esconde todo el proceso. Quien lo usa
 * (la pantalla de reportes) no conoce plantillas, builders, secciones ni decoradores.
 */
@Service
public class ReporteFacade {

    private final RegistroPlantillas plantillas;
    private final DirectorReporte director;
    private final DatosTienda tienda;

    public ReporteFacade(RegistroPlantillas plantillas, DirectorReporte director, DatosTienda tienda) {
        this.plantillas = plantillas;
        this.director = director;
        this.tienda = tienda;
    }

    public ReporteGenerado generarReporte(SolicitudReporte solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud es obligatoria");
        }
        PlantillaReporte plantilla = plantillas.obtener(solicitud.tipo());
        Reporte reporte = director.construir(plantilla, solicitud.fecha());
        if (solicitud.conSello()) {
            reporte = new SelloFerreteriaDecorador(reporte, tienda);
        }
        if (solicitud.conMarcaAgua()) {
            reporte = new MarcaAguaDecorador(reporte, solicitud.marcaAgua());
        }
        String huella = null;
        if (solicitud.conFirma()) {
            FirmaDigitalDecorador firma = new FirmaDigitalDecorador(reporte);
            huella = firma.getHuella();
            reporte = firma;
        }
        return new ReporteGenerado(reporte.getTitulo(), reporte.getHtml(), huella);
    }
}
