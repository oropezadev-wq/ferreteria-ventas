package pe.idat.ferreteria.configuracion;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.idat.ferreteria.aplicacion.ProductoService;
import pe.idat.ferreteria.aplicacion.VentaService;

/**
 * Carga datos de ejemplo al arrancar, para que la aplicación y los reportes muestren algo desde el inicio.
 * Los datos están en memoria: se vuelven a cargar cada vez que la aplicación arranca.
 * <p>
 * OJO: si uno de estos métodos lanza una excepción, la aplicación no arranca. Mientras no estén listos,
 * deben quedar vacíos.
 */
@Component
public class DatosIniciales implements ApplicationRunner {

    private final ProductoService productos;
    private final VentaService ventas;

    public DatosIniciales(ProductoService productos, VentaService ventas) {
        this.productos = productos;
        this.ventas = ventas;
    }

    @Override
    public void run(ApplicationArguments args) {
        cargarProductos();
        cargarVentas();
    }

    private void cargarProductos() {
        // TODO (issue Productos): unos 15 productos reales de ferretería, de todas las categorías, y varios con
        // stock bajo el mínimo para que se vea la alerta. Ejemplo:
        //   productos.crear("HER-0001", "Martillo de uña 16 oz", CategoriaProducto.HERRAMIENTAS,
        //           new BigDecimal("39.90"), 25, 5);
        //   productos.crear("CON-0001", "Cemento Sol Tipo I 42.5 kg", CategoriaProducto.CONSTRUCCION,
        //           new BigDecimal("32.50"), 4, 10);   // bajo el mínimo
    }

    private void cargarVentas() {
        // TODO (issue Datos para reportes): unas 30 ventas de ejemplo para que los reportes tengan datos.
        // Se registran con ventas.registrar(List.of(new ItemVenta(productoId, cantidad)), new PagoEfectivo(...)),
        // así que primero deben existir los productos y el stock debe alcanzar.
        // (Por ahora todas quedan con la fecha de hoy; mostrar varias fechas es una mejora opcional.)
    }
}
