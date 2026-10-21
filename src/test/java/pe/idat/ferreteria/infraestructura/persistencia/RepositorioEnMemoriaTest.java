package pe.idat.ferreteria.infraestructura.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.idat.ferreteria.dominio.comun.EntidadBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepositorioEnMemoriaTest {

    /** Entidad mínima solo para probar el repositorio genérico. */
    static final class Articulo extends EntidadBase {
        private final String nombre;

        Articulo(Long id, String nombre) {
            super(id);
            this.nombre = nombre;
        }

        String getNombre() {
            return nombre;
        }
    }

    static final class ArticuloRepositorio extends RepositorioEnMemoria<Articulo> {
        List<Articulo> buscarPorNombre(String texto) {
            return filtrar(a -> a.getNombre().contains(texto));
        }
    }

    private ArticuloRepositorio repositorio;

    @BeforeEach
    void crearRepositorio() {
        repositorio = new ArticuloRepositorio();
    }

    private Articulo nuevo(String nombre) {
        return repositorio.guardar(new Articulo(repositorio.siguienteId(), nombre));
    }

    @Test
    void siguienteIdEsCorrelativoYNoSeReutilizaTrasEliminar() {
        Articulo martillo = nuevo("Martillo");
        Articulo alicate = nuevo("Alicate");
        repositorio.eliminarPorId(alicate.getId());

        assertThat(martillo.getId()).isEqualTo(1L);
        assertThat(alicate.getId()).isEqualTo(2L);
        assertThat(repositorio.siguienteId()).isEqualTo(3L);
    }

    @Test
    void guardarConIdExistenteReemplaza() {
        Articulo original = nuevo("Martillo");
        repositorio.guardar(new Articulo(original.getId(), "Martillo 16 oz"));

        assertThat(repositorio.listar()).hasSize(1);
        assertThat(repositorio.buscarPorId(original.getId())).get()
                .extracting(Articulo::getNombre).isEqualTo("Martillo 16 oz");
    }

    @Test
    void guardarConIdMayorAvanzaLaSecuencia() {
        repositorio.guardar(new Articulo(10L, "Cemento"));

        assertThat(repositorio.siguienteId()).isEqualTo(11L);
    }

    @Test
    void listarYFiltrarDevuelvenOrdenadoPorId() {
        repositorio.guardar(new Articulo(5L, "Tubo PVC 1/2"));
        repositorio.guardar(new Articulo(2L, "Tubo PVC 3/4"));
        repositorio.guardar(new Articulo(3L, "Cable THW"));

        assertThat(repositorio.listar()).extracting(Articulo::getId).containsExactly(2L, 3L, 5L);
        assertThat(repositorio.buscarPorNombre("Tubo")).extracting(Articulo::getId).containsExactly(2L, 5L);
    }

    @Test
    void consultasConIdInexistenteONulo() {
        assertThat(repositorio.buscarPorId(99L)).isEmpty();
        assertThat(repositorio.buscarPorId(null)).isEmpty();
        assertThat(repositorio.existePorId(null)).isFalse();
        repositorio.eliminarPorId(null);
        assertThatThrownBy(() -> repositorio.guardar(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void listarNoExponeLaTablaInterna() {
        nuevo("Martillo");

        assertThatThrownBy(() -> repositorio.listar().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void entidadesSonIgualesPorIdYRechazanIdInvalido() {
        assertThat(new Articulo(1L, "A")).isEqualTo(new Articulo(1L, "B"));
        assertThat(new Articulo(1L, "A")).isNotEqualTo(new Articulo(2L, "A"));
        assertThatThrownBy(() -> new Articulo(null, "A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Articulo(0L, "A")).isInstanceOf(IllegalArgumentException.class);
    }
}
