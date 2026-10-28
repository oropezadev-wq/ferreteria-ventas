package pe.idat.ferreteria.configuracion;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.pago.Pago;
import pe.idat.ferreteria.modelo.pago.PagoEfectivo;
import pe.idat.ferreteria.modelo.pago.PagoTarjeta;
import pe.idat.ferreteria.modelo.pago.PagoYape;
import pe.idat.ferreteria.servicio.ItemVenta;
import pe.idat.ferreteria.servicio.ProductoService;
import pe.idat.ferreteria.servicio.VentaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Carga productos y ventas de ejemplo al arrancar. Como todo está en memoria, se cargan de nuevo cada vez.
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
        crear("HER-0001", "Martillo de uña 16 oz", CategoriaProducto.HERRAMIENTAS, "39.90", 60, 5);
        crear("HER-0002", "Desarmador plano 6\"", CategoriaProducto.HERRAMIENTAS, "12.50", 80, 10);
        crear("HER-0003", "Wincha 5 m", CategoriaProducto.HERRAMIENTAS, "18.00", 70, 8);
        crear("CON-0001", "Cemento Tipo I 42.5 kg", CategoriaProducto.CONSTRUCCION, "32.50", 120, 20);
        crear("CON-0002", "Fierro corrugado 1/2\" x 9 m", CategoriaProducto.CONSTRUCCION, "45.00", 90, 15);
        crear("CON-0003", "Ladrillo King Kong (millar)", CategoriaProducto.CONSTRUCCION, "780.00", 6, 5);
        crear("ELE-0001", "Cable THW 14 AWG (rollo 100 m)", CategoriaProducto.ELECTRICIDAD, "189.00", 40, 5);
        crear("ELE-0002", "Interruptor simple", CategoriaProducto.ELECTRICIDAD, "8.90", 100, 15);
        crear("ELE-0003", "Foco LED 12 W", CategoriaProducto.ELECTRICIDAD, "9.50", 150, 20);
        crear("GAS-0001", "Tubo PVC 1/2\" x 5 m", CategoriaProducto.GASFITERIA, "14.80", 90, 10);
        crear("GAS-0002", "Llave de paso 1/2\"", CategoriaProducto.GASFITERIA, "24.00", 50, 8);
        crear("GAS-0003", "Cinta teflón", CategoriaProducto.GASFITERIA, "2.50", 4, 10);
        crear("PIN-0001", "Pintura látex blanco 1 gal", CategoriaProducto.PINTURAS, "54.90", 45, 6);
        crear("PIN-0002", "Brocha 3\"", CategoriaProducto.PINTURAS, "11.00", 3, 5);
        crear("PIN-0003", "Thinner 1 L", CategoriaProducto.PINTURAS, "16.50", 60, 8);
    }

    private void crear(String codigo, String nombre, CategoriaProducto categoria, String precio, int stock,
                       int stockMinimo) {
        productos.crear(codigo, nombre, categoria, new BigDecimal(precio), stock, stockMinimo);
    }

    // 30 ventas repartidas en los últimos dos meses; la última es de hoy.
    private void cargarVentas() {
        // Solo productos con stock de sobra (no se usan los que están bajo el mínimo).
        long[] ids = {1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 13, 15};
        LocalDate hoy = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDateTime fecha = hoy.minusDays(i * 2L).atTime(9 + i % 9, 15);
            long primero = ids[i % ids.length];
            long segundo = ids[(i + 5) % ids.length];
            List<ItemVenta> items = List.of(new ItemVenta(primero, 1 + i % 3), new ItemVenta(segundo, 1));
            ventas.registrar(items, pagoDeEjemplo(i), fecha);
        }
    }

    private Pago pagoDeEjemplo(int i) {
        if (i % 3 == 1) {
            return new PagoTarjeta("4321");
        }
        if (i % 3 == 2) {
            return new PagoYape("987654321");
        }
        return new PagoEfectivo(new BigDecimal("1000.00"));
    }
}
