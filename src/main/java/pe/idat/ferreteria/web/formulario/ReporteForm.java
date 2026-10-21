package pe.idat.ferreteria.web.formulario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import pe.idat.ferreteria.reportes.fachada.SolicitudReporte;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;

/** Datos de la pantalla de reportes: qué reporte, de qué fecha y con qué adornos. */
public class ReporteForm {

    @NotNull(message = "Elija un reporte")
    private TipoReporte tipo = TipoReporte.VENTAS_MENSUAL;

    @NotNull(message = "Elija una fecha")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    private boolean conSello = true;

    @Size(max = SolicitudReporte.LARGO_MAXIMO_MARCA_AGUA, message = "Máximo {max} caracteres")
    private String marcaAgua;

    private boolean conFirma = true;

    public SolicitudReporte aSolicitud() {
        return new SolicitudReporte(tipo, fecha, conSello, marcaAgua, conFirma);
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isConSello() {
        return conSello;
    }

    public void setConSello(boolean conSello) {
        this.conSello = conSello;
    }

    public String getMarcaAgua() {
        return marcaAgua;
    }

    public void setMarcaAgua(String marcaAgua) {
        this.marcaAgua = marcaAgua;
    }

    public boolean isConFirma() {
        return conFirma;
    }

    public void setConFirma(boolean conFirma) {
        this.conFirma = conFirma;
    }
}
