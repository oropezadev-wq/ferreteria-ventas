# Guion de pruebas de usabilidad

Responsable: @luisjose123 · Issue #11 · Aplicación: Gestor de ventas de ferretería

## Estado del guion

Se completaron las cinco pruebas individuales de usabilidad: registro de productos, venta con Yape, visualización del ticket, generación del reporte mensual y configuración de la tienda. A continuación se documentan las tareas, las observaciones y las mejoras propuestas.

## Objetivo

Evaluar la facilidad de uso de las cinco funciones principales de la aplicación, identificar aspectos claros de la interfaz y proponer mejoras a partir de las pruebas individuales realizadas.

## Preparación

1. Ejecutar la aplicación de ferretería en el entorno local.
2. Utilizar datos de ejemplo para las operaciones de prueba.
3. Revisar cada función y documentar el resultado observado.

## Datos de prueba

Se utilizan productos y datos de ejemplo de una ferretería, sin datos personales reales.

## Tareas

### Tarea 1. Registrar un producto

- **Qué se le pide:** registrar un producto nuevo con código, nombre, categoría, precio y stock.
- **Camino esperado:** Productos → Nuevo producto → completar los campos → Guardar → verificar el producto en la lista.
- **Se logró si:** aparece el mensaje «Producto registrado.» y el producto figura en la lista con sus datos correctos.
- **Datos utilizados:** código HER-0004; producto Taladro eléctrico 650 W; categoría Herramientas; precio S/ 159.90; stock 15.
- **Resultado de mi prueba individual:** tarea completada correctamente, sin ayuda.
- **Lo que observé en mi prueba:**
  - El botón «Nuevo producto» está ubicado en un lugar visible y se encuentra fácilmente.
  - El formulario es claro: permite identificar dónde ingresar el código, nombre, categoría, precio y stock.
  - El registro es sencillo y, al guardar, aparece el mensaje «Producto registrado.»; el producto nuevo figura en la lista con sus datos correctos.
  - En la tabla de productos, el campo numérico situado entre «Editar» y «Reponer» no tiene una etiqueta que explique que corresponde a la cantidad de unidades por reponer. Esto podría confundir a una persona que usa el sistema por primera vez.
  - Para reponer un artículo es necesario localizarlo desplazándose manualmente por la lista de productos. Cuando el inventario es extenso, encontrar un producto agotado o con poco stock puede resultar tedioso.
- **Mejoras propuestas:**
  1. Añadir una etiqueta clara, como «Cantidad a reponer», al campo numérico situado junto al botón «Reponer».
  2. Incorporar un buscador por nombre o código para localizar rápidamente el producto que necesita reposición, sin recorrer toda la lista.
- **Conclusión:** el registro de productos resultó fácil e intuitivo en esta prueba individual. Para mejorar la gestión del inventario, se recomienda identificar claramente la cantidad a reponer y facilitar la búsqueda de productos.

### Tarea 2. Registrar una venta con Yape

- **Qué se le pide:** registrar una venta de productos de ferretería utilizando Yape como método de pago.
- **Camino esperado:** Nueva venta → seleccionar producto y cantidad → elegir Yape → ingresar número de celular → Registrar venta → revisar la confirmación y el ticket.
- **Se logró si:** la venta se registra y se muestra un ticket con el detalle de la operación y el método de pago Yape.
- **Resultado de mi prueba individual:** tarea completada satisfactoriamente.
- **Lo que observé en mi prueba:**
  - El formulario de nueva venta es fácil de comprender porque los campos de producto y cantidad están claramente identificados.
  - La selección de productos mediante una lista desplegable es sencilla cuando hay pocos artículos. Sin embargo, con un catálogo amplio puede resultar difícil localizar un producto específico.
  - Los métodos de pago se presentan en una lista desplegable fácil de identificar. Al seleccionar Yape, pude ingresar el número de celular y registrar la venta con un clic.
  - Los campos «Efectivo recibido (S/)» y «Tarjeta: últimos 4» siguen siendo visibles en el formulario de pago con Yape; sería más claro que se deshabilitaran cuando no correspondan al método seleccionado.
  - Tras el registro, se visualizó un ticket interno con datos de la tienda, número de ticket, fecha y hora, productos, cantidades, precios unitarios, subtotales, IGV, total y referencia del pago con Yape.
  - Durante la prueba agregué intencionalmente el mismo producto en dos líneas de la venta; su repetición en el ticket corresponde a esa acción y no a un error.
