# ADR-002: Singleton clásico para ConfiguracionGlobal, publicado como bean

Estado: aceptada (2026-10-20)

## Contexto

La tienda tiene parámetros únicos (razón social, RUC, moneda, IGV, formato regional, umbral de stock) que leen las ventas, las pantallas y los reportes. El curso evalúa el patrón Singleton, y un bean de Spring con alcance singleton no muestra el patrón en el código: lo resuelve el contenedor.

## Decisión

- `ConfiguracionGlobal` implementa el Singleton con el *holder idiom*: la JVM crea la instancia la primera vez que se usa `getInstancia()`, de forma perezosa y segura entre hilos, sin `synchronized`.
- Clase `final`, constructor privado con una guarda que lanza `IllegalStateException` si se intenta crear otra instancia por reflexión, y no `Serializable`.
- El estado es un record inmutable `ParametrosTienda` dentro de un `AtomicReference`: `actualizar()` lo reemplaza de una vez y ningún hilo ve valores mezclados. El record se valida al construirse.
- Implementa tres interfaces pequeñas (`ParametrosFiscales`, `FormatoRegional`, `DatosTienda`); los consumidores dependen solo de la que necesitan.
- `ConfiguracionBeans` la publica con **un solo** `@Bean` que devuelve `getInstancia()`. Con un `@Bean` por interfaz, Spring compara por la instancia real, que implementa las tres, e inyectar cualquiera de ellas falla con `NoUniqueBeanDefinitionException` (comprobado). Con un solo bean, pedir cualquiera de las interfaces resuelve a la misma instancia.
- Solo `ConfiguracionController` depende de la clase concreta, porque es el único que modifica la configuración. El dominio no la usa: el servicio de ventas le pasa la tasa de IGV a `Venta`.

## Alternativas

- **Enum de un solo elemento**: también protege contra reflexión y serialización, pero no puede tener un constructor privado con lógica ni heredar comportamiento, y en la sustentación se ve menos el mecanismo del patrón.
- **Bean de Spring sin Singleton propio**: más simple, pero el patrón no aparece en el código.

## Consecuencias

- El estado sobrevive entre pruebas de la misma JVM: cada prueba que lo modifica lo restaura en `@AfterEach` con `ParametrosTienda.porDefecto()`.
- Los cambios hechos en `/configuracion` se pierden al reiniciar, igual que el resto de los datos (ADR-001).
