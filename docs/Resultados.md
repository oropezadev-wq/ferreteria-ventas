# Resultados de pruebas de usabilidad

**Proyecto:** Sistema de Gestión de Ventas para Ferretería  
**Aplicación:** Ferretería El Constructor S.A.C.  
**Responsable:** @luisjose123  
**Issue:** #11  
**Archivo:** `docs/usabilidad/resultados.md`

---

## 1. Objetivo

El presente documento tiene como finalidad organizar los resultados de las pruebas de usabilidad del Sistema de Gestión de Ventas para Ferretería, considerando cinco actividades principales: registrar productos, realizar ventas, consultar tickets, generar reportes y modificar la configuración de la tienda.

Se contemplan tres perfiles de usuario con diferentes niveles de experiencia para identificar posibles dificultades de navegación, comprensión de la información y facilidad de uso.

Este documento reúne observaciones y propuestas de mejora elaboradas a partir de las pantallas disponibles. Las observaciones deberán contrastarse con las pruebas de usabilidad realizadas.

## 2. Actividades evaluadas

Las actividades consideradas son las siguientes:

1. Registrar un producto nuevo en el inventario.
2. Registrar una venta utilizando Yape como método de pago.
3. Consultar el ticket de una venta registrada.
4. Generar un reporte mensual de ventas.
5. Modificar los datos de configuración de la ferretería.

---

## 3. Participante 1 — Usuario con poca experiencia en sistemas de ventas

**Perfil:** Persona que utiliza aplicaciones básicas, pero no está acostumbrada a administrar inventarios ni registrar operaciones comerciales.

### Prueba 1. Registrar un producto

**Resultado previsto:** Tarea completada con algunas dudas.

**Observación para validar:**

El participante ingresó a la sección Productos y reconoció que la información estaba organizada en un listado con códigos, nombres, categorías, precios y cantidades disponibles.

Durante la revisión del inventario, le llamó la atención que algunos artículos mostraran una advertencia amarilla de stock bajo, como la cinta teflón y la brocha de 3 pulgadas.

Aunque comprendió que estas advertencias indicaban pocas unidades disponibles, no identificó inmediatamente si debía editar el producto o utilizar la opción Reponer para aumentar las existencias.

**Mejora sugerida:**

Agregar una explicación breve junto a las alertas de stock bajo, indicando que el producto necesita reposición. También sería útil diferenciar visualmente las acciones de edición y reposición.

### Prueba 2. Registrar una venta con Yape

**Resultado previsto:** Tarea completada con dudas.

**Observación para validar:**

El participante logró identificar el procedimiento general para registrar una venta, seleccionar productos, establecer cantidades y elegir Yape como método de pago.

Sin embargo, al tratarse de una operación comercial, manifestó preocupación por confirmar si los productos y cantidades seleccionados eran correctos antes de registrar definitivamente la venta.

Consideró que sería conveniente visualizar un resumen final con los artículos, las cantidades y el importe total antes de confirmar la operación.

**Mejora sugerida:**

Incorporar una confirmación previa al registro de la venta, mostrando los productos seleccionados, las cantidades y el total que deberá pagar el cliente.

### Prueba 3. Consultar un ticket

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante accedió al listado de ventas y encontró la opción Ver ticket.

En el comprobante observó el nombre de la ferretería, la fecha, el número de venta, los productos adquiridos, las cantidades, los precios unitarios y el total.

También identificó que el ticket indicaba el pago mediante Yape y que se trataba de un documento interno, no de un comprobante de pago.

Aunque la información estaba ordenada, consideró que algunos textos, especialmente los importes y las indicaciones finales, podrían mostrarse con mayor tamaño para facilitar su lectura.

**Mejora sugerida:**

Mejorar la jerarquía visual del ticket, resaltando el número de venta, el método de pago y el importe total.

### Prueba 4. Generar un reporte mensual

**Resultado previsto:** Tarea completada con orientación.

**Observación para validar:**

El participante ingresó a Reportes y seleccionó la opción Ventas del mes.

Al generar el documento, encontró un resumen de ventas, un detalle de operaciones y una sección de ventas por categoría.

La información le permitió identificar qué categorías habían registrado mayores importes. No obstante, consideró que el gráfico de barras podría acompañarse de una explicación más sencilla para personas que no están acostumbradas a interpretar reportes comerciales.

**Mejora sugerida:**

Incorporar un pequeño resumen automático que destaque la categoría con mayores ventas y los principales resultados del período.

### Prueba 5. Modificar la configuración de la tienda

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante identificó los campos destinados a la razón social, RUC, dirección, teléfono e IGV.

Consideró que estos datos eran importantes porque aparecen en distintas partes del sistema, como los tickets y reportes.