- **Mejoras propuestas:**
  1. Incorporar un buscador por código o nombre del producto para agilizar la selección en inventarios grandes.
  2. Deshabilitar u ocultar los campos de efectivo y tarjeta cuando el método de pago sea Yape, dejando habilitados solo los datos pertinentes.
- **Conclusión:** el proceso de venta con Yape resultó sencillo en esta prueba individual. Las mejoras principales se relacionan con la búsqueda de productos y la claridad de los campos según el método de pago.

### Tarea 3. Visualizar el ticket de venta

- **Qué se le pide:** localizar una venta registrada y consultar su ticket.
- **Camino esperado:** Ventas → identificar la venta → Ver ticket → revisar los datos de la operación.
- **Se logró si:** el ticket se abre y permite identificar claramente la venta y sus importes.
- **Resultado de mi prueba individual:** tarea completada satisfactoriamente.
- **Lo que observé en mi prueba:**
  - En la sección «Ventas», el botón «Ver ticket» se identifica fácilmente y permite acceder directamente al ticket correspondiente.
  - La información está presentada de forma clara y ordenada: datos de la ferretería, número de ticket, fecha, hora, productos, cantidades, precios unitarios, subtotales, IGV y total.
  - El ticket también muestra la referencia del pago realizado con Yape.
- **Mejoras propuestas:**
  1. Incorporar un buscador por número de ticket para localizar una venta sin recorrer toda la lista.
  2. Agregar un filtro por fecha o período para consultar tickets anteriores con mayor facilidad.
  3. Ofrecer opciones para imprimir el ticket o descargarlo en PDF, manteniendo la indicación de que es un ticket interno y no un comprobante de pago.
  4. Identificar el medio de pago con una etiqueta más explícita, por ejemplo, «Método de pago: Yape», seguida del número de celular correspondiente.
- **Conclusión:** consultar el ticket resulta sencillo y la información es comprensible. Las mejoras propuestas facilitarían la búsqueda, consulta y conservación de los registros de venta.

### Tarea 4. Generar el reporte mensual

- **Qué se le pide:** generar un reporte de ventas del mes y revisar sus resultados.
- **Camino esperado:** Reportes → seleccionar «Ventas del mes» → elegir fecha → configurar opciones del reporte → Generar reporte → revisar los resultados.
- **Se logró si:** se genera el reporte del mes seleccionado y se pueden consultar los datos de las ventas.
- **Resultado de mi prueba individual:** tarea completada satisfactoriamente, con oportunidades de mejora en la claridad del formulario.
- **Lo que observé en mi prueba:**
  - La sección permite seleccionar distintos tipos de reporte, como ventas del día, del mes, del año e inventario valorizado.
  - El campo «Fecha» puede resultar confuso para una persona sin experiencia, porque no explica si la fecha elegida corresponde a un solo día, a todo el mes o a otro período de consulta.
  - La opción «Marca de agua (opcional)» no explica su finalidad, por lo que algunos usuarios podrían no comprender para qué sirve.
  - Una vez generado, el reporte presenta la información de forma clara y organizada: resumen de ventas, importe total, detalle por fecha, número de venta, producto, cantidad y subtotal, además de ventas por categoría y un gráfico comparativo.
  - El botón «Imprimir / guardar PDF» es visible y facilita conservar o imprimir el reporte.
- **Mejoras propuestas:**
  1. Mostrar de manera explícita el período que abarcará el reporte antes de generarlo, por ejemplo: «Del 01/10/2026 al 31/10/2026» para ventas del mes.
  2. Añadir una explicación breve junto a «Marca de agua», por ejemplo: «Texto que aparecerá de fondo en el reporte, como CONFIDENCIAL o COPIA».
  3. Adaptar la selección de fecha al tipo de reporte: día para ventas diarias, mes y año para ventas mensuales, y año para ventas anuales; aclarar el criterio temporal aplicable al inventario valorizado.
  4. Incorporar una vista previa que permita verificar el tipo de reporte y el período seleccionado antes de imprimirlo o guardarlo.
- **Conclusión:** la generación del reporte mensual resultó sencilla y la información obtenida es comprensible. Se recomienda mejorar las indicaciones del formulario para que los usuarios identifiquen con claridad qué período están consultando y para qué sirve cada opción.

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
