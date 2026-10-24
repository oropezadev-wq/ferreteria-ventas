package pe.idat.ferreteria.dominio.comun;

/** Un producto, venta o plantilla buscado no existe. En la API se responde con 404. */
public class RecursoNoEncontradoException extends ReglaNegocioException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con id " + id + " no existe");
    }

    /** Para búsquedas por otra clave, por ejemplo el código de producto. */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
