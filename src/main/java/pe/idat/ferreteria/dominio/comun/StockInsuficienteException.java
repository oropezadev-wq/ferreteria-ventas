package pe.idat.ferreteria.dominio.comun;

/** Se intenta vender o descontar más unidades de las disponibles. Lo lanza Producto (Integrante 2). */
public class StockInsuficienteException extends ReglaNegocioException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
