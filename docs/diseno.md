# Diseño del gestor de ventas

Explicación corta de cada patrón: qué problema resuelve, cómo está hecho y cómo mostrarlo en la aplicación.

## Cómo encaja todo

```
Pantalla Reportes ──► ReporteController ──► ReporteFacade.generarReporte(...)     (Facade)
                                               │
                                               ├─ RegistroPlantillas.obtener(tipo)  → copia      (Prototype)
                                               ├─ DirectorReporte usa ReporteBuilder              (Builder)
                                               │     y arma Secciones, Párrafos, Tablas, Gráficos (Composite)
                                               └─ Sello, MarcaAgua y Firma envuelven el reporte   (Decorator)

ConfiguracionGlobal.getInstancia() → datos de la tienda e IGV para menú, ticket y sello  (Singleton)
```

## Singleton: `configuracion/ConfiguracionGlobal`

- **Problema:** la razón social, el RUC y el IGV se usan en todo el sistema y tienen que ser los mismos en todas partes.
- **Cómo:** el constructor es privado y la única forma de obtener el objeto es `ConfiguracionGlobal.getInstancia()`. La primera vez lo crea (`if (instancia == null)`) y después devuelve siempre el mismo.
- **Dónde se usa:** en el menú y el pie de página, en el IGV de cada venta (`Venta.getOpGravada`) y en el sello de los reportes.
- **Demo:** en *Configuración* cambiar el nombre de la tienda: cambia al momento en el menú, en el ticket y en el sello del reporte.

## Prototype: `reportes/prototipo`

- **Problema:** los reportes del día, del mes y del año se parecen mucho; crearlos desde cero repetiría código.
- **Cómo:** `PlantillaReporte` tiene el método `copiar()`. En `RegistroPlantillas` se arma una plantilla base y las demás salen de clonarla y cambiarle el título, el rango de fechas o una sección.
- **Copia profunda:** al copiar se crea una lista nueva de secciones. Así, agregar una sección a la copia no la agrega al original.
- **Demo:** generar "Ventas del día" y "Ventas del mes": las dos salen de la misma plantilla base.

## Builder: `reportes/builder`

- **Problema:** un reporte tiene muchas partes (título, subtítulo, secciones, tablas, gráficos). Armarlo con un constructor enorme sería difícil de leer.
- **Cómo:** `ReporteBuilder` arma el reporte paso a paso con métodos encadenados:
  `new ReporteBuilder("Ventas").seccion("Resumen").parrafo("...").build()`.
  `DirectorReporte` decide qué pasos dar según las secciones que pide la plantilla.
- **Demo:** "Ventas del mes" tiene resumen, detalle y ventas por categoría; "Inventario valorizado" solo tiene la tabla de inventario. Es el mismo builder con distintos pasos.

## Composite: `reportes/composite`

- **Problema:** un reporte tiene secciones, y dentro de ellas párrafos, tablas, gráficos o incluso otras secciones.
- **Cómo:** `Seccion`, `Parrafo`, `Tabla` y `Grafico` heredan de `ElementoReporte`. `Seccion` guarda una lista de `ElementoReporte`, así que puede tener cualquier pieza dentro. Para mostrar el reporte se llama `renderizarHtml()` a cada pieza sin preguntar de qué tipo es.
- **Demo:** cada bloque del reporte (Resumen, Detalle, Ventas por categoría) es una `Seccion` con sus hojas dentro.

## Decorator: `reportes/decorador`

- **Problema:** a veces el reporte lleva sello, marca de agua o firma, y a veces no. Una clase por cada combinación serían 8 clases.
- **Cómo:** cada adorno es una clase que envuelve un `Reporte` y le agrega algo a su HTML sin modificarlo: `new FirmaDecorador(new SelloFerreteriaDecorador(reporte))`.
- **Demo:** generar el mismo reporte marcando y desmarcando las opciones de sello, marca de agua y firma.

## Facade: `reportes/fachada/ReporteFacade`

- **Problema:** generar un reporte usa plantillas, builder, secciones y decoradores; el controlador no debería conocer todo eso.
- **Cómo:** `generarReporte(...)` hace todo el proceso y devuelve el reporte listo. `ReporteController` solo llama a ese método.

## Productos y ventas

- `Producto` valida sus datos y controla su stock (`descontarStock`, `reponerStock`).
- `Pago` es una clase abstracta; `PagoEfectivo`, `PagoTarjeta` y `PagoYape` validan cada uno a su manera. La venta llama a `pago.validar(total)` sin saber qué tipo de pago es (polimorfismo).
- `VentaService.registrar` primero revisa el stock de todos los productos y recién después descuenta. Si uno no alcanza, no se cambia ningún stock.
- Los precios incluyen IGV: op. gravada = total / 1.18, e IGV = total − op. gravada.

## UML

Los diagramas van en `docs/uml/`: diagrama de clases de cada patrón y diagrama de paquetes.
