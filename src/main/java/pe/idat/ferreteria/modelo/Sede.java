package pe.idat.ferreteria.modelo;

public enum Sede {
    LIMA("Lima"),
    AREQUIPA("Arequipa"),
    TRUJILLO("Trujillo");

    private final String nombre;

    Sede(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
