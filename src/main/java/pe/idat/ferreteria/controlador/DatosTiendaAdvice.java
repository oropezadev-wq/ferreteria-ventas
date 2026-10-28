package pe.idat.ferreteria.controlador;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pe.idat.ferreteria.configuracion.ConfiguracionGlobal;

// Pone los datos de la tienda (el Singleton) en todas las pantallas, para el menú y el pie de página.
@ControllerAdvice
public class DatosTiendaAdvice {

    @ModelAttribute("tienda")
    public ConfiguracionGlobal tienda() {
        return ConfiguracionGlobal.getInstancia();
    }
}
