package pe.idat.ferreteria.reportes.fachada;

import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.reportes.modelo.NivelDetalle;
import pe.idat.ferreteria.reportes.modelo.TipoReporte;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudReporteTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 20);

    @Test
    void rechazaCamposObligatoriosNulos() {
        assertThatThrownBy(() -> new SolicitudReporte(null, HOY, NivelDetalle.RESUMEN, Set.of(), null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("tipo");
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, null, NivelDetalle.RESUMEN, Set.of(), null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("fecha");
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, null, Set.of(), null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("detalle");
    }

    @Test
    void marcaDeAguaExigeTextoValido() {
        Set<ExtraReporte> conMarca = Set.of(ExtraReporte.MARCA_AGUA);

        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN, conMarca, "  "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN, conMarca,
                "X".repeat(SolicitudReporte.LARGO_MAXIMO_MARCA_AGUA + 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void marcaDeAguaEnElLimiteYConEspaciosAlrededorSeAcepta() {
        Set<ExtraReporte> conMarca = Set.of(ExtraReporte.MARCA_AGUA);
        String treinta = "Ñ".repeat(SolicitudReporte.LARGO_MAXIMO_MARCA_AGUA);

        assertThat(new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN, conMarca, treinta)
                .textoMarcaAgua()).isEqualTo(treinta);
        // El largo se mide después de recortar espacios.
        assertThat(new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN, conMarca,
                "   " + treinta + "   ").textoMarcaAgua()).isEqualTo(treinta);
        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN, conMarca, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void copiaDefensivaDeExtras() {
        java.util.Set<ExtraReporte> origen = new java.util.HashSet<>(Set.of(ExtraReporte.SELLO));
        SolicitudReporte solicitud = new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN,
                origen, null);

        origen.add(ExtraReporte.FIRMA);

        assertThat(solicitud.extras()).containsExactly(ExtraReporte.SELLO);
        assertThat(solicitud.tieneExtra(ExtraReporte.FIRMA)).isFalse();
        assertThat(solicitud.tieneExtra(null)).isFalse();
    }

    @Test
    void rechazaExtrasConNulos() {
        List<ExtraReporte> conNulo = java.util.Arrays.asList(ExtraReporte.SELLO, null);

        assertThatThrownBy(() -> new SolicitudReporte(TipoReporte.VENTAS_DIARIO, HOY, NivelDetalle.RESUMEN,
                new java.util.HashSet<>(conNulo), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void extrasQuedanInmodificablesYEnElOrdenDelEnum() {
        SolicitudReporte solicitud = new SolicitudReporte(TipoReporte.VENTAS_MENSUAL, HOY, NivelDetalle.DETALLADO,
                Set.of(ExtraReporte.FIRMA, ExtraReporte.SELLO, ExtraReporte.MARCA_AGUA), "  BORRADOR ");

        assertThat(solicitud.extras())
                .containsExactly(ExtraReporte.SELLO, ExtraReporte.MARCA_AGUA, ExtraReporte.FIRMA);
        assertThat(solicitud.textoMarcaAgua()).isEqualTo("BORRADOR");
        assertThatThrownBy(() -> solicitud.extras().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void sinMarcaDeAguaElTextoSeDescartaYExtrasNulosEsVacio() {
        SolicitudReporte solicitud = new SolicitudReporte(TipoReporte.INVENTARIO_VALORIZADO, HOY,
                NivelDetalle.RESUMEN, null, "BORRADOR");

        assertThat(solicitud.extras()).isEmpty();
        assertThat(solicitud.textoMarcaAgua()).isNull();
        assertThat(solicitud.tieneExtra(ExtraReporte.FIRMA)).isFalse();
    }
}
