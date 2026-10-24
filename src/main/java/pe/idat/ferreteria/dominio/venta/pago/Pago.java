package pe.idat.ferreteria.dominio.venta.pago;

import pe.idat.ferreteria.dominio.comun.Dinero;

/**
 * Forma de pago de una venta. Polimorfismo: la venta llama a {@link #validar} sin saber si es efectivo,
 * tarjeta o Yape; cada subclase valida a su manera. Para agregar un medio de pago nuevo (por ejemplo Plin)
 * basta con crear otra subclase, sin tocar Venta (principio abierto/cerrado).
 */
public abstract class Pago {

    /** Comprueba que el pago es válido para ese total; si no, lanza PagoInvalidoException. */
    public abstract void validar(Dinero total);

    /** Texto para el ticket, por ejemplo "Efectivo: recibido S/ 50.00, vuelto S/ 10.10". */
    public abstract String descripcion(Dinero total);
}
