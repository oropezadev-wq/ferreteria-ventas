# Diseño del gestor de ventas de ferretería

Documento de referencia para implementar cada módulo y patrón. Si algo del código difiere de este diseño, manda el código y se anota aquí.
El id de las entidades llega por constructor (sin `setId()` público) para no exponer atributos.

## 1. Decisiones de arquitectura

| Decisión | Elección | Motivo |
|---|---|---|
| Capas | dominio (Java puro) → aplicacion → infraestructura → web; módulo reportes con un subpaquete por patrón | Cada patrón visible en su carpeta; dominio testeable sin Spring |
| Persistencia | En memoria con interfaces `Repositorio<T>`, sin JPA/H2 | JPA obliga a setters/constructor vacío (choca con POO) y complica la clonación profunda |
| UI | Thymeleaf + Bootstrap 5 responsive, Bootstrap local en static/vendor | Un solo proyecto, sin CORS; JS solo para filas de la venta |
| API REST | Opcional: /api/reportes, /api/productos, /api/ventas + Swagger | Casi gratis; muestra el Facade |
| Reporte | HTML autocontenido (CSS en línea, gráficos de barras CSS), descargable; PDF con "Imprimir" del navegador (@media print) | PDF a costo cero |
| Tiempo | Bean `Clock` (America/Lima) | Pruebas deterministas |
| IGV | Precios incluyen IGV; opGravada = total/1.18 (HALF_UP), igv = total - opGravada | Cuadra al céntimo; invariante testeable |

Dependencias: web solo conoce aplicacion y ReporteFacade; reportes lee datos solo vía `ConsultaDatosReporte`; dominio no importa nada externo.

## 2. Estructura del repositorio

```
ferreteria-ventas/
├── README.md  LICENSE(MIT)  pom.xml  mvnw  mvnw.cmd  .mvn/wrapper/ (Maven 3.9.16)
├── .github/workflows/ci.yml
├── docs/
│   ├── arquitectura.md
│   ├── patrones.md        (por patrón: problema, clases, ruta al código, test que lo prueba)
│   ├── decisiones/ADR-001-persistencia-en-memoria.md, ADR-002-singleton.md, ADR-003-formato-reporte.md
│   ├── uml/ferreteria.mdj + uml/png/
│   ├── capturas/
│   └── sustentacion/sustentacion.pdf
├── src/main/java/pe/idat/ferreteria/
│   ├── configuracion/   ConfiguracionGlobal, ParametrosFiscales, FormatoRegional, DatosTienda,
│   │                    ParametrosTienda(record), ConfiguracionBeans, DatosIniciales
│   ├── dominio/comun/   EntidadBase, Dinero, excepciones (ReglaNegocio, RecursoNoEncontrado,
│   │                    StockInsuficiente, MonedaIncompatible, PagoInvalido)
│   ├── dominio/producto/ Producto, CodigoProducto, CategoriaProducto, UnidadMedida, ProductoRepositorio
│   ├── dominio/venta/   Venta, LineaVenta, EstadoVenta, VentaRepositorio,
│   │                    pago/ Pago, PagoEfectivo, PagoTarjeta, PagoTransferencia, PagoYape, TipoPago
│   ├── aplicacion/      ProductoService(+Impl), VentaService(+Impl), comando/
│   ├── infraestructura/persistencia/  Repositorio, RepositorioEnMemoria, *RepositorioEnMemoria
│   ├── reportes/
│   │   ├── modelo/      Reporte, ReporteBase, TipoReporte, Periodo, Granularidad, NivelDetalle, TipoContenido
│   │   ├── datos/       ConsultaDatosReporte, ResumenVentas, InventarioValorizado
│   │   ├── prototipo/   Prototipo, PlantillaReporte, PlantillaVentas, PlantillaInventario, EstiloReporte,
│   │   │                RegistroPlantillas, PlantillasPredefinidas
│   │   ├── builder/     ReporteBuilder, EnsambladorContenido, EnsambladorVentas, EnsambladorInventario
│   │   ├── composite/   ElementoReporte, Seccion, Parrafo, Tabla, Grafico, Indicador, Html (escape)
│   │   ├── decorador/   DecoradorReporte, SelloFerreteriaDecorador, MarcaAguaDecorador, FirmaDigitalDecorador
│   │   └── fachada/     ReporteFacade, SolicitudReporte, ReporteGenerado, ExtraReporte, DocumentoHtml
│   └── web/             controladores, formulario/ (DTO + Bean Validation), ModeloGlobalAdvice,
│                        ManejadorErroresWeb, api/
├── src/main/resources/  templates/, static/{css, vendor/bootstrap, js/venta.js}
└── src/test/java/pe/idat/ferreteria/   (refleja main)
```

