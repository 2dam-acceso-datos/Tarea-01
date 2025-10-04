package Utils;

import Reservation.ReservationFields;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utilidades de propósito general que complementan el flujo principal de la aplicación. Incluye
 * operaciones de formato de texto, validación de registros y procesamiento masivo de archivos de
 * reservas.
 */
public class Utils {

    /**
     * Capitaliza cada palabra de la cadena recibida preservando los espacios simples entre ellas.
     *
     * @param text texto a transformar.
     * @return el texto con cada palabra iniciando en mayúscula.
     */
    public static String capitalizeWords(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }
        String[] words = text.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Valida una colección de registros verificando tanto la cantidad de campos como las reglas
     * específicas asociadas a cada columna.
     *
     * @param records             registros a revisar.
     * @param expectedFieldsCount número de columnas esperadas por registro.
     * @return {@code true} si todos los registros cumplen con las reglas de validación.
     */
    public static boolean validateReservationRecords(List<String[]> records, int expectedFieldsCount) {
        ReservationFields[] reservationFields = ReservationFields.values();

        if (records == null || records.isEmpty()) {
            System.out.println("❌ La lista de registros está vacía.");
            return false;
        }

        for (int rowIndex = 0; rowIndex < records.size(); rowIndex++) {
            String[] row = records.get(rowIndex);

            // Comprobar cantidad de columnas
            if (row.length != expectedFieldsCount) {
                System.out.printf("❌ Error en registro #%d: se esperaban %d campos, pero hay %d.%n",
                        rowIndex + 1, expectedFieldsCount, row.length);
                return false;
            }

            // Validar cada campo contra su ReservationField
            for (int colIndex = 0; colIndex < expectedFieldsCount; colIndex++) {
                String fieldValue = row[colIndex].trim();
                ReservationFields fieldType = reservationFields[colIndex];

                String error = validateField(fieldValue, fieldType);
                if (error != null) {
                    System.out.printf("❌ Error en registro #%d, campo %s: %s%n",
                            rowIndex + 1, fieldType.name(), error);
                    return false;
                }
            }
        }

        return true; // todo pasó la validación
    }


    /**
     * Aplica reglas de validación específicas según el tipo de campo indicado.
     *
     * @param field            valor a validar.
     * @param reservationField tipo de campo que determina la validación.
     * @return {@code null} si el valor es válido o un mensaje descriptivo del error.
     */
    public static String validateField(String field, ReservationFields reservationField) {
        if (field == null || field.trim().isEmpty()) {
            return "⚠️ El campo " + reservationField.name() + " no puede estar vacío.";
        }

        field = field.trim();

        return switch (reservationField) {
            case SEAT_NUMBER -> {
                if (!field.matches("\\d{1,3}[A-F]")) {
                    yield "❌ El número de asiento debe ser de 1 a 3 dígitos seguidos de una letra A-F (ej: 12C).";
                }
                yield null; // sin error
            }

            case PASSENGER_NAME -> {
                if (!field.matches("[A-Za-zÁÉÍÓÚÑáéíóúñ\\s]+")) {
                    yield "❌ El nombre solo puede contener letras y espacios.";
                }
                yield null;
            }

            case CLASS -> {
                if (!field.matches("ECONOMY|BUSINESS|FIRST")) {
                    yield "❌ La clase debe ser ECONOMY, BUSINESS o FIRST.";
                }
                yield null;
            }

            case DESTINATION -> {
                if (field.length() < 3) {
                    yield "❌ El destino debe tener al menos 3 caracteres.";
                }
                yield null;
            }
        };
    }

    /**
     * Procesa un archivo de reservas existente, separando los registros válidos por destino y
     * registrando en un log los fallos detectados.
     *
     * @param inputFile           archivo CSV que contiene las reservas a evaluar.
     * @param expectedFieldsCount número de columnas que cada registro debería poseer.
     * @throws IOException si ocurre un problema al leer el archivo o al escribir los resultados.
     */
    public static void processReservationFile(
            File inputFile,
            int expectedFieldsCount
    ) throws IOException {

        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line.split(","));
            }
        }

        File errorLog = new File("registro_errores.log");
        if (errorLog.exists()) errorLog.delete(); // limpiar logs previos
        Map<String, List<String[]>> validByDestination = new LinkedHashMap<>();

        for (String[] row : records) {
            if (row.length != expectedFieldsCount) {
                logError(errorLog, row, "Número incorrecto de campos (esperados " + expectedFieldsCount + ")");
                continue;
            }

            boolean valid = true;
            String destination = "";

            for (int i = 0; i < expectedFieldsCount; i++) {
                String field = row[i].trim();
                ReservationFields type = ReservationFields.values()[i];
                String error = validateField(field, type);

                if (error != null) {
                    logError(errorLog, row, error);
                    valid = false;
                    break;
                }

                if (type == ReservationFields.DESTINATION) destination = field;
            }

            if (valid) {
                validByDestination
                        .computeIfAbsent(destination, k -> new ArrayList<>())
                        .add(row);
            }
        }

        // Escribir los ficheros de salida
        for (Map.Entry<String, List<String[]>> entry : validByDestination.entrySet()) {
            String destination = entry.getKey().toLowerCase();
            List<String[]> validRecords = entry.getValue();
            File outputFile = new File("reservas_" + destination + ".txt");

            try (FileWriter fw = new FileWriter(outputFile, false)) {
                for (String[] row : validRecords) {
                    fw.write(String.join(",", row) + System.lineSeparator());
                }
            }
        }

        // Mostrar resumen
        System.out.println("\n📦 Resumen de archivos creados:");
        if (validByDestination.isEmpty()) {
            System.out.println("⚠ No se generaron archivos de reservas válidas.");
        } else {
            for (Map.Entry<String, List<String[]>> entry : validByDestination.entrySet()) {
                String destination = entry.getKey();
                List<String[]> validRecords = entry.getValue();
                System.out.println("\nArchivo: reservas_" + destination + ".txt");
                for (String[] row : validRecords) {
                    System.out.println("   - " + String.join(", ", row));
                }
            }

            System.out.println("\n📊 Registros válidos por archivo:");
            for (Map.Entry<String, List<String[]>> entry : validByDestination.entrySet()) {
                System.out.println(" - reservas_" + entry.getKey() + ".txt: " + entry.getValue().size());
            }

            int totalValidos = validByDestination.values().stream()
                    .mapToInt(List::size)
                    .sum();

            System.out.println("\n✅ Total de registros válidos: " + totalValidos);
        }

        // Mostrar contenido del log de errores
        if (errorLog.exists()) {
            System.out.println("\n📋 Contenido de 'registro_errores.log':");
            try (BufferedReader br = new BufferedReader(new FileReader(errorLog))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } else {
            System.out.println("\n✅ No se encontraron errores.");
        }
    }


    /**
     * Registra un mensaje de error junto con el registro problemático en un archivo de log.
     *
     * @param logFile     archivo donde se almacenará el error.
     * @param row         registro que provocó la incidencia.
     * @param description detalle del error encontrado.
     */
    private static void logError(File logFile, String[] row, String description) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            String timestamp = LocalDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write(timestamp + ", " + String.join(",", row) + ", " + description + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("❌ Error escribiendo en el log: " + e.getMessage());
        }
    }
}
