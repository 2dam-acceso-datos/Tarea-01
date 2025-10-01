# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/) y este proyecto intenta adherirse a [Semantic Versioning](https://semver.org/lang/es/).

## [Unreleased]

## [1.5.0] - 2025-10-01
### Cambiado
- Refactorización del código para mejorar la legibilidad y el mantenimiento.
- Uso de funciones para modularizar la lógica de creación y escritura de archivos.
- Mejora en la gestión de errores y validaciones de entrada.

## [1.4.0] - 2025-10-01
### Añadido
- Crea ficheros en base a los destinos de cada reserva `reservas_<destino>.txt`.
- Copia los registros de reservas al fichero correspondiente según el destino.

## [1.3.0] - 2025-09-30
### Añadido
- Implementación de registros en el fichero `reservas_maestro.txt`.
- Máximo de carácteres en los campos.
- Mejor control de errores.

## [1.2.0] - 2025-09-30
### Añadido
- Encabezados en el fichero `reservas_maestro.txt`.

## [1.1.0] - 2025-09-30
### Añadido
- Implementar la creación del fichero `reservas_maestro.txt`.

## [1.0.0] - 2025-09-29
### Añadido
- Registro en consola con formato enriquecido y estadísticas agregadas para todas las reservas.
- Filtro opcional por clase de reserva al generar el log.

### Corregido
- Ajuste en la presentación de las reservas para evitar desalineaciones en la salida de consola.

## [0.2.0] - 2025-09-29
### Añadido
- Cuadro de diálogo con control numérico para definir cuántas reservas capturar en una sesión.
- Comentarios aclaratorios en el código para facilitar el mantenimiento.

### Eliminado
- Importaciones innecesarias detectadas durante la revisión.

## [0.1.0] - 2025-09-28
### Añadido
- Enumeraciones de campos y clases de reserva para estandarizar la estructura del archivo.
- Creación validada de archivos de salida con encabezados configurables.
- Captura asistida de datos de pasajeros y escritura en `reservas.txt`.
- Documentación inicial y configuración de `.gitignore`.

> Nota: Genera etiquetas (tags) en el repositorio para activar los enlaces de comparación entre versiones.
