package pe.idat.ferreteria.reportes.prototipo;

// Patrón Prototype: el objeto sabe crear una copia de sí mismo.
public interface Prototipo<T> {

    T copiar();
}
