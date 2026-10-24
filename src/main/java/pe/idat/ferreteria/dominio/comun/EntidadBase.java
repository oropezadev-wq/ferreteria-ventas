package pe.idat.ferreteria.dominio.comun;

import java.util.Objects;

/**
 * Base de las entidades del dominio (Producto, Venta...). El id llega por constructor y no cambia:
 * no hay {@code setId()}, así que nadie fuera de la entidad puede alterar su identidad.
 * <p>
 * Dos entidades son iguales si son de la misma clase y tienen el mismo id, aunque sus demás datos difieran.
 * Cada entidad (Producto, Venta) la extiende.
 */
public abstract class EntidadBase {

    private final Long id;

    protected EntidadBase(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id de la entidad debe ser un número positivo");
        }
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public final boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        return id.equals(((EntidadBase) otro).id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
