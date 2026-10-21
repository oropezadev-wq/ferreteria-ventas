package pe.idat.ferreteria.dominio.comun;

/** El pago no cubre el total o sus datos no son válidos. Lo lanza la jerarquía Pago. */
public class PagoInvalidoException extends ReglaNegocioException {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
