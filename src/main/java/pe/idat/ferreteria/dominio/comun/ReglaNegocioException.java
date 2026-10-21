package pe.idat.ferreteria.dominio.comun;

/**
 * Base de las excepciones del dominio: una operación viola una regla del negocio.
 * En la API se responde con 409; en las pantallas se muestra el mensaje al usuario.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
