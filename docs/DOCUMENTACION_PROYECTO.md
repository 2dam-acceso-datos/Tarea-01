# Documentación Completa del Gestor de Reservas Aéreas

## 1. Introducción
El objetivo del proyecto es ofrecer una herramienta ligera en Java para crear, validar y analizar
reservas aéreas almacenadas en archivos CSV. El sistema combina interactividad mediante
ventanas emergentes (Swing) con utilidades de procesamiento en lote para que tanto usuarios
operativos como desarrolladores puedan manipular la información de manera confiable.

Esta documentación centraliza todos los detalles relevantes del código, describe los flujos
principales y aporta guías para extender o mantener la solución en el futuro.

## 2. Arquitectura general
La solución se compone de dos paquetes principales:

- `Reservation`: alberga la lógica de dominio para crear archivos, capturar datos y generar
  reportes de reservas.
- `Utils`: contiene funciones auxiliares que brindan validaciones, formateo y procesamiento de
  archivos.

La estructura del proyecto en disco es la siguiente:

```text
Tarea-01/
├── CHANGELOG.md
├── CHANGELOG_EN.md
├── LICENSE
├── README.md
├── README_EN.md
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

## 3. Componentes principales
### 3.1 `Reservation.Main`
Es el punto de entrada del programa. El método `main` conserva el flujo original solicitado en la práctica y ejecuta secuencialmente tres ejercicios:

1. **Archivo base** (comentado) para crear un fichero simple con tres campos.
2. **Escenario maestro** que genera `reservas_maestro.txt`, captura información mediante Swing, crea archivos por destino y muestra un log filtrado por clase.
3. **Escenario de validación** que procesa `reservas_maestro_con_errores.txt` utilizando las utilidades de comprobación masiva.

La inclusión directa de estos pasos dentro de `main` permite reproducir los ejercicios sin necesidad de navegar por métodos auxiliares.

### 3.2 `Reservation.ReservationAll`
Clase responsable de:

- Crear archivos de reservas y escribir encabezados.
- Capturar datos de nuevas reservas mediante cuadros de diálogo (`writeReservation`).
- Preguntar al usuario cuántas reservas generar (`pickHowManyRegisters`).
- Mostrar informes en consola (`logguer`).
- Dividir el archivo maestro en subarchivos por destino (`createandFillFileByDestination`).
- Mostrar reservas específicas de un destino (`showReservationsByCountry`).

La clase admite dos modos:

- **Tres campos**: asiento, pasajero y clase.
- **Cuatro campos**: añade el destino, habilitando la segmentación por país.

### 3.3 `Reservation.ReservationFields`
Enumeración que define el orden y la semántica de los campos de un registro. Se utiliza en la
creación de encabezados, validaciones y operaciones de agrupamiento.

### 3.4 `Reservation.ReservationClass`
Enumera las clases de viaje disponibles (First, Business y Economy). Se emplea tanto en la
captura interactiva como en los reportes.

### 3.5 `Reservation.Destinations`
Catálogo predefinido de destinos soportados. Permite limitar la entrada de datos a una lista
controlada y evita inconsistencias al generar archivos por país.

### 3.6 `Utils.Utils`
Proporciona utilidades de apoyo:

- `capitalizeWords`: normaliza nombres y apellidos.
- `validateReservationRecords` y `validateField`: reglas de validación para cada campo.
- `processReservationFile`: lee un CSV existente, separa los registros válidos por destino y
  genera un log con los errores.
- Método privado `logError` para registrar incidencias con marca de tiempo.

## 4. Flujo de datos
### 4.1 Archivos involucrados
- `reservas_maestro.txt`: archivo principal con encabezados y todas las reservas capturadas.
- `reservas_<destino>.txt`: archivos secundarios generados automáticamente por destino.
- `reservas_maestro_con_errores.txt`: archivo preparado manualmente con datos de prueba para
  evaluar la validación.
- `registro_errores.log`: resultado de procesar archivos con errores; contiene la descripción
  detallada de cada incidencia.

### 4.2 Captura interactiva
1. El usuario ejecuta `java -cp out Reservation.Main`.
2. Se crea (o reutiliza) el archivo maestro y sus encabezados.
3. Aparece un cuadro de selección numérica para definir cuántas reservas ingresar.
4. Para cada reserva se solicitan asiento, nombre, clase y destino. Cada entrada se valida al
   momento y se muestra una notificación de éxito o error.
5. Una vez finalizada la captura, el sistema genera automáticamente los archivos por destino y
   muestra un log con un resumen.

### 4.3 Procesamiento por destino
`createandFillFileByDestination` agrupa las reservas del archivo maestro por país y genera
un archivo individual para cada uno. Cada fichero incluye los encabezados y los registros
correspondientes.

### 4.4 Validación en lote
`Utils.processReservationFile` permite revisar archivos externos o preparados con errores.
El método detecta inconsistencias (campos vacíos, formatos incorrectos, cantidad de columnas) y
las registra en `registro_errores.log`. Los registros válidos se guardan en archivos
`reservas_<destino>.txt` sobrescribiendo cualquier contenido previo para garantizar coherencia.

## 5. Guía de uso rápido
### 5.1 Requisitos
- JDK 17 o superior.
- Entorno gráfico disponible para ejecutar Swing.

### 5.2 Compilación
Desde la carpeta `Tarea-01/Tarea-01`:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

### 5.3 Ejecución de ejemplos
- Ejecución completa de la práctica:
  ```bash
  java -cp out Reservation.Main
  ```

### 5.4 Validación de archivos existentes
Asegúrate de colocar o editar `reservas_maestro_con_errores.txt` en la raíz del proyecto.
Después ejecuta:

```bash
java -cp out Reservation.Main
```

El bloque final de `main` detectará que el archivo contiene datos y lanzará el proceso
de validación.

## 6. Validaciones y manejo de errores
- **Número de asiento**: debe seguir el patrón `\d{1,3}[A-F]` (ejemplo: `12C`).
- **Nombre del pasajero**: admite letras, espacios y caracteres acentuados.
- **Clase**: se limita a `ECONOMY`, `BUSINESS` o `FIRST`.
- **Destino**: mínimo tres caracteres; se recomienda usar valores del enum `Destinations`.

Los mensajes de error se presentan tanto en los cuadros de diálogo como en consola durante el
procesamiento en lote. `processReservationFile` además genera un log con marca temporal.

## 7. Extensión y mantenimiento
- **Añadir nuevos destinos**: incorporar el nombre en `Destinations.java`. El resto del flujo los
  reconocerá automáticamente.
- **Cambiar el formato de archivos**: modificar los métodos `writeHeaders`, `writeReservation` y
  `processReservationFile` para ajustar el separador o los encabezados.
- **Automatizar la captura**: sustituir las llamadas a `JOptionPane` por lectura de consola o por
  integración con otras fuentes de datos.
- **Pruebas unitarias**: los métodos están estructurados para facilitar la inyección de dependencias
  y aislar la lógica. Por ejemplo, `createFileInternal` es `static` y puede probarse con directorios
  temporales.

## 8. Referencia rápida de clases y métodos
| Clase                         | Responsabilidad principal                                      | Métodos clave |
|------------------------------|----------------------------------------------------------------|---------------|
| `Reservation.Main`           | Orquesta los ejemplos de uso y coordina los escenarios         | `main` |
| `Reservation.ReservationAll` | Gestión integral de archivos de reservas                       | `createFile`, `writeHeaders`, `pickHowManyRegisters`, `logguer`, `createandFillFileByDestination`, `showReservationsByCountry` |
| `Reservation.ReservationFields` | Define el orden y tipo de cada columna                      | Uso en validaciones y encabezados |
| `Reservation.ReservationClass`  | Enum de clases disponibles                                  | Uso en capturas y filtros |
| `Reservation.Destinations`      | Catálogo de destinos permitidos                             | Uso en menús y nombres de archivo |
| `Utils.Utils`                   | Validaciones y utilidades de procesamiento                  | `capitalizeWords`, `validateField`, `processReservationFile` |

## 9. Buenas prácticas adoptadas
- JavaDoc detallado en todos los métodos para facilitar el mantenimiento.
- Uso de enumeraciones para evitar cadenas mágicas y errores de tipeo.
- Separación clara entre la lógica de dominio (`Reservation`) y las utilidades (`Utils`).
- Logs descriptivos y mensajes de consola amigables para la persona usuaria.

## 10. Preguntas frecuentes
**¿Necesito interfaz gráfica para ejecutar el proyecto?**
Sí. La captura de datos se realiza mediante `JOptionPane`, por lo que se requiere un entorno con soporte para Swing. Si necesitas automatizar la entrada, revisa la sección de extensiones para reemplazar los cuadros de diálogo.

**¿Dónde se guardan los archivos generados?**
En el mismo directorio donde se ejecuta la aplicación, habitualmente `Tarea-01/Tarea-01`.

**¿Cómo limpio los archivos creados durante las pruebas?**
Elimina los archivos `reservas_*.txt` y `registro_errores.log`. Se regenerarán según sea necesario.

**¿Puedo cambiar el idioma de los mensajes?**
Sí, basta con editar las cadenas de texto en las clases correspondientes. Se recomienda extraerlas
 a un archivo de propiedades si se planea soportar múltiples idiomas.

## 11. Próximos pasos sugeridos
- Agregar pruebas automatizadas que simulen la captura de datos sin dependencia de Swing.
- Incorporar persistencia en base de datos para entornos productivos.
- Internacionalizar los mensajes para soportar otros idiomas.
- Integrar una interfaz web o móvil como capa de presentación alternativa.

---

Esta documentación se mantendrá actualizada conforme evolucione el proyecto. Cualquier aporte o
sugerencia es bienvenido mediante issues o pull requests.
