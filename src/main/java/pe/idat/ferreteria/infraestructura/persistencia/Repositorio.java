package pe.idat.ferreteria.infraestructura.persistencia;

import pe.idat.ferreteria.dominio.comun.EntidadBase;

import java.util.List;
import java.util.Optional;

/**
 * Operaciones básicas de persistencia. Los servicios dependen de esta interfaz y no de la implementación,
 * para poder cambiar la memoria por una base de datos sin tocar la lógica de negocio.
 * <p>
 * Como el id llega por constructor, quien crea la entidad primero reserva uno con {@link #siguienteId()}:
 * {@code new Producto(repositorio.siguienteId(), ...)}.
 * Los repositorios de productos y ventas la extienden.
 */
public interface Repositorio<T extends EntidadBase> {

    /** Reserva un id nuevo; nunca se repite, aunque se eliminen registros. */
    Long siguienteId();

    /** Inserta la entidad o la reemplaza si ya existe una con su id. */
    T guardar(T entidad);

    Optional<T> buscarPorId(Long id);

    /** Todos los registros ordenados por id. */
    List<T> listar();

    boolean existePorId(Long id);

    void eliminarPorId(Long id);
}
