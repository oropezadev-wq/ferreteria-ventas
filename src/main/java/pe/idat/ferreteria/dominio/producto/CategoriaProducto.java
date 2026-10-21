package pe.idat.ferreteria.dominio.producto;

/** Categorías de productos de la ferretería. */
public enum CategoriaProducto {
    HERRAMIENTAS("Herramientas"),
    CONSTRUCCION("Construcción"),
    ELECTRICIDAD("Electricidad"),
    GASFITERIA("Gasfitería"),
    PINTURAS("Pinturas");

    private final String nombre;

    CategoriaProducto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
