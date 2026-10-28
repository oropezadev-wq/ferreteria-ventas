package pe.idat.ferreteria.controlador;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.idat.ferreteria.reportes.fachada.ReporteFacade;
import pe.idat.ferreteria.reportes.modelo.Reporte;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteFacade facade;

    public ReporteController(ReporteFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public String formulario(Model modelo) {
        return mostrar(modelo, TipoReporte.VENTAS_MENSUAL, LocalDate.now(), true, "", true);
    }

    @PostMapping
    public String generar(@RequestParam TipoReporte tipo,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                          @RequestParam(defaultValue = "false") boolean conSello,
                          @RequestParam(defaultValue = "") String marcaAgua,
                          @RequestParam(defaultValue = "false") boolean conFirma,
                          Model modelo) {
        try {
            // Una sola llamada: la fachada se encarga del resto.
            Reporte reporte = facade.generarReporte(tipo, fecha, conSello, marcaAgua, conFirma);
            modelo.addAttribute("reporte", reporte);
        } catch (IllegalArgumentException e) {
            modelo.addAttribute("error", e.getMessage());
        }
        return mostrar(modelo, tipo, fecha, conSello, marcaAgua, conFirma);
    }

    private String mostrar(Model modelo, TipoReporte tipo, LocalDate fecha, boolean conSello, String marcaAgua,
                           boolean conFirma) {
        modelo.addAttribute("tipos", TipoReporte.values());
        modelo.addAttribute("tipo", tipo);
        modelo.addAttribute("fecha", fecha);
        modelo.addAttribute("conSello", conSello);
        modelo.addAttribute("marcaAgua", marcaAgua);
        modelo.addAttribute("conFirma", conFirma);
        return "reportes/index";
    }
}
