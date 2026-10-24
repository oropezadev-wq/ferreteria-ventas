package pe.idat.ferreteria.reportes.fachada;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudReporteTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 21);

    @Test
    void tipoYFechaSonObligatorios() {
        assertThatThrownBy(() -> new SolicitudReporte(null, HOY, true, null, true))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, null, true, null, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void marcaDeAguaVaciaSignificaSinMarca() {
        SolicitudReporte solicitud = new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, false, "   ", false);

        assertThat(solicitud.conMarcaAgua()).isFalse();
        assertThat(solicitud.marcaAgua()).isNull();
    }

    @Test
    void marcaDeAguaSeRecortaYTieneLimite() {
        SolicitudReporte solicitud = new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, false, "  BORRADOR ", false);
        assertThat(solicitud.marcaAgua()).isEqualTo("BORRADOR");

        String largo = "X".repeat(SolicitudReporte.LARGO_MAXIMO_MARCA_AGUA + 1);
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, false, largo, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
