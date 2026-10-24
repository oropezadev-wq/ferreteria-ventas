package pe.idat.ferreteria.reportes.fachada;

import org.springframework.stereotype.Service;
import pe.idat.ferreteria.configuracion.DatosTienda;
import pe.idat.ferreteria.reportes.builder.DirectorReporte;
import pe.idat.ferreteria.reportes.prototipo.RegistroPlantillas;

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
        // TODO (issue Facade):
        //  1. Prototype:  PlantillaReporte plantilla = plantillas.obtener(solicitud.tipo());
        //  2. Builder + Composite:  Reporte reporte = director.construir(plantilla, solicitud.fecha());
        //  3. Decorator, en este orden (la firma al final para que cubra todo):
        //       if (solicitud.conSello())     reporte = new SelloFerreteriaDecorador(reporte, tienda);
        //       if (solicitud.conMarcaAgua()) reporte = new MarcaAguaDecorador(reporte, solicitud.marcaAgua());
        //       String huella = null;
        //       if (solicitud.conFirma()) { FirmaDigitalDecorador firma = new FirmaDigitalDecorador(reporte);
        //                                   huella = firma.getHuella(); reporte = firma; }
        //  4. return new ReporteGenerado(reporte.getTitulo(), reporte.getHtml(), huella);
        // El Singleton (ConfiguracionGlobal) llega como DatosTienda y como FormatoRegional en el director.
        throw new UnsupportedOperationException("Por implementar: ReporteFacade.generarReporte");
    }
}
