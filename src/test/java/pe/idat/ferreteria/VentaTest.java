package pe.idat.ferreteria;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.modelo.CategoriaProducto;
import pe.idat.ferreteria.modelo.LineaVenta;
import pe.idat.ferreteria.modelo.Sede;
import pe.idat.ferreteria.modelo.Venta;
import pe.idat.ferreteria.modelo.pago.PagoEfectivo;
import pe.idat.ferreteria.modelo.pago.PagoTransferencia;
import pe.idat.ferreteria.modelo.pago.PagoYape;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VentaTest {
    private final List<LineaVenta> lineas = List.of(
            new LineaVenta(1L, "Martillo", CategoriaProducto.HERRAMIENTAS, new BigDecimal("39.90"), new BigDecimal("24.38"), 2),
            new LineaVenta(2L, "Foco LED", CategoriaProducto.ELECTRICIDAD, new BigDecimal("9.50"), new BigDecimal("4.88"), 1));

    @Test
    void calculaTotalIgvYCosto() {
        Venta venta = new Venta(1L, LocalDateTime.now(), Sede.LIMA, "Cliente general", "Vendedor", lineas,
                new PagoYape("987654321"));

        assertEquals("V-000001", venta.getNumero());
        assertEquals(new BigDecimal("89.30"), venta.getTotal());
        assertEquals(new BigDecimal("75.68"), venta.getOpGravada());
        assertEquals(new BigDecimal("13.62"), venta.getIgv());
        assertEquals(new BigDecimal("53.64"), venta.getCosto());
    }

    @Test
    void rechazaEfectivoQueNoCubreElTotal() {
        assertThrows(IllegalArgumentException.class,
                () -> new Venta(1L, LocalDateTime.now(), Sede.LIMA, null, "Vendedor", lineas,
                        new PagoEfectivo(new BigDecimal("50.00"))));
    }

    @Test
    void transferenciaPideNumeroDeOperacion() {
        assertThrows(IllegalArgumentException.class, () -> new PagoTransferencia("12"));
        assertEquals("Transferencia, operación N.º 00123456",
                new PagoTransferencia("00123456").descripcion(BigDecimal.TEN));
    }
}
