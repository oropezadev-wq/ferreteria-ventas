package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FirmaDigitalDecorador extends DecoradorReporte {
    public FirmaDigitalDecorador(Reporte reporte) {
        super(reporte);
    }

    @Override
    public String getHtml() {
        String contenido = reporte.getHtml();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        // el código sale del contenido del reporte: si alguien lo cambia, el código ya no coincide
        String codigo = Integer.toHexString(contenido.hashCode()).toUpperCase();
        return contenido
                + "<div class=\"firma\"><p>Firmado digitalmente por: Administrador</p>"
                + "<p>Fecha: " + fecha + "</p>"
                + "<p>Código de verificación: " + codigo + "</p></div>";
    }
}
