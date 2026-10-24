package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;

/**
 * Decorador que agrega una huella SHA-256 del contenido del reporte (firma digital SIMULADA).
 * <p>
 * La huella cambia si cambia un solo carácter del reporte, así que sirve para comprobar que no fue alterado.
 * No prueba quién lo firmó: para eso haría falta una firma real con clave privada y certificado.
 * Debe ser la última capa para que su huella cubra también el sello y la marca de agua.
 */
public final class FirmaDigitalDecorador extends DecoradorReporte {

    public FirmaDigitalDecorador(Reporte envuelto) {
        super(envuelto);
    }

    /** Huella SHA-256 del HTML del reporte envuelto, en hexadecimal (64 caracteres). */
    public String getHuella() {
        // TODO (issue Decorator):
        //   byte[] bytes = MessageDigest.getInstance("SHA-256")
        //           .digest(getEnvuelto().getHtml().getBytes(StandardCharsets.UTF_8));
        //   return HexFormat.of().formatHex(bytes);
        // MessageDigest.getInstance lanza NoSuchAlgorithmException (excepción comprobada): capturarla con
        // try/catch y relanzarla como new IllegalStateException("SHA-256 no disponible", e).
        throw new UnsupportedOperationException("Por implementar: FirmaDigitalDecorador.getHuella");
    }

    @Override
    public String getHtml() {
        // TODO (issue Decorator): getEnvuelto().getHtml()
        //   + "<p class=\"firma\">Huella SHA-256 (firma simulada): " + getHuella() + "</p>"
        throw new UnsupportedOperationException("Por implementar: FirmaDigitalDecorador.getHtml");
    }
}
