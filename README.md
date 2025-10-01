# Gestor de Reservas Aéreas

Este repositorio contiene una aplicación de consola escrita en Java que permite crear y gestionar un registro básico de reservas aéreas. El flujo principal se centra en la generación de un archivo CSV (`reservas.txt`), la recopilación de datos mediante cuadros de diálogo y la presentación de un resumen formateado con estadísticas.

## Características principales

- Creación de archivos de reservas con validación de nombre y permisos.
- Estructura configurable de campos mediante enumeraciones (`Reservation.ReservationFields`).
- Captura asistida de datos con interfaces emergentes (`JOptionPane`).
- Registro interactivo para determinar cuántas reservas capturar en una sesión.
- Visualización formateada de reservas en consola con encabezados y emojis.
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
3. Asegúrate de tener el JDK instalado y configurado en la variable de entorno `JAVA_HOME`.

## Ejecución

Desde la carpeta `Tarea-01/` que contiene el código fuente (es decir, `Tarea-01/Tarea-01` respecto al repositorio):

```bash
javac -d out src/*.java
java -cp out Reservation.Main
```

Al iniciar, la aplicación solicitará:

1. El número de registros a crear.
2. El número de asiento, nombre del pasajero y clase para cada reserva.
3. Opcionalmente, el destino si se inicializa `Reservation.ReservationAll` con el modo de cuatro campos.

El resultado se guarda en `reservas.txt` y se muestra un log formateado en la consola.

## Estructura del repositorio

```text
Tarea-01/
├── LICENSE
├── README.md
├── README_EN.md
├── CHANGELOG.md
├── CHANGELOG_EN.md
└── Tarea-01/
    └── src/
        ├── Reservation.Main.java
        ├── Reservation.ReservationAll.java
        ├── Reservation.ReservationClass.java
        └── Reservation.ReservationFields.java
```

## Flujo de trabajo recomendado

- **Ramas**: utiliza ramas de características (`feature/`), correcciones (`hotfix/`) y ramas de integración (`develop`, `main`).
- **Commits**: emplea mensajes descriptivos orientados a la acción, por ejemplo `Added: Soporte para destinos`.
- **Pull Requests**: describe claramente el alcance, pruebas realizadas y cualquier consideración adicional.

## Contribuir

1. Crea un fork del repositorio.
2. Genera una rama de trabajo descriptiva.
3. Implementa los cambios con pruebas locales.
4. Envía un pull request explicando el motivo y el impacto.

## Licencia

Este proyecto está disponible bajo la licencia MIT. Consulta el archivo [`LICENSE`](LICENSE) para más información.
