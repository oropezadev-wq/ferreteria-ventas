package pe.idat.ferreteria.web;

import jakarta.validation.Valid;
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
import pe.idat.ferreteria.aplicacion.ProductoService;
import pe.idat.ferreteria.dominio.comun.ReglaNegocioException;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.web.formulario.ProductoForm;

/** Pantallas de inventario: listar, crear, editar y reponer stock. */
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
        return formulario(modelo, new ProductoForm(), null);
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("formulario") ProductoForm form, BindingResult errores, Model modelo,
                        RedirectAttributes redireccion) {
        if (!errores.hasErrors()) {
            try {
                productos.crear(form.getCodigo(), form.getNombre(), form.getCategoria(), form.getPrecio(),
                        form.getStock(), form.getStockMinimo());
                redireccion.addFlashAttribute("mensaje", "Producto registrado.");
                return "redirect:/productos";
            } catch (ReglaNegocioException | IllegalArgumentException ex) {
                errores.reject("producto.invalido", ex.getMessage());
            }
        }
        return formulario(modelo, form, null);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        return formulario(modelo, ProductoForm.desde(productos.buscar(id)), id);
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("formulario") ProductoForm form,
                             BindingResult errores, Model modelo, RedirectAttributes redireccion) {
        if (!errores.hasErrors()) {
            try {
                productos.actualizar(id, form.getNombre(), form.getPrecio(), form.getStockMinimo());
                redireccion.addFlashAttribute("mensaje", "Producto actualizado.");
                return "redirect:/productos";
            } catch (ReglaNegocioException | IllegalArgumentException ex) {
                errores.reject("producto.invalido", ex.getMessage());
            }
        }
        return formulario(modelo, form, id);
    }

    @PostMapping("/{id}/reponer")
    public String reponer(@PathVariable Long id, @RequestParam(defaultValue = "0") int cantidad,
                          RedirectAttributes redireccion) {
        try {
            productos.reponerStock(id, cantidad);
            redireccion.addFlashAttribute("mensaje", "Stock repuesto.");
        } catch (ReglaNegocioException | IllegalArgumentException ex) {
            redireccion.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/productos";
    }

    /** {@code id} null = producto nuevo; con id = edición (código, categoría y stock no se editan). */
    private String formulario(Model modelo, ProductoForm form, Long id) {
        modelo.addAttribute("formulario", form);
        modelo.addAttribute("productoId", id);
        modelo.addAttribute("categorias", CategoriaProducto.values());
        return "productos/formulario";
    }
}