## 3. Patrones

### 3.1 Singleton: ConfiguracionGlobal (criterio 3; requisito 1)
Singleton clásico (holder idiom) publicado como bean; los consumidores lo reciben inyectado por interfaces. El patrón se ve en el código, respeta DIP y se puede probar.

```java
public final class ConfiguracionGlobal implements ParametrosFiscales, FormatoRegional, DatosTienda {
    private final AtomicReference<ParametrosTienda> parametros = new AtomicReference<>(ParametrosTienda.porDefecto());
    private ConfiguracionGlobal() { if (Holder.INSTANCIA != null) throw new IllegalStateException("Usar getInstancia()"); }
    private static final class Holder { private static final ConfiguracionGlobal INSTANCIA = new ConfiguracionGlobal(); }
    public static ConfiguracionGlobal getInstancia() { return Holder.INSTANCIA; }
    public void actualizar(ParametrosTienda nuevos)   // valida y reemplaza atómicamente
    // ParametrosFiscales: getMoneda(), getTasaIgv(), getRedondeo()
    // FormatoRegional: formatearDinero, formatearFecha, formatearFechaHora, getLocale
    // DatosTienda: getRazonSocial, getRuc, getDireccion, getTelefono, getUmbralStockBajo
}
```
- Inicialización perezosa y thread-safe por la JVM; estado en record inmutable dentro de AtomicReference.
- Constructor privado, clase final, guarda contra reflexión, no Serializable (alternativa enum en el ADR).
- ConfiguracionBeans expone un solo @Bean que devuelve `getInstancia()` (con tres, Spring no sabe cuál inyectar: ver ADR-002); vistas vía ModeloGlobalAdvice.
- El dominio no lo llama: VentaServiceImpl pasa la tasa de IGV a Venta.
- Demo: /configuracion cambia el nombre de la tienda y se refleja en ticket y reportes.

### 3.2 Prototype: plantillas de reporte (criterio 3; requisito 2)
```java
public interface Prototipo<T> { T copiar(); }   // copia PROFUNDA por contrato
public abstract class PlantillaReporte implements Prototipo<PlantillaReporte> {
    clave, titulo, contenido, granularidad (DIA, MES, ANIO, NINGUNA),
    Seccion estructura (esqueleto Composite), EstiloReporte estilo, EnumSet<ExtraReporte> extrasPorDefecto
    protected PlantillaReporte(PlantillaReporte origen)   // estructura.copiar(), estilo.copiar(), EnumSet.copyOf
    renombrar(String), conGranularidad(Granularidad), agregarSeccionBase(clave, titulo), copiarEstructura()
}
PlantillaVentas (incluirComparativo), PlantillaInventario (categorias, umbral), EstiloReporte implements Prototipo
RegistroPlantillas: registrar(TipoReporte, PlantillaReporte); obtener(TipoReporte) -> SIEMPRE copiar()
```
- Se copia todo lo mutable (árbol Composite recursivo, estilo, sets, filas de tablas); lo inmutable se comparte. Sin Cloneable (copia superficial por defecto).
- El Facade solo conoce RegistroPlantillas y la clase abstracta.
- PlantillasPredefinidas: una `ventasBase` y derivadas por clonación (diario, mensual con "Ventas por categoría", anual) + INVENTARIO_VALORIZADO.

### 3.3 Builder: ReporteBuilder (criterio 3; requisito 3)
```java
ReporteBuilder.nuevo(titulo) | ReporteBuilder.desdePlantilla(plantilla)
  .periodo(p).generadoEl(t).estilo(e)
  .seccion(titulo) / .enSeccion(clave) / .finSeccion()     // pila -> anidamiento ilimitado
  .parrafo(texto).indicador(etiqueta, valor).tabla(titulo, encabezados, filas).grafico(titulo, serie)
  .omitirSeccionesVacias()
  .build()   // valida título, >=1 elemento, secciones cerradas; devuelve ReporteBase inmutable
interface EnsambladorContenido { boolean soporta(TipoContenido); void ensamblar(ReporteBuilder, Periodo, NivelDetalle); }
```
- Builder arma el árbol; la representación HTML la deciden los nodos y DocumentoHtml. Ensambladores = directores.
- Configuraciones: con plantilla o desde cero; RESUMEN (indicadores y gráficos) vs DETALLADO (+ tablas).
- Todo texto se escapa con Html.escapar (la vista usa th:utext).

