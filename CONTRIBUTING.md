# Cómo trabajar en este repositorio

Guía para el equipo. Si es tu primera vez trabajando con git en grupo, sigue los pasos en orden.

## Reglas de oro

1. **Nadie sube cambios directo a `main`.** Todo entra por Pull Request (PR).
2. **Una rama por tarea**, con su issue asignado. El issue dice qué hacer y cuándo está terminado.
3. **Antes de abrir el PR, las pruebas pasan**: `mvnw verify` en verde.
4. **Cada PR lo revisa otra persona** del equipo antes de integrarlo.
5. **Las piezas comunes no se cambian sin avisar en el grupo**: `dominio/comun`, `reportes/modelo`, `reportes/datos`, interfaces de `reportes/*` y `configuracion`. Otros dependen de ellas.
6. Cada uno hace commits **con su propia cuenta**: así el historial muestra el aporte de cada integrante.

## 1. Preparar el entorno (una sola vez)

Programas que hay que instalar:

| Programa | Dónde | Nota |
|---|---|---|
| JDK 21 (Temurin) | https://adoptium.net | En el instalador marca **"Set JAVA_HOME variable"** y **"Add to PATH"**. |
| IntelliJ IDEA Community | https://www.jetbrains.com/idea/download | Gratis. |
| Git | https://git-scm.com | Opciones por defecto. |

**Maven no se instala.** Los archivos `mvnw` (Linux/Mac) y `mvnw.cmd` (Windows) son el *Maven Wrapper*: la
primera vez descargan solos la versión correcta de Maven. Así todo el equipo usa la misma.

Para comprobar que Java quedó bien, en una terminal nueva: `java -version` debe decir `21`. Si dice que no
encuentra el comando, el JDK se instaló sin las dos opciones de arriba: vuelve a correr el instalador y
márcalas (o ejecuta las pruebas desde IntelliJ, que tiene su propia configuración de Java).

Configura tu identidad en Git con **tu** cuenta de GitHub:

```bash
git config --global user.name "tu-usuario-de-github"
git config --global user.email "el-correo-de-tu-cuenta@ejemplo.com"
```

- Acepta la invitación al repositorio (llega por correo o en https://github.com/notifications).
- Clona el repo y ábrelo en IntelliJ (*File → Open* sobre la carpeta):

```bash
git clone https://github.com/oropezadev-wq/ferreteria-ventas.git
cd ferreteria-ventas
./mvnw verify          # en Windows (cmd/PowerShell): mvnw.cmd verify
```

Para correr la app: ejecutar `FerreteriaApplication` en IntelliJ y abrir http://localhost:8080.

## 2. Qué tengo que hacer

1. Abre tu issue (pestaña **Issues**, filtra por tu usuario).
2. En IntelliJ, abre la vista **TODO** (menú *View → Tool Windows → TODO*): lista todos los `// TODO (issue ...)`
   del código. Los tuyos dicen el nombre de tu issue y explican paso a paso qué escribir.
3. Mira el **ejemplo resuelto** de tu parte (`Parrafo`, `MarcaAguaDecorador` o `PagoEfectivo`) y sigue la
   misma forma.
4. Reemplaza el `throw new UnsupportedOperationException(...)` por tu código.
5. Escribe tus pruebas en `src/test/java` (mismo paquete que tu clase). Para correrlas en IntelliJ: clic
   derecho sobre la clase de prueba → *Run*.
6. El diseño de cada patrón y qué decir en la sustentación está en `docs/diseno.md`.

## 3. Empezar una tarea

Siempre desde un `main` actualizado:

```bash
git checkout main
git pull
git checkout -b nombre-de-la-tarea     # ej.: patron-composite, productos-inventario
```

## 4. Guardar avances (commits)

Commits pequeños y frecuentes. Formato del mensaje:

```
tipo(área): descripción en minúscula, en español
```

Tipos: `feat` (funcionalidad), `fix` (corrección), `test` (pruebas), `docs` (documentación), `refactor`, `style`, `chore`.

Ejemplos:

```
feat(producto): agrega producto con control de stock
test(composite): verifica anidamiento de 50 niveles
docs(uml): agrega diagrama de clases del Prototype
```

```bash
git add .
git commit -m "feat(producto): agrega producto con control de stock"
```

## 5. Subir la rama y abrir el Pull Request

```bash
./mvnw verify                       # debe terminar en BUILD SUCCESS
git push -u origin nombre-de-la-tarea
```

En GitHub aparece el botón **Compare & pull request**. En la descripción:

- `Closes #N` (el número de tu issue), para que se cierre solo al integrar.
- Qué hiciste y cómo probarlo.

El CI corre las pruebas automáticamente en el PR. Si sale en rojo, revisa el detalle, corrige y vuelve a hacer `push` a la misma rama.

## 6. Revisar el PR de un compañero

En la pestaña *Files changed*: ¿se entiende?, ¿tiene pruebas?, ¿respeta el diseño (`docs/diseno.md`)? Comenta o aprueba con *Review changes → Approve*.

## 7. Si `main` avanzó mientras trabajabas

```bash
git checkout main
git pull
git checkout nombre-de-la-tarea
git merge main
```

Si aparece un **conflicto**, no borres código ajeno para que compile: avisa en el grupo y se resuelve juntos.

## Dónde está cada cosa

- Diseño completo (patrones, clases, pruebas): `docs/diseno.md`
- Decisiones de arquitectura: `docs/decisiones/`
- Diagramas UML (StarUML): `docs/uml/`
- Tareas, responsables y fechas: pestaña **Issues** del repositorio
