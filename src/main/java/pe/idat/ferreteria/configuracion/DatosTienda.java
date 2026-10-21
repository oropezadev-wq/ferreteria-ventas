package pe.idat.ferreteria.configuracion;

/** Datos de la tienda que aparecen en el layout, el ticket y el sello de los reportes. */
public interface DatosTienda {

    String getRazonSocial();

    /** RUC ficticio de demostración. */
    String getRuc();

    String getDireccion();

    String getTelefono();

    /** Cantidad desde la cual un producto se considera con stock bajo. */
    int getUmbralStockBajo();
}