### 3.4 Composite (criterio 4; requisito 5)
```java
public abstract class ElementoReporte implements Prototipo<ElementoReporte> {
    renderizarHtml(int nivel); contarElementos(); copiar();
}
Seccion: agregar (rechaza null, a sí misma y ancestros), quitar, hijos() (List.copyOf), buscar(clave) recursivo, contiene, estaVacia
Hojas: Parrafo, Indicador, Tabla, Grafico
```
Diseño "seguro": agregar/quitar solo en Seccion (ISP + LSP); explicarlo frente al diseño "transparente" de GoF. Sin límite de profundidad.

### 3.5 Decorator (criterio 4; requisito 6)
```java
interface Reporte { titulo(); estilosCss(); encabezadoHtml(); cuerpoHtml(); pieHtml(); List<String> caracteristicas(); }
ReporteBase implements Reporte
abstract DecoradorReporte implements Reporte (envuelto, delega por defecto)
SelloFerreteriaDecorador(Reporte, DatosTienda)    // pie con razón social, RUC ficticio, dirección
MarcaAguaDecorador(Reporte, String texto)         // "BORRADOR", "CONFIDENCIAL"
FirmaDigitalDecorador(Reporte, firmante, Clock)   // SHA-256 de encabezado+cuerpo+pie + firmante + fecha; huella()
```
- Partes separadas para no manipular un HTML completo; DocumentoHtml.ensamblar arma el archivo.
- Orden fijo por enum ExtraReporte (SELLO, MARCA_AGUA, FIRMA); la firma al final para cubrir todo.
- Firma = huella de integridad, NO prueba autoría (no hay claves). Una real usaría java.security.Signature + certificado. En la UI: "Huella SHA-256 (firma simulada)".

### 3.6 Facade (criterio 4; requisito 4)
```java
@Service public class ReporteFacade {
    ReporteFacade(RegistroPlantillas, List<EnsambladorContenido>, FormatoRegional, DatosTienda, Clock)
    public ReporteGenerado generarReporte(SolicitudReporte solicitud)   // único método público
}
record SolicitudReporte(TipoReporte tipo, LocalDate fechaReferencia, NivelDetalle detalle, Set<ExtraReporte> extras, String textoMarcaAgua)
record ReporteGenerado(titulo, html, nombreArchivo, Optional<String> huellaSha256, caracteristicas, generadoEl)
```
Flujo: configuración → registro.obtener (clon profundo) → Periodo → ReporteBuilder.desdePlantilla → ensamblador que soporta el contenido → build → decoradores en orden → DocumentoHtml → ReporteGenerado.

## 4. Dominio y SOLID

- EntidadBase: id final por constructor; equals/hashCode por id.
- Dinero (record): monto escala 2 HALF_UP, Currency; no nulo, >= 0; sumar, restar, multiplicar, comparar; monedas distintas -> MonedaIncompatibleException.
- CodigoProducto: `^[A-Z]{3}-\d{4}$` (ELE-0012).
- CategoriaProducto: HERRAMIENTAS, CONSTRUCCION, ELECTRICIDAD, GASFITERIA, PINTURAS, FERRETERIA_GENERAL. UnidadMedida: UNIDAD, METRO, BOLSA, GALON, CAJA, ROLLO, KILO.
- Producto: precioVenta (con IGV), costoUnitario, stock, stockMinimo, activo; descontarStock (StockInsuficienteException), reponerStock, cambiarPrecio, estaBajoMinimo, valorizar, desactivar. Sin setters.
- Pago (abstracta, validarContra(total)): PagoEfectivo (vuelto), PagoTarjeta (solo marca y últimos 4), PagoTransferencia, PagoYape (`^9\d{8}$`).
- LineaVenta: snapshot del producto + cantidad > 0 + subtotal.
- Venta: número V-000123 (ticket interno), Venta.registrar(...); al menos una línea, líneas repetidas fusionadas, una moneda, pago validado, opGravada + igv == total, anular una sola vez.
- VentaServiceImpl.registrarVenta: verifica stock de todas las líneas y luego descuenta (todo o nada).
- Formularios: DTO con Bean Validation, separados de las entidades.

| Principio | Evidencia |
|---|---|
| S | Venta (reglas), VentaServiceImpl (orquestación), ConsultaDatosReporte, EnsambladorVentas, cada decorador, ReporteFacade |
| O | Nuevo pago (PagoPlin) sin tocar Venta; nuevo reporte = plantilla + ensamblador sin tocar el Facade; nueva hoja o decorador sin tocar lo existente |
| L | Contrato validarContra en todas las subclases de Pago (test parametrizado); decoradores sustituyen a Reporte; hojas/Seccion a ElementoReporte |
| I | ConfiguracionGlobal con 3 interfaces pequeñas; Prototipo<T>; Composite seguro; repos específicos |
| D | Servicios dependen de interfaces de repositorio; Facade de List<EnsambladorContenido>, interfaces de configuración y Clock |

