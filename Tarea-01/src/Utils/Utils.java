package Utils;


import Reservation.ReservationFields;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                File validFile = new File("reservas_" + destination + ".txt");
                try (FileWriter fw = new FileWriter(validFile, true)) {
                    fw.write(String.join(",", row) + System.lineSeparator());
                }
            }
        }
    }

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
