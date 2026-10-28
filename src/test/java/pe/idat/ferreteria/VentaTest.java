package pe.idat.ferreteria;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.LineaVenta;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.modelo.pago.PagoEfectivo;
import pe.idat.ferreteria.modelo.pago.PagoYape;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VentaTest {

    private final List<LineaVenta> lineas = List.of(
            new LineaVenta(1L, "Martillo", CategoriaProducto.HERRAMIENTAS, new BigDecimal("39.90"), 2),
            new LineaVenta(2L, "Foco LED", CategoriaProducto.ELECTRICIDAD, new BigDecimal("9.50"), 1));

    @Test
    void calculaTotalEIgv() {
        Venta venta = new Venta(1L, LocalDateTime.now(), lineas, new PagoYape("987654321"));

        assertEquals("V-000001", venta.getNumero());
        assertEquals(new BigDecimal("89.30"), venta.getTotal());
        // 89.30 / 1.18 = 75.68 y el IGV es la diferencia (con el IGV de 18 % por defecto).
        assertEquals(new BigDecimal("75.68"), venta.getOpGravada());
        assertEquals(new BigDecimal("13.62"), venta.getIgv());
    }

    @Test
    void rechazaEfectivoQueNoCubreElTotal() {
        assertThrows(IllegalArgumentException.class,
                () -> new Venta(1L, LocalDateTime.now(), lineas, new PagoEfectivo(new BigDecimal("50.00"))));
    }
}
