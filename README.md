# Gestor de ventas de ferretería

Aplicación web hecha con Java y Spring Boot para una ferretería: registro de productos, ventas con efectivo, tarjeta o Yape, y reportes de ventas e inventario.

Proyecto del curso **Patrones de Diseño de Software** (IDAT). La tienda "Ferretería El Constructor S.A.C." y su RUC son inventados.

## Tecnologías

- Java 21
- Spring Boot 4.1.1 (Web MVC y Thymeleaf)
- Bootstrap 5 (copia local en `static/vendor`)
- Maven (incluido con `mvnw`)

Los datos se guardan **en memoria** (listas en Java), sin base de datos. Al arrancar se cargan 15 productos y 30 ventas de ejemplo.

## Cómo correrlo

Necesita el JDK 21 y VS Code (con Git instalado). No hace falta instalar Maven.

1. En VS Code: `Ctrl + Shift + P` → **Git: Clone** → pegar la dirección del repositorio y elegir una carpeta.
2. Abrir una terminal (`Ctrl + ñ`) y ejecutar:

```powershell
.\mvnw spring-boot:run
```

Si sale un error de `JAVA_HOME`, indicar dónde está Java antes de correrlo (la ruta puede variar según la versión instalada):

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"; .\mvnw spring-boot:run
```

3. Cuando aparezca `Started FerreteriaVentasApplication`, abrir http://localhost:8080

Si el puerto 8080 está ocupado, usar otro: `.\mvnw spring-boot:run "-Dspring-boot.run.arguments=--server.port=8082"` y abrir http://localhost:8082

## Estructura (MVC)

```
src/main/java/pe/idat/ferreteria
├── configuracion/     ConfiguracionGlobal (Singleton) y DatosIniciales
├── modelo/            Producto, Venta, LineaVenta, CategoriaProducto
│   └── pago/          Pago, PagoEfectivo, PagoTarjeta, PagoYape
├── repositorio/       ProductoRepositorio, VentaRepositorio (en memoria)
├── servicio/          ProductoService, VentaService
├── controlador/       controladores de cada pantalla y formularios
└── reportes/
    ├── modelo/        Reporte, ReporteBase, TipoReporte, Granularidad, SeccionPlantilla
    ├── prototipo/     Prototype: Prototipo, PlantillaReporte, RegistroPlantillas
    ├── composite/     Composite: ElementoReporte, Seccion, Parrafo, Tabla, Grafico
    ├── builder/       Builder: ReporteBuilder, DirectorReporte
    ├── datos/         DatosReporte
    ├── decorador/     Decorator: sello, marca de agua y firma
    └── fachada/       Facade: ReporteFacade
src/main/resources/templates   vistas (Thymeleaf)
```

- **Modelo:** las clases del negocio y sus reglas (por ejemplo, no vender más de lo que hay en stock).
- **Vista:** las páginas HTML en `templates`.
- **Controlador:** recibe lo que envía la página, llama al servicio y elige qué vista mostrar.

## Patrones de diseño

| Patrón | Tipo | Dónde | Para qué |
|---|---|---|---|
| Singleton | Creacional | `configuracion/ConfiguracionGlobal` | Una sola configuración de la tienda (razón social, RUC, IGV) para todo el sistema |
| Prototype | Creacional | `reportes/prototipo` | Las plantillas de reporte se clonan en vez de crearse desde cero |
| Builder | Creacional | `reportes/builder` | Armar el reporte paso a paso |
| Composite | Estructural | `reportes/composite` | Secciones que contienen párrafos, tablas, gráficos u otras secciones |
| Decorator | Estructural | `reportes/decorador` | Agregar sello, marca de agua y firma sin cambiar el reporte |
| Facade | Estructural | `reportes/fachada` | Un solo método para generar cualquier reporte |

## Pruebas

```bash
./mvnw test
```

Hay una prueba sencilla por patrón, una del cálculo de la venta y otra que verifica que la aplicación arranca.
