package pe.idat.ferreteria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.idat.ferreteria.controlador.formulario.VentaForm;
import pe.idat.ferreteria.modelo.Sede;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.servicio.ProductoService;
import pe.idat.ferreteria.servicio.VentaService;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    private final VentaService ventas;
    private final ProductoService productos;

    public VentaController(VentaService ventas, ProductoService productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("ventas", ventas.listar());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model modelo) {
        return mostrarFormulario(modelo, new VentaForm(), null);
    }

    @PostMapping
    public String registrar(@ModelAttribute("formulario") VentaForm form, BindingResult errores, Model modelo) {
        if (errores.hasErrors()) {
            return mostrarFormulario(modelo, form, "Revise las cantidades y montos ingresados.");
        }
        try {
            Venta venta = ventas.registrar(form.items(), form.pago(), form.getSede(), form.getCliente(), form.getVendedor());
            return "redirect:/ventas/" + venta.getId();
        } catch (IllegalArgumentException e) {
            return mostrarFormulario(modelo, form, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("venta", ventas.buscar(id));
        return "ventas/detalle";
    }

    private String mostrarFormulario(Model modelo, VentaForm form, String error) {
        modelo.addAttribute("formulario", form);
        modelo.addAttribute("productos", productos.listar());
        modelo.addAttribute("metodos", List.of("EFECTIVO", "TARJETA", "YAPE", "TRANSFERENCIA"));
        modelo.addAttribute("sedes", Sede.values());
        modelo.addAttribute("error", error);
        return "ventas/nueva";
    }
}
