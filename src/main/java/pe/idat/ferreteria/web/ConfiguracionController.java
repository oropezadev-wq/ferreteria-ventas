package pe.idat.ferreteria.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;
import pe.idat.ferreteria.web.formulario.ConfiguracionForm;

/**
 * Pantalla de datos de la tienda: demo del Singleton. Al guardar, el nuevo nombre se ve de inmediato
 * en el navbar y el footer, porque todos leen la misma instancia de {@link ConfiguracionGlobal}.
 * Es el único consumidor que depende de la clase concreta, porque es el único que la modifica.
 */
@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {

    private static final String VISTA = "configuracion";

    private final ConfiguracionGlobal configuracion;

    public ConfiguracionController(ConfiguracionGlobal configuracion) {
        this.configuracion = configuracion;
    }

    @GetMapping
    public String formulario(Model modelo) {
        modelo.addAttribute("formulario", ConfiguracionForm.desde(configuracion.getParametros()));
        agregarDatosFijos(modelo);
        return VISTA;
    }

    /** Patrón PRG: si los datos son válidos redirige con un mensaje flash; si no, vuelve a mostrar el formulario. */
    @PostMapping
    public String guardar(@Valid @ModelAttribute("formulario") ConfiguracionForm formulario, BindingResult errores,
                          Model modelo, RedirectAttributes redireccion) {
        if (!errores.hasErrors()) {
            try {
                configuracion.actualizar(formulario.aParametros(configuracion.getParametros()));
                redireccion.addFlashAttribute("mensaje", "Configuración guardada.");
                return "redirect:/configuracion";
            } catch (IllegalArgumentException ex) {
                errores.reject("configuracion.invalida", ex.getMessage());
            }
        }
        agregarDatosFijos(modelo);
        return VISTA;
    }

    /** Moneda, formato regional y redondeo se muestran pero no se editan. */
    private void agregarDatosFijos(Model modelo) {
        var parametros = configuracion.getParametros();
        modelo.addAttribute("moneda", parametros.moneda().getCurrencyCode());
        modelo.addAttribute("locale", parametros.locale().toLanguageTag());
        modelo.addAttribute("redondeo", parametros.redondeo().name());
    }
}
