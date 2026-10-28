package pe.idat.ferreteria.configuracion;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.idat.ferreteria.aplicacion.ProductoService;
import pe.idat.ferreteria.aplicacion.VentaService;
import pe.idat.ferreteria.aplicacion.ItemVenta;
import pe.idat.ferreteria.dominio.producto.CategoriaProducto;
import pe.idat.ferreteria.dominio.producto.Producto;
import pe.idat.ferreteria.dominio.venta.pago.PagoEfectivo;
import pe.idat.ferreteria.dominio.comun.Dinero;

import java.math.BigDecimal;
import java.util.List;

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
        crear("HER-0001", "Martillo de uña 16 oz", CategoriaProducto.HERRAMIENTAS, "39.90", 25, 5);
        crear("HER-0002", "Destornillador Phillips", CategoriaProducto.HERRAMIENTAS, "12.50", 30, 8);
        crear("HER-0003", "Alicate universal 8 pulgadas", CategoriaProducto.HERRAMIENTAS, "29.90", 6, 10);
        crear("CON-0001", "Cemento Sol Tipo I 42.5 kg", CategoriaProducto.CONSTRUCCION, "32.50", 4, 10);
        crear("CON-0002", "Ladrillo King Kong", CategoriaProducto.CONSTRUCCION, "1.20", 150, 30);
        crear("CON-0003", "Bolsa de arena fina", CategoriaProducto.CONSTRUCCION, "18.00", 20, 5);
        crear("ELE-0001", "Cable eléctrico 2.5 mm por metro", CategoriaProducto.ELECTRICIDAD, "3.80", 80, 20);
        crear("ELE-0002", "Interruptor simple", CategoriaProducto.ELECTRICIDAD, "8.50", 7, 10);
        crear("ELE-0003", "Tomacorriente doble", CategoriaProducto.ELECTRICIDAD, "14.90", 25, 5);
        crear("GAS-0001", "Codo PVC 1/2 pulgada", CategoriaProducto.GASFITERIA, "2.20", 40, 10);
        crear("GAS-0002", "Tubo PVC 1/2 pulgada", CategoriaProducto.GASFITERIA, "9.90", 18, 5);
        crear("GAS-0003", "Llave de paso 1/2 pulgada", CategoriaProducto.GASFITERIA, "24.50", 5, 8);
        crear("PIN-0001", "Pintura látex blanca 1 galón", CategoriaProducto.PINTURAS, "69.90", 12, 4);
        crear("PIN-0002", "Rodillo para pintar 9 pulgadas", CategoriaProducto.PINTURAS, "17.90", 25, 5);
        crear("PIN-0003", "Brocha de 2 pulgadas", CategoriaProducto.PINTURAS, "10.50", 8, 10);
    }

    private void cargarVentas() {
        List<Producto> disponibles = productos.listar();
        for (int i = 0; i < 30; i++) {
            Producto producto = disponibles.get(i % disponibles.size());
            ventas.registrar(List.of(new ItemVenta(producto.getId(), 1)),
                    new PagoEfectivo(Dinero.soles("1000")));
        }
    }

    private void crear(String codigo, String nombre, CategoriaProducto categoria, String precio, int stock,
                       int stockMinimo) {
        productos.crear(codigo, nombre, categoria, new BigDecimal(precio), stock, stockMinimo);
    }
}
