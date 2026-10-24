package pe.idat.ferreteria.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pe.idat.ferreteria.configuracion.DatosTienda;

/**
 * Agrega {@code tienda} al modelo de todas las pantallas, para que el layout (navbar y footer) muestre
 * los datos vigentes de la configuración sin que cada controlador los cargue.
 */
@ControllerAdvice(annotations = Controller.class)
public class ModeloGlobalAdvice {

    private final DatosTienda datosTienda;

    public ModeloGlobalAdvice(DatosTienda datosTienda) {
        this.datosTienda = datosTienda;
    }

    @ModelAttribute("tienda")
    public DatosTienda tienda() {
        return datosTienda;
    }
}
