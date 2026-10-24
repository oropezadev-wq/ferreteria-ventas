package pe.idat.ferreteria.dominio.comun;

/** Se intenta vender o descontar más unidades de las disponibles. Lo lanza Producto. */
public class StockInsuficienteException extends ReglaNegocioException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
