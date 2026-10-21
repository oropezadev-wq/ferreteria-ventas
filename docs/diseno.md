# Diseño del gestor de ventas de ferretería

Guía corta para implementar cada parte y explicarla en la sustentación. En el código, las partes que faltan
están marcadas con `// TODO (issue ...)` y explican paso a paso qué escribir. Cada patrón tiene al menos un
**ejemplo resuelto** para copiar la forma.

## Cómo encaja todo

```
Pantalla de reportes ──► ReporteFacade.generarReporte(solicitud)          (Facade)
                              │
                              ├─ RegistroPlantillas.obtener(tipo) → copia  (Prototype)
                              ├─ DirectorReporte + ReporteBuilder           (Builder)
                              │      arma Secciones, Párrafos, Tablas...    (Composite)
                              ├─ Sello, MarcaAgua, Firma envuelven el reporte (Decorator)
                              └─ datos de la tienda, IGV, formato           (Singleton ConfiguracionGlobal)
```

Capas del proyecto:

| Paquete | Qué hay |
|---|---|
| `dominio` | Las clases del negocio: `Producto`, `Venta`, `Pago`, `Dinero`. Sin Spring. |
| `aplicacion` | Servicios con los casos de uso: `ProductoService`, `VentaService`. |
| `infraestructura.persistencia` | Dónde se guardan los datos (en memoria). |
| `reportes` | Un subpaquete por patrón. |
| `web` | Controladores y formularios de las pantallas (ya terminados). |
| `configuracion` | `ConfiguracionGlobal` (Singleton) y los datos de ejemplo. |

## Reglas para todos

- Campos `private`, sin setters en las clases del dominio. Se valida en el constructor y en los métodos
  (lanzar `IllegalArgumentException` o una excepción del dominio con un mensaje claro).
- Los servicios y controladores dependen de **interfaces** (`ProductoRepositorio`, `ProductoService`...).
- Todo texto que vaya a un reporte pasa por `Html.escapar(...)`.
- Cada clase que completes lleva al menos 3 pruebas en `src/test` (mismo paquete).

---

## Singleton: `configuracion.ConfiguracionGlobal` (terminado)

- **Problema:** los datos de la tienda (razón social, RUC, IGV, moneda) se usan en todo el sistema y deben ser
  los mismos en todas partes.
- **Cómo:** constructor privado y un único punto de acceso, `ConfiguracionGlobal.getInstancia()`. Spring lo
  publica como un solo bean, así que también se puede inyectar.
- **Demo:** en *Configuración* se cambia el nombre de la tienda y aparece al instante en el menú, el ticket y
  el sello de los reportes.
- **Para decir:** "Hay una sola instancia en toda la aplicación; aunque 100 hilos la pidan a la vez, el test
  comprueba que todos reciben la misma."

## Prototype: `reportes.prototipo` (issue Prototype)

- **Problema:** los reportes diario, mensual y anual se parecen mucho; crearlos desde cero repetiría código.
- **Cómo:** se arma una plantilla base y las demás se obtienen **clonándola** (`copiar()`) y cambiando lo
  necesario. `RegistroPlantillas.obtener(tipo)` siempre entrega una copia, nunca el original.
- **Clave:** la copia es **profunda**: la lista de secciones se copia en una lista nueva, así que cambiar la
  copia no cambia el original. (Una copia superficial compartiría la lista: es lo que la rúbrica castiga.)
- **Pruebas mínimas:** agregar una sección a la copia no la agrega al original; `obtener` dos veces da
  objetos distintos; tipo sin plantilla lanza excepción.
- **Para decir:** "No usamos `new` para cada reporte: clonamos una plantilla base y la adaptamos."

## Builder: `reportes.builder` (issue Builder)

- **Problema:** un reporte tiene muchas partes (título, secciones, tablas, gráficos) y armarlo con un
  constructor gigante sería ilegible.
- **Cómo:** `ReporteBuilder` arma el reporte **paso a paso** con métodos encadenados:
  `ReporteBuilder.nuevo("Ventas").seccion("Resumen").parrafo("...").finSeccion().build()`.
  `DirectorReporte` decide **qué** pasos dar según las secciones de la plantilla.
