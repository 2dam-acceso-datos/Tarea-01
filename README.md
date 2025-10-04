# Gestor de Reservas Aéreas

Este repositorio contiene una aplicación en Java que permite crear y gestionar un registro básico de reservas aéreas. El flujo principal se centra en la generación de archivos CSV, la recopilación de datos mediante cuadros de diálogo (`JOptionPane`) y la presentación de un resumen formateado con estadísticas.

## Características principales

- Creación de archivos de reservas con validación de nombre, permisos y estructura.
- Estructura configurable de campos mediante enumeraciones (`Reservation.ReservationFields`).
- Captura asistida de datos con interfaces emergentes (`JOptionPane`).
- Registro interactivo para determinar cuántas reservas capturar en una sesión.
- Visualización formateada de reservas en consola con encabezados, emojis y estadísticas filtradas.
- Ejecución opcional sin ventanas emergentes cuando se omite el argumento `--interactive`.
- Estadísticas resumidas, incluido el filtrado opcional por clase (`Reservation.ReservationClass`).

## Requisitos previos

- Java Development Kit (JDK) 17 o superior.
- Sistema operativo con soporte para la interfaz gráfica de Java (Swing).

## Configuración del proyecto

1. Clona el repositorio y entra en la carpeta principal:
   ```bash
   git clone <url-del-repositorio>
   cd Tarea-01
   ```
2. Accede al módulo con el código fuente:
   ```bash
   cd Tarea-01
   ```
3. Crea la carpeta de salida y compila los archivos Java:
   ```bash
   mkdir -p out
   javac -d out $(find src -name "*.java")
   ```
4. Asegúrate de tener el JDK instalado y configurado en la variable de entorno `JAVA_HOME`.

## Ejecución

Desde la carpeta `Tarea-01/` que contiene el código fuente (es decir, `Tarea-01/Tarea-01` respecto al repositorio):

```bash
java -cp out Reservation.Main
```

Para habilitar la captura interactiva con ventanas Swing añade el argumento `--interactive`:

```bash
java -cp out Reservation.Main --interactive
```

Al iniciar, la aplicación solicitará:

1. El número de registros a crear.
2. El número de asiento, nombre del pasajero y clase para cada reserva.
3. Opcionalmente, el destino si se inicializa `Reservation.ReservationAll` con el modo de cuatro campos.

Los resultados se guardan en `reservas_maestro.txt`, se crean archivos por destino y se muestra un log formateado en la consola. Si existe el archivo `reservas_maestro_con_errores.txt` se demostrará además el flujo de validación masiva.

## Estructura del repositorio

```text
Tarea-01/
├── LICENSE
├── README.md
├── README_EN.md
├── CHANGELOG.md
├── CHANGELOG_EN.md
├── docs/
│   └── DOCUMENTACION_PROYECTO.md
└── Tarea-01/
    └── src/
        ├── Reservation/
        │   ├── Destinations.java
        │   ├── Main.java
        │   ├── ReservationAll.java
        │   ├── ReservationClass.java
        │   └── ReservationFields.java
        └── Utils/
            └── Utils.java
```

## Flujo de trabajo recomendado

- **Ramas**: utiliza ramas de características (`feature/`), correcciones (`hotfix/`) y ramas de integración (`develop`, `main`).
- **Commits**: emplea mensajes descriptivos orientados a la acción, por ejemplo `Added: Soporte para destinos`.
- **Pull Requests**: describe claramente el alcance, pruebas realizadas y cualquier consideración adicional.

## Documentación ampliada

La guía completa del proyecto, con explicación detallada de flujos, validaciones y ejemplos, se encuentra en [`docs/DOCUMENTACION_PROYECTO.md`](docs/DOCUMENTACION_PROYECTO.md).

## Licencia

Este proyecto está disponible bajo la licencia MIT. Consulta el archivo [`LICENSE`](LICENSE) para más información.