Sin embargo, manifestó que sería conveniente advertir al usuario cuando se intenta modificar información importante de la empresa, especialmente el RUC o el porcentaje de IGV.

**Mejora sugerida:**

Agregar una confirmación para cambios importantes de configuración y diferenciar los datos generales de los parámetros tributarios.

### Conclusión del participante 1

Este perfil permite explorar principalmente la claridad de las instrucciones y la seguridad que transmite el sistema al realizar operaciones.

Las mejoras prioritarias para este tipo de usuario serían las explicaciones de stock, las confirmaciones antes de registrar ventas y una presentación más clara de los resultados comerciales.

---

## 4. Participante 2 — Usuario con experiencia en ventas

**Perfil:** Persona familiarizada con la atención al cliente, la búsqueda de artículos y el registro de operaciones comerciales.

### Prueba 1. Registrar un producto

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante comprendió la organización del inventario y consideró útil que cada artículo contara con un código identificador.

Observó que el listado incluía productos de distintas categorías, como Herramientas, Construcción, Electricidad, Gasfitería y Pinturas.

Desde su perspectiva, el principal aspecto por mejorar no sería el registro, sino la rapidez con la que se pueden localizar artículos cuando el inventario aumenta considerablemente.

También señaló que sería útil ordenar los productos según su cantidad disponible para identificar cuáles requieren atención primero.

**Mejora sugerida:**

Permitir ordenar el inventario por stock disponible e incorporar un filtro específico para mostrar productos con existencias bajas.

### Prueba 2. Registrar una venta con Yape

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante comprendió la selección de productos y el procedimiento de registro mediante Yape.

Al evaluar la operación desde el punto de vista de una persona que atiende clientes, consideró importante reducir el tiempo necesario para registrar varias unidades de un mismo artículo.

También sugirió que el sistema permita verificar claramente el importe total antes de registrar el pago, especialmente cuando el cliente compra productos de diferentes categorías.

**Mejora sugerida:**

Mostrar un total actualizado durante el registro de la venta y destacar los datos esenciales para confirmar el pago.

### Prueba 3. Consultar un ticket

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante revisó el listado de ventas, donde las operaciones aparecen identificadas con números consecutivos, fecha, cantidad de productos y total.

Consideró útil que cada operación tenga su propio número de ticket, porque facilita la identificación de una venta específica.

Sin embargo, observó que, cuando existen numerosas operaciones, puede resultar complicado ubicar una venta antigua únicamente desplazándose por el listado.

**Mejora sugerida:**

Incorporar filtros por rango de fechas y número de ticket para agilizar la atención de consultas posteriores a la venta.

### Prueba 4. Generar un reporte mensual

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante revisó el reporte mensual y consideró útil que presentara tanto el número de operaciones como el importe total vendido.

En el reporte mensual del sistema correspondiente a octubre de 2026 se muestran 17 ventas registradas y un total de S/ 2041.80.

También identificó la distribución por categorías, donde Electricidad presenta un importe superior al de las demás categorías mostradas.

Consideró que esta información podría utilizarse para tomar decisiones comerciales, aunque sería más útil si pudiera compararse con períodos anteriores.

**Mejora sugerida:**

Agregar una comparación opcional entre dos meses, mostrando la diferencia de ventas en soles y en porcentaje.

### Prueba 5. Modificar la configuración de la tienda

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante comprendió que la configuración permite mantener actualizados los datos de identificación del negocio.

Consideró importante que los cambios realizados se reflejen correctamente en los tickets posteriores, especialmente cuando se modifica la dirección o el teléfono.

Desde su experiencia comercial, señaló que sería conveniente disponer de una vista previa de cómo aparecerán los datos de la ferretería antes de confirmar los cambios.

**Mejora sugerida:**

Incorporar una vista previa sencilla del encabezado del ticket dentro de la sección Configuración.

### Conclusión del participante 2

Este perfil se enfoca en la eficiencia operativa y la rapidez de atención al cliente.

Las principales oportunidades de mejora serían el ordenamiento del inventario, la consulta rápida de ventas anteriores y la comparación de resultados comerciales entre períodos.

---

## 5. Participante 3 — Usuario familiarizado con aplicaciones web

**Perfil:** Persona que utiliza aplicaciones informáticas con frecuencia y presta atención a la organización de la información, los controles y la navegación.

### Prueba 1. Registrar un producto

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante revisó la presentación del inventario y consideró adecuada la separación de la información en columnas.

También identificó que los códigos de los productos siguen una estructura relacionada con sus categorías, como HER para herramientas, CON para construcción y ELE para electricidad.

Desde su perspectiva, sería conveniente reforzar la validación de los datos al registrar o modificar artículos para evitar errores como precios negativos, códigos repetidos o cantidades inválidas.

**Mejora sugerida:**