- **Pruebas mínimas:** un reporte con secciones anidadas se arma bien; `finSeccion()` sin sección abierta
  lanza excepción; `build()` con secciones sin cerrar lanza excepción.
- **Para decir:** "El builder separa cómo se construye el reporte de cómo se ve: el mismo builder sirve para
  todos los tipos de reporte."

## Composite: `reportes.composite` (issue Composite)

- **Problema:** un reporte tiene secciones que contienen subsecciones, párrafos y tablas, con cualquier
  profundidad.
- **Cómo:** `Seccion`, `Parrafo`, `Tabla` y `Grafico` heredan de `ElementoReporte`. Una sección guarda una
  lista de `ElementoReporte`, así que puede contener cualquier pieza, incluida otra sección. Para mostrar el
  reporte se llama a `renderizarHtml()` en todas igual, sin preguntar qué tipo son.
- **Pruebas mínimas:** sección dentro de sección dentro de sección se muestra completa; `contarElementos()`
  cuenta bien; un texto con `<script>` sale escapado.
- **Para decir:** "Tratamos igual a una hoja y a una sección; por eso el anidamiento no tiene límite."

## Decorator: `reportes.decorador` (issue Decorator)

- **Problema:** a veces el reporte lleva marca de agua, sello o firma, y a veces no. Hacer una clase por cada
  combinación sería imposible de mantener.
- **Cómo:** cada adorno es una clase que **envuelve** un `Reporte` y le agrega algo a su HTML, sin modificar
  el original. Se pueden poner en capas: `new FirmaDigitalDecorador(new SelloFerreteriaDecorador(reporte, tienda))`.
- **Firma:** es una huella SHA-256 del contenido (firma **simulada**): si cambia un carácter, cambia la huella.
  No prueba quién firmó; para eso haría falta un certificado digital. Va siempre en la última capa.
- **Pruebas mínimas:** el sello aparece al final; la marca de agua al inicio; la huella tiene 64 caracteres y
  cambia si cambia el contenido.
- **Para decir:** "Agregamos funciones al reporte en tiempo de ejecución, sin tocar la clase del reporte."

## Facade: `reportes.fachada.ReporteFacade` (issue Facade)

- **Problema:** generar un reporte requiere plantillas, builder, secciones y decoradores; la pantalla no
  debería conocer todo eso.
- **Cómo:** un único método público, `generarReporte(SolicitudReporte)`, hace todo el proceso y devuelve el
  reporte listo. El controlador solo llama a ese método.
- **Para decir:** "La pantalla conoce una sola clase; si mañana cambia cómo se arma un reporte, la pantalla no
  se entera."

---

## Productos y ventas (dominio, POO y SOLID)

- `Producto`: valida sus datos y controla su stock (`descontarStock`, `reponerStock`). Sin setters.
- `Pago` es abstracta; `PagoEfectivo` (resuelto), `PagoTarjeta` y `PagoYape` validan cada una a su manera.
  **Polimorfismo:** la venta llama a `pago.validar(total)` sin saber qué tipo de pago es. Para agregar Plin
  basta con otra subclase (principio abierto/cerrado).
- `Venta`: calcula total, operación gravada e IGV (los precios incluyen IGV).
- `VentaServiceImpl.registrar`: primero revisa el stock de **todos** los productos y recién después descuenta.
  Si uno no alcanza, no se cambia ningún stock.

| Principio SOLID | Dónde se ve |
|---|---|
| S (una responsabilidad) | Cada decorador hace una sola cosa; `Producto` controla su stock; el servicio coordina |
| O (abierto/cerrado) | Nuevo pago = nueva subclase de `Pago`; nuevo adorno = nuevo decorador |
| L (sustitución) | Cualquier `Pago` o decorador se usa donde se espera la clase base |
| I (interfaces pequeñas) | `ConfiguracionGlobal` se entrega como `DatosTienda`, `FormatoRegional` o `ParametrosFiscales` según quién la use |
| D (inversión de dependencias) | Servicios y reportes dependen de interfaces (`ProductoRepositorio`, `ConsultaDatosReporte`) |

## UML (StarUML, carpeta `docs/uml/`)

Cada integrante dibuja el diagrama de clases de su parte. Además: diagrama de paquetes y diagrama de
secuencia de `generarReporte`.
