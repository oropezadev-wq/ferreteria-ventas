package pe.idat.ferreteria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio(Model modelo) {
        modelo.addAttribute("hoy", LocalDate.now());
        return "inicio";
    }
}
