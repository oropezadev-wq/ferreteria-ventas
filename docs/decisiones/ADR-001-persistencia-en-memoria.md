# ADR-001: Persistencia en memoria

Estado: aceptada (2026-10-20)

## Contexto

El objetivo del proyecto es mostrar patrones de diseño y un dominio orientado a objetos. JPA exige constructor vacío y setters o acceso por reflexión a los atributos, lo que choca con entidades inmutables en su identidad (id por constructor, sin `setId()`), y complica la clonación profunda que necesita el patrón Prototype.

## Decisión

- Los servicios dependen de la interfaz `Repositorio<T>`; la implementación es `RepositorioEnMemoria<T>`, con un `ConcurrentHashMap` y una secuencia `AtomicLong`.
- Como el id llega por constructor, quien crea una entidad reserva antes su id con `siguienteId()`.
- Los ids nunca se reutilizan; si se guarda una entidad con un id mayor al último reservado, la secuencia avanza.
- Los datos de ejemplo se cargan al arrancar.

## Consecuencias

- Los datos se pierden al reiniciar la aplicación; es aceptable para una demo académica.
- Las pruebas no necesitan base de datos y son rápidas.
- Cambiar a una base de datos real implica escribir otra implementación de los repositorios, sin tocar dominio ni servicios.
- No hay transacciones: las operaciones "todo o nada" (por ejemplo, descontar stock de varias líneas) se resuelven en el servicio validando antes de modificar.
