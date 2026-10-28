package pe.idat.ferreteria.controlador;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;

@ControllerAdvice
public class DatosTiendaAdvice {
    @ModelAttribute("tienda")
    public ConfiguracionGlobal tienda() {
        return ConfiguracionGlobal.getInstancia();
    }
}
