package pe.idat.ferreteria.reportes.prototipo;

/**
 * Patrón Prototype: un objeto sabe crear una copia de sí mismo.
 * <p>
 * Por contrato la copia es PROFUNDA: todo lo mutable (hijos, estilos, colecciones) se duplica, de modo que
 * modificar la copia nunca altera el original; lo inmutable puede compartirse. No se usa {@link Cloneable}
 * porque su copia por defecto es superficial.
 * Contrato común (Integrante 1); lo implementan las plantillas (Integrante 2) y los elementos del Composite (Integrante 4).
 */
@FunctionalInterface
public interface Prototipo<T> {

    T copiar();
}
