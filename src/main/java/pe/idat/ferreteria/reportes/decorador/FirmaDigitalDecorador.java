package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

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
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                    .digest(getEnvuelto().getHtml().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    @Override
    public String getHtml() {
        return getEnvuelto().getHtml()
                + "<p class=\"firma\">Huella SHA-256 (firma simulada): " + getHuella() + "</p>";
    }
}
