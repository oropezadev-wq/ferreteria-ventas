package pe.idat.ferreteria.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;
import pe.idat.ferreteria.reportes.fachada.ReporteFacade;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;
import pe.idat.ferreteria.web.formulario.ReporteForm;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Pantalla de reportes. Solo conoce al {@link ReporteFacade}: no sabe nada de plantillas, builders ni
 * decoradores (esa es la ventaja del patrón Facade).
 */
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteFacade facade;
    private final Clock reloj;

    public ReporteController(ReporteFacade facade, Clock reloj) {
        this.facade = facade;
        this.reloj = reloj;
    }

    @GetMapping
    public String formulario(Model modelo) {
        ReporteForm form = new ReporteForm();
        form.setFecha(LocalDate.now(reloj));
        return vista(modelo, form);
    }

    @PostMapping
    public String generar(@Valid @ModelAttribute("formulario") ReporteForm form, BindingResult errores, Model modelo) {
        if (!errores.hasErrors()) {
            try {
                modelo.addAttribute("reporte", facade.generarReporte(form.aSolicitud()));
            } catch (ReglaNegocioException | IllegalArgumentException ex) {
                errores.reject("reporte.invalido", ex.getMessage());
            }
        }
        return vista(modelo, form);
    }

    private String vista(Model modelo, ReporteForm form) {
        modelo.addAttribute("formulario", form);
        modelo.addAttribute("tipos", TipoReporte.values());
        return "reportes/index";
    }
}
