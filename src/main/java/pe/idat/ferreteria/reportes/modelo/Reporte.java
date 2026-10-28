package pe.idat.ferreteria.reportes.modelo;

// Lo que comparten el reporte base y sus decoradores.
public interface Reporte {

    String getTitulo();

    String getHtml();
}
