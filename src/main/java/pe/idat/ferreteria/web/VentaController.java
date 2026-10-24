package pe.idat.ferreteria.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.idat.ferreteria.aplicacion.ProductoService;
import pe.idat.ferreteria.aplicacion.VentaService;
import pe.idat.ferreteria.configuracion.ParametrosFiscales;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;
import pe.idat.ferreteria.dominio.venta.Venta;
import pe.idat.ferreteria.web.formulario.VentaForm;

/** Pantallas de ventas: nueva venta (punto de venta), historial y ticket. */
@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventas;
    private final ProductoService productos;
    private final ParametrosFiscales fiscales;

    public VentaController(VentaService ventas, ProductoService productos, ParametrosFiscales fiscales) {
        this.ventas = ventas;
        this.productos = productos;
        this.fiscales = fiscales;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("ventas", ventas.listar());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model modelo) {
        return formulario(modelo, new VentaForm(), null);
    }

    @PostMapping
    public String registrar(@ModelAttribute("formulario") VentaForm form, Model modelo) {
        try {
            Venta venta = ventas.registrar(form.items(), form.pago());
            return "redirect:/ventas/" + venta.getId();
        } catch (ReglaNegocioException | IllegalArgumentException ex) {
            return formulario(modelo, form, ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model modelo) {
        Venta venta = ventas.buscar(id);
        modelo.addAttribute("venta", venta);
        modelo.addAttribute("opGravada", venta.getOpGravada(fiscales.getTasaIgv()));
        modelo.addAttribute("igv", venta.getIgv(fiscales.getTasaIgv()));
        return "ventas/detalle";
    }

    private String formulario(Model modelo, VentaForm form, String error) {
        modelo.addAttribute("formulario", form);
        modelo.addAttribute("productos", productos.listar());
        modelo.addAttribute("metodos", VentaForm.MetodoPago.values());
        modelo.addAttribute("error", error);
        return "ventas/nueva";
    }
}
