package pe.idat.ferreteria.configuracion;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.Sede;
import pe.idat.ferreteria.modelo.pago.Pago;
import pe.idat.ferreteria.modelo.pago.PagoEfectivo;
import pe.idat.ferreteria.modelo.pago.PagoTarjeta;
import pe.idat.ferreteria.modelo.pago.PagoTransferencia;
import pe.idat.ferreteria.modelo.pago.PagoYape;
import pe.idat.ferreteria.servicio.ItemVenta;
import pe.idat.ferreteria.servicio.ProductoService;
import pe.idat.ferreteria.servicio.VentaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatosIniciales implements ApplicationRunner {
    private static final String[] CLIENTES = {"Cliente general", "Constructora Los Andes S.A.C.", "Cliente general",
            "Inversiones Pacífico E.I.R.L."};
    private static final String[] VENDEDORES = {"Carlos Ríos", "Ana Quispe", "Luis Torres"};

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
        crear("HER-0001", "Martillo de uña 16 oz", "Mango de fibra de vidrio", CategoriaProducto.HERRAMIENTAS, "6.50", "39.90", 60, 5);
        crear("HER-0002", "Desarmador plano 6\"", "Punta imantada", CategoriaProducto.HERRAMIENTAS, "1.80", "12.50", 80, 10);
        crear("HER-0003", "Wincha 5 m", "Cinta de acero con freno", CategoriaProducto.HERRAMIENTAS, "2.60", "18.00", 70, 8);
        crear("CON-0001", "Cemento Tipo I 42.5 kg", "Uso general", CategoriaProducto.CONSTRUCCION, "6.80", "32.50", 120, 20);
        crear("CON-0002", "Fierro corrugado 1/2\" x 9 m", "Grado 60", CategoriaProducto.CONSTRUCCION, "8.90", "45.00", 90, 15);
        crear("CON-0003", "Ladrillo King Kong (millar)", "18 huecos", CategoriaProducto.CONSTRUCCION, "160.00", "780.00", 6, 5);
        crear("ELE-0001", "Cable THW 14 AWG (rollo 100 m)", "Cobre, 600 V", CategoriaProducto.ELECTRICIDAD, "38.00", "189.00", 40, 5);
        crear("ELE-0002", "Interruptor simple", "Empotrable, 10 A", CategoriaProducto.ELECTRICIDAD, "1.20", "8.90", 100, 15);
        crear("ELE-0003", "Foco LED 12 W", "Luz blanca, rosca E27", CategoriaProducto.ELECTRICIDAD, "1.30", "9.50", 150, 20);
        crear("GAS-0001", "Tubo PVC 1/2\" x 5 m", "Para agua fría", CategoriaProducto.GASFITERIA, "2.20", "14.80", 90, 10);
        crear("GAS-0002", "Llave de paso 1/2\"", "Bronce", CategoriaProducto.GASFITERIA, "3.90", "24.00", 50, 8);
        crear("GAS-0003", "Cinta teflón", "12 mm x 10 m", CategoriaProducto.GASFITERIA, "0.30", "2.50", 4, 10);
        crear("PIN-0001", "Pintura látex blanco 1 gal", "Interior y exterior", CategoriaProducto.PINTURAS, "9.50", "54.90", 45, 6);
        crear("PIN-0002", "Brocha 3\"", "Cerda natural", CategoriaProducto.PINTURAS, "1.70", "11.00", 3, 5);
        crear("PIN-0003", "Thinner 1 L", "Acrílico", CategoriaProducto.PINTURAS, "2.40", "16.50", 60, 8);
    }

    private void crear(String codigo, String nombre, String descripcion, CategoriaProducto categoria, String costoDolares,
                       String precio, int stock, int stockMinimo) {
        String proveedor = categoria == CategoriaProducto.HERRAMIENTAS || categoria == CategoriaProducto.ELECTRICIDAD
                ? "Importadora Oriente S.A.C." : "Distribuidora Nacional S.A.";
        productos.crear(codigo, nombre, descripcion, categoria, new BigDecimal(costoDolares), new BigDecimal(precio),
                proveedor, LocalDate.now().minusDays(90), stock, stockMinimo);
    }

    // 30 ventas repartidas en los últimos dos meses y en las 3 sedes; la última es de hoy
    private void cargarVentas() {
        long[] ids = {1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 13, 15};
        LocalDate hoy = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDateTime fecha = hoy.minusDays(i * 2L).atTime(9 + i % 9, 15);
            long primero = ids[i % ids.length];
            long segundo = ids[(i + 5) % ids.length];
            List<ItemVenta> items = List.of(new ItemVenta(primero, 1 + i % 3), new ItemVenta(segundo, 1));
            Sede sede = Sede.values()[i % Sede.values().length];
            ventas.registrar(items, pagoDeEjemplo(i), sede, CLIENTES[i % CLIENTES.length],
                    VENDEDORES[i % VENDEDORES.length], fecha);
        }
    }

    private Pago pagoDeEjemplo(int i) {
        return switch (i % 4) {
            case 1 -> new PagoTarjeta("4321");
            case 2 -> new PagoYape("987654321");
            case 3 -> new PagoTransferencia("00123456");
            default -> new PagoEfectivo(new BigDecimal("1000.00"));
        };
    }
}
