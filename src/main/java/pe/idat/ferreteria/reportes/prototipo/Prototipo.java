package pe.idat.ferreteria.reportes.prototipo;

/**
 * Patrón Prototype: un objeto sabe crear una copia de sí mismo.
 * <p>
 * Por contrato la copia es PROFUNDA: todo lo mutable (hijos, estilos, colecciones) se duplica, de modo que
 * modificar la copia nunca altera el original; lo inmutable puede compartirse. No se usa {@link Cloneable}
 * porque su copia por defecto es superficial.
 * Lo implementan PlantillaReporte y las piezas del Composite (ElementoReporte).
 */
@FunctionalInterface
public interface Prototipo<T> {

    T copiar();
}
