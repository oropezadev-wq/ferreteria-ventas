package pe.idat.ferreteria.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Clock;
import java.time.LocalDate;

/** Página de inicio. El dashboard con ventas del día y alertas de stock se agrega cuando existan los servicios. */
@Controller
public class InicioController {

    private final Clock reloj;

    public InicioController(Clock reloj) {
        this.reloj = reloj;
    }

    @GetMapping("/")
    public String inicio(Model modelo) {
        modelo.addAttribute("hoy", LocalDate.now(reloj));
        return "inicio";
    }
}
