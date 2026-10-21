package pe.idat.ferreteria.infraestructura.persistencia;

import pe.idat.ferreteria.dominio.comun.EntidadBase;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * Simula una tabla de base de datos con un {@link Map} en memoria.
 * <ul>
 *   <li>Los ids son autoincrementales y nunca se reutilizan, aunque se elimine un registro.</li>
 *   <li>Si se guarda una entidad con un id mayor que el último reservado, la secuencia avanza
 *       para que {@link #siguienteId()} no lo repita.</li>
 *   <li>{@link ConcurrentHashMap} y {@link AtomicLong} evitan datos corruptos si llegan varias peticiones a la vez.</li>
 *   <li>Los datos se pierden al detener la aplicación (ver docs/decisiones/ADR-001).</li>
 * </ul>
 */
public abstract class RepositorioEnMemoria<T extends EntidadBase> implements Repositorio<T> {

    private final Map<Long, T> tabla = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public Long siguienteId() {
        return secuencia.incrementAndGet();
    }

    @Override
    public T guardar(T entidad) {
        Objects.requireNonNull(entidad, "La entidad es obligatoria");
        secuencia.accumulateAndGet(entidad.getId(), Math::max);
        tabla.put(entidad.getId(), entidad);
        return entidad;
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        return id == null ? Optional.empty() : Optional.ofNullable(tabla.get(id));
    }

    @Override
    public List<T> listar() {
        return filtrar(entidad -> true);
    }

    @Override
    public boolean existePorId(Long id) {
        return id != null && tabla.containsKey(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        if (id != null) {
            tabla.remove(id);
        }
    }

    /** Registros que cumplen la condición, ordenados por id como lo haría un SELECT con ORDER BY. */
    protected List<T> filtrar(Predicate<T> condicion) {
        return tabla.values().stream()
                .filter(condicion)
                .sorted(Comparator.comparing(EntidadBase::getId))
                .toList();
    }
}
