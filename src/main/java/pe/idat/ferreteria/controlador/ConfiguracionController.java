package pe.idat.ferreteria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;

import java.math.BigDecimal;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {
    @GetMapping
    public String formulario() {
        return "configuracion";
    }

    @PostMapping
    public String guardar(@RequestParam String razonSocial, @RequestParam String ruc, @RequestParam String direccion,
                          @RequestParam String telefono, @RequestParam(required = false) BigDecimal igv,
                          @RequestParam(required = false) BigDecimal tipoCambio,
                          Model modelo, RedirectAttributes redireccion) {
        try {
            ConfiguracionGlobal.getInstancia().actualizar(razonSocial, ruc, direccion, telefono, igv, tipoCambio);
            redireccion.addFlashAttribute("mensaje", "Configuración guardada.");
            return "redirect:/configuracion";
        } catch (IllegalArgumentException e) {
            modelo.addAttribute("error", e.getMessage());
            return "configuracion";
        }
    }
}
