package pe.idat.ferreteria.reportes.decorador;

import pe.idat.ferreteria.reportes.modelo.Reporte;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FirmaDecorador extends DecoradorReporte {
    public FirmaDecorador(Reporte reporte) {
        super(reporte);
    }

    @Override
    public String getHtml() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return reporte.getHtml()
                + "<div class=\"firma\"><p>______________________</p><p>Administrador</p>"
                + "<p>Generado el " + fecha + "</p></div>";
    }
}
