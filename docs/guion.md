# Guion de pruebas de usabilidad

Responsable: @luisjose123 · Issue #11 · Aplicación: Gestor de ventas de ferretería

## Estado del guion

Este guion es una primera versión. Por ahora solo se hizo una prueba, y todo lo demás se hará cuando la
aplicación esté lista para usar, es decir, cuando mis compañeros integren sus partes.

**Lo que ya se hizo**

- Se armó la base del guion: objetivo, preparación, lo que se le dice a la persona, qué se anota y las
  preguntas finales.
- Se hizo una prueba propia de la tarea 5 (cambiar los datos de la tienda), que es la única pantalla que
  funciona hoy. Esa prueba la hice yo solo, no con las 3 personas.

**Lo que falta hacer**

- Probar y escribir las otras tareas del issue: registrar un producto, registrar una venta con Yape, ver el
  ticket y generar el reporte del mes.
- Hacer las pruebas con las 3 personas, con todas las tareas.
- Pasar lo observado a `resultados.md`, con los problemas encontrados, las mejoras propuestas y las capturas.

## Objetivo

Ver si una persona que nunca usó la aplicación puede hacer las 5 tareas principales sin ayuda, y en qué
parte se confunde. Lo que se evalúa es la aplicación, no a la persona.

## Preparación

1. Uso la última versión de `main`, con las partes de todos integradas.
2. Pruebo con 3 personas que no hayan participado en el proyecto.
3. Reinicio la aplicación antes de cada persona. Como el sistema no usa base de datos y guarda todo en
   memoria, al reiniciar los datos vuelven a los iniciales y todas las personas empiezan igual.
4. Tengo a la mano este guion y `resultados.md` para anotar. Si hay capturas, las guardo en
   `docs/usabilidad/capturas/`.

## Qué le digo a la persona antes de empezar

>“Estoy probando una aplicación para una ferretería. Quiero ver si es fácil de usar. Mientras la usas, ve diciendo en voz alta lo que piensas. Yo no te voy a ayudar, así veo dónde se complica. ¿Puedo anotar lo que pase y tomar capturas de pantalla?”

## Datos de prueba

Uso datos de ejemplo de una ferretería ("Ferretería Valentina"), no datos personales.

## Tareas

Por ahora está solo la tarea 5, que es la que ya probé. Las demás tareas del issue se agregan cuando la
aplicación esté completa.

### Tarea 5. Cambiar los datos de la tienda

- **Qué se le pide:** actualizar los datos de la tienda (razón social, dirección y teléfono).
- **Camino esperado:** Configuración → cambiar los campos → Guardar.
- **Se logró si:** aparece el mensaje "Configuración guardada." y el nombre nuevo se ve en la barra superior.
- **Lo que observé en mi prueba:**
  - Cambié la razón social, la dirección y el teléfono por los de una ferretería de ejemplo, y los datos se
    guardaron correctamente.
  - Me pareció fácil de usar.
  - El nombre de la barra superior cambió al nuevo. En el pie de página también aparecieron la razón social,
    el RUC, la dirección y el teléfono nuevos.
  - El sistema guardó el nombre tal como lo escribí, en minúsculas y sin corregirlo.
  - El mensaje "Configuración guardada." se queda en la pantalla y no desaparece solo: se va cuando se
    cambia de pantalla.
  - Al reiniciar la aplicación, la configuración vuelve a como estaba al inicio, porque no hay base de
    datos y todo se guarda en memoria.

## Qué anoto en cada tarea

- Si la persona lo logró sola, con ayuda o no lo logró.
- Cuánto se demoró.
- Los errores que tuvo (clics equivocados, mensajes de error).
- Lo que dijo, con sus palabras.

## Preguntas finales

1. Del 1 (muy difícil) al 5 (muy fácil), ¿qué tan fácil te pareció usar la aplicación?
2. ¿Qué fue lo más confuso?
3. ¿Qué fue lo que estuvo más claro?
4. ¿Qué cambiarías primero?
