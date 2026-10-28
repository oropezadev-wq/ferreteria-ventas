package pe.idat.ferreteria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.idat.ferreteria.controlador.formulario.ProductoForm;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.servicio.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productos;

    public ProductoController(ProductoService productos) {
        this.productos = productos;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("productos", productos.listar());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        return mostrarFormulario(modelo, new ProductoForm(), null);
    }

    @PostMapping
    public String crear(@ModelAttribute("formulario") ProductoForm form, BindingResult errores, Model modelo,
                        RedirectAttributes redireccion) {
        if (errores.hasErrors()) {
            return mostrarFormulario(modelo, form, null);
        }
        try {
            productos.crear(form.getCodigo(), form.getNombre(), form.getCategoria(), form.getPrecio(),
                    form.getStock(), form.getStockMinimo());
            redireccion.addFlashAttribute("mensaje", "Producto registrado.");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            errores.reject("producto.invalido", e.getMessage());
            return mostrarFormulario(modelo, form, null);
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        return mostrarFormulario(modelo, ProductoForm.desde(productos.buscar(id)), id);
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("formulario") ProductoForm form,
                             BindingResult errores, Model modelo, RedirectAttributes redireccion) {
        if (errores.hasErrors()) {
            return mostrarFormulario(modelo, form, id);
        }
        try {
            productos.actualizar(id, form.getNombre(), form.getPrecio(), form.getStockMinimo());
            redireccion.addFlashAttribute("mensaje", "Producto actualizado.");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            errores.reject("producto.invalido", e.getMessage());
            return mostrarFormulario(modelo, form, id);
        }
    }

    @PostMapping("/{id}/reponer")
    public String reponer(@PathVariable Long id, @RequestParam(defaultValue = "0") int cantidad,
                          RedirectAttributes redireccion) {
        try {
            productos.reponerStock(id, cantidad);
            redireccion.addFlashAttribute("mensaje", "Stock repuesto.");
        } catch (IllegalArgumentException e) {
            redireccion.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    private String mostrarFormulario(Model modelo, ProductoForm form, Long id) {
        modelo.addAttribute("formulario", form);
        modelo.addAttribute("productoId", id);
        modelo.addAttribute("categorias", CategoriaProducto.values());
        return "productos/formulario";
    }
}