## 5. Pantallas y endpoints

| Ruta | Pantalla |
|---|---|
| GET / | Dashboard: ventas de hoy, ticket promedio, alertas de stock bajo |
| /productos (listar, filtrar, nuevo, editar, reponer) | Inventario |
| GET /ventas/nueva, POST /ventas | Punto de venta (filas dinámicas, método de pago con campos por tipo; el servidor recalcula) |
| GET /ventas, GET /ventas/{id}, POST /ventas/{id}/anular | Historial y ticket |
| GET /reportes, POST /reportes/generar, GET /reportes/descargar | Tipo, fecha, nivel, extras, marca de agua → vista previa, huella, descarga, imprimir |
| GET/POST /configuracion | Datos de la tienda (demo del Singleton) |
| /api/... | Opcional con Swagger |

Figma (si se pide): 5 pantallas en móvil (360 px) y desktop (1280 px).
Datos semilla: ~40 productos reales (martillo 16 oz S/ 39.90, cemento Sol 42.5 kg S/ 32.50, fierro 1/2" S/ 45.00, cable THW 14 S/ 2.10/m, tubo PVC 1/2" S/ 8.50, pintura látex galón S/ 59.90...), ~365 días de ventas (Random(42)), varios productos bajo mínimo. Tienda ficticia "Ferretería El Constructor S.A.C." con RUC marcado como ficticio.

## 6. Pruebas (JUnit 5 + AssertJ, 60-80 tests, JaCoCo)

- Singleton: misma referencia; 100 hilos -> 1 instancia; constructor privado y reflexión bloqueada; actualizar visible y validado; bean == getInstancia(); restaurar en @AfterEach.
- Prototype: clon distinto en cada nivel; modificar el clon no altera el original; obtener() dos veces da instancias distintas; clave inexistente; copiar() conserva el tipo; mensual = base + su sección.
- Builder: RESUMEN vs DETALLADO; desde plantilla y desde cero; errores de secciones abiertas/cerradas y título vacío; enSeccion inexistente; omitirSeccionesVacias; 10 niveles fluidos.
- Composite: 50 niveles con contarElementos; sección vacía; trato uniforme; anti-ciclos; hijos() inmodificable; escape de `<script>`; buscar profundo.
- Decorator: cada uno agrega su marca; orden en caracteristicas(); capas repetibles; huella de 64 hex igual a MessageDigest; determinismo con Clock; firma antes del sello no lo cubre.
- Facade: repos en memoria sembrados; firma siempre última; 4 tipos; tipo sin ensamblador; sin extras no hay huella.
- Dominio: Dinero, CodigoProducto, stock insuficiente sin cambios, IGV al céntimo, pago insuficiente, anulación doble, contrato de Pago parametrizado.
- Servicio: venta con segunda línea sin stock no cambia ningún stock; numeración correlativa.
- Web (MockMvc): stock insuficiente muestra mensaje; generar reporte responde 200.

## 7. UML (StarUML: docs/uml/ferreteria.mdj + PNG)

1. Paquetes/componentes. 2. Clases de dominio. 3. Creacionales (Singleton, Prototype, Builder). 4. Estructurales (Composite, Decorator, Facade). 5. Secuencia de generarReporte. 6. Opcional: secuencia de registrarVenta.

## 8. Orden de construcción

Una rama por tarea; todo cambio entra a `main` por Pull Request (ver `CONTRIBUTING.md`). Orden lógico de construcción: proyecto base → readme → Singleton (+test) → Dinero → Producto → repositorios → Venta y pagos → registro atómico → datos semilla → web productos → punto de venta → Composite → Prototype → Builder → Decorators → Facade → web reportes → web configuración → tests de reportes → CI → UML → README → sustentación.

El reparto de tareas y las fechas están en los issues del repositorio.

## 9. Adaptación a otro dominio (caso SERF) y riesgos

Cambia solo el dominio: Producto con país de origen y costo en otra moneda; entidad Filial y Venta.filialId; ConversorMoneda con tasas en ConfiguracionGlobal (moneda de reporte EUR); plantilla y EnsambladorConsolidado (Consolidado → Filial → Categoría). Los patrones y el Facade no cambian.

Riesgos: escape obligatorio por th:utext; estado del Singleton en tests; JS del punto de venta acotado; redondeo del IGV; datos en memoria se pierden al reiniciar (ADR).