Incorporar mensajes de validación específicos en el registro y edición de productos, indicando el campo que debe corregirse.

### Prueba 2. Registrar una venta con Yape

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante comprendió el proceso general de venta y prestó especial atención a la información relacionada con el pago.

Consideró importante que el sistema diferencie claramente el registro de una operación mediante Yape de la verificación efectiva del pago recibido.

También señaló que, después de registrar una venta, sería útil mostrar una confirmación visible con el número de operación generado.

**Mejora sugerida:**

Mostrar una confirmación clara del registro, con el número de venta y un acceso directo al ticket correspondiente. Además, evitar mensajes que sugieran que el pago fue verificado automáticamente si el sistema solo registra los datos ingresados.

### Prueba 3. Consultar un ticket

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante revisó los tickets disponibles y observó que cada documento incluye los importes correspondientes a la operación gravada, IGV y total.

También identificó que algunos tickets contienen artículos diferentes, mientras que otros pueden mostrar el mismo producto en más de una línea.

Consideró que, para facilitar la revisión, sería útil mantener una presentación uniforme de los importes y permitir distinguir claramente cada operación.

Asimismo, valoró que se indique expresamente que el ticket interno no constituye un comprobante de pago.

**Mejora sugerida:**

Incorporar una opción para copiar el número de ticket y mejorar la presentación uniforme de cantidades, precios e importes.

### Prueba 4. Generar un reporte mensual

**Resultado previsto:** Tarea completada con observaciones.

**Observación para validar:**

El participante revisó la sección Reportes y observó que el sistema permite seleccionar un tipo de reporte, una fecha y una marca de agua opcional.

También identificó las casillas para incluir el sello de la ferretería y la firma del administrador.

En el documento generado, consideró positiva la separación entre el resumen, el detalle de ventas y el gráfico por categorías.

Sin embargo, señaló que el formulario podría mostrar una indicación más clara sobre el efecto de activar o desactivar el sello y la firma antes de generar el reporte.

**Mejora sugerida:**

Incorporar una vista previa de los elementos opcionales del reporte y mostrar una breve descripción de cada opción.

### Prueba 5. Modificar la configuración de la tienda

**Resultado previsto:** Tarea completada.

**Observación para validar:**

El participante revisó los campos de configuración y consideró que la información estaba agrupada de manera comprensible.

No obstante, desde una perspectiva de uso continuo, señaló que sería importante informar si existen modificaciones pendientes de guardar.

También consideró conveniente advertir cuando el usuario intenta abandonar la pantalla después de editar datos sin confirmar los cambios.

**Mejora sugerida:**

Mostrar una advertencia cuando existan cambios sin guardar y distinguir claramente entre información modificada y configuración guardada.

### Conclusión del participante 3

Este perfil permite analizar la claridad de los mensajes, la validación de datos y la prevención de errores durante el uso del sistema.

Las mejoras más relevantes serían reforzar los mensajes de confirmación, prevenir la pérdida accidental de información y facilitar la revisión de los documentos antes de imprimirlos.

---

## 6. Análisis comparativo de los tres perfiles

Los tres perfiles permiten analizar el sistema desde perspectivas diferentes.

El primer participante representa a una persona que necesita instrucciones claras y confirmaciones antes de realizar operaciones importantes. Sus observaciones se centran en la comprensión de los controles, las alertas y la información presentada.

El segundo participante representa a un usuario orientado a las ventas y a la atención al cliente. Sus principales intereses son la rapidez de las operaciones, la búsqueda de información y el aprovechamiento de los reportes comerciales.

El tercer participante representa a un usuario acostumbrado a utilizar aplicaciones web. Su evaluación se concentra en la prevención de errores, la validación de los datos y la claridad de los mensajes del sistema.

Estas diferencias permiten organizar mejoras de usabilidad que atiendan distintas necesidades sin modificar innecesariamente la estructura principal de la aplicación.

## 7. Propuestas generales de mejora

A partir de los escenarios analizados, se plantean las siguientes recomendaciones:

1. **Gestión de productos:** incorporar filtros de stock bajo, ordenamiento del inventario y mensajes claros de validación.
2. **Registro de ventas:** mostrar el total durante la operación, incorporar una confirmación previa y presentar el número de venta generado.
3. **Consulta de tickets:** permitir búsquedas por número y fecha, mejorar la legibilidad de los importes y facilitar la identificación de operaciones.
4. **Generación de reportes:** incorporar comparaciones entre períodos, explicaciones de las opciones disponibles y una vista previa del documento.
5. **Configuración de la tienda:** incluir confirmaciones para cambios importantes, advertencias sobre modificaciones sin guardar y una vista previa de los datos que aparecerán en los tickets.

Estas propuestas deben considerarse recomendaciones de diseño y no funcionalidades ya implementadas.
