package Reservation;

import javax.swing.*;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

import static Utils.Utils.*;
import static java.util.Arrays.stream;

public class ReservationAll {
    private final String fileName;
    private boolean has4Fields = false;

    // ------------- CONSTRUCTORS ----------------
    public ReservationAll(String fileName) {
        this.fileName = fileName;
    }

    public ReservationAll() {
        throw new UnsupportedOperationException("Introduce los campos necesarios y el nombre del archivo");
    }

    public ReservationAll(boolean has4Fields, String fileName) {
        this.has4Fields = has4Fields;
        this.fileName = fileName;
    }

    //  ---------------- METHODS ----------------
    public File createFile() throws IOException {
        validateInputs();

        String workingDir = System.getProperty("user.dir");
        File directory = new File(workingDir);

        return createFileInternal(directory, fileName);
    }

    private static File createFileInternal(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);

        if (file.exists()) {
            return file;
        }

        try {
            if (file.createNewFile()) {
                return file;
            } else {
                throw new IOException("No se pudo crear el archivo.");
            }
        } catch (SecurityException e) {
            throw new AccessDeniedException("Permisos insuficientes: " + file.getAbsolutePath());
        }
    }

    public void createandFillFileByDestination(ReservationFields field) throws IOException {
        if (!has4Fields) {
            throw new IllegalStateException("La opción de crear archivos por destino requiere el destino.");
        }

        // Mapa destino -> reservas
        Map<String, List<String[]>> reservasPorDestino = getUniqueDestinationsWithRecords(field);
        if (reservasPorDestino.isEmpty()) {
            System.out.printf("No hay reservas para procesar con el destino %s.", field.name());
            return;
        }

        // Crear un archivo por cada destino
        for (Map.Entry<String, List<String[]>> entry : reservasPorDestino.entrySet()) {
            if (!validateReservationRecords(entry.getValue(), (has4Fields) ? 4 : 3)) {
                return; // si hay error, salimos
            }

            String dest = entry.getKey();
            List<String[]> reservas = entry.getValue();

            String cleanDest = dest.toLowerCase().replaceAll("\\s+", "_");
            ReservationAll aux = new ReservationAll(true, "reservas_" + cleanDest + ".txt");
            File auxFile = aux.createFile();

            // Abrimos en modo overwrite para limpiar el archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(auxFile, false))) {
                // Escribir encabezados
                writer.write("SEAT_NUMBER, PASSENGER_NAME, CLASS, DESTINATION");
                writer.newLine();

                // Escribir reservas de ese destino
                for (String[] reserva : reservas) {
                    writer.write(String.join(", ", reserva));
                    writer.newLine();
                }
            }

            System.out.println("Archivo generado para destino: " + dest + " -> " + auxFile.getAbsolutePath());
        }
    }

    private Map<String, List<String[]>> getUniqueDestinationsWithRecords(ReservationFields field) throws IOException {
        // Mapa destino -> reservas
        Map<String, List<String[]>> reservasPorDestino = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // CSV esperado: asiento,nombre,clase,destino

                if (isHeader) {
                    isHeader = false; // saltamos encabezado
                    continue;
                }

                String destino = switch (field) {
                    case SEAT_NUMBER -> (!parts[1].trim().isEmpty()) ? parts[1].trim() : null;
                    case PASSENGER_NAME -> (!parts[1].trim().isEmpty()) ? parts[1].trim() : null;
                    case CLASS -> (!parts[2].trim().isEmpty()) ? parts[2].trim() : null;
                    case DESTINATION -> (!parts[3].trim().isEmpty())
                            ? parts[3].trim() : null;
                };

                reservasPorDestino.computeIfAbsent(destino, k -> new ArrayList<>()).add(parts);
            }

            return reservasPorDestino;
        }
    }

    private void validateInputs() {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName no puede ser nulo o vacío.");
        }
    }

    // Método para escribir encabezados
    public void writeHeaders(File file, ReservationFields... headers) throws IOException {
        boolean fileExists = file.exists() && file.length() > 0;

        // Solo escribir encabezados si el archivo está vacío
        if (!fileExists) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                for (int i = 0; i < headers.length; i++) {
                    writer.write(headers[i].name());
                    if (i < headers.length - 1) {
                        writer.write(", ");
                    }
                }
                writer.newLine();
            }
        }
    }

    public void writeReservation() {
        String error;
        String reservationDataSeat;
        String reservationDataName;
        boolean itsOkey;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Opciones para la columna de clases de la reserva.
            String[] clases = {
                    String.valueOf(ReservationClass.ECONOMY),
                    String.valueOf(ReservationClass.BUSINESS),
                    String.valueOf(ReservationClass.FIRST)
            };



            // Bucle hasta que el usuario ingrese algo válido
            do {
                itsOkey = false;
                reservationDataSeat = JOptionPane.showInputDialog("Ingrese el número de asiento").trim();
                error = validateField(reservationDataSeat, ReservationFields.SEAT_NUMBER);
                if (error != null) {
                    JOptionPane.showMessageDialog(null,
                            error,
                            "Error de validación",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // OK
                    itsOkey = true;
                    JOptionPane.showMessageDialog(null,
                            "✅ Dato válido: " + reservationDataSeat,
                            "Correcto",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } while (!itsOkey);

            do {
                itsOkey = false;
                reservationDataName = JOptionPane.showInputDialog("Ingrese el nombre del pasajero (máx 32 carácteres)").trim();
                error = validateField(reservationDataName, ReservationFields.PASSENGER_NAME);

                if (error != null) {
                    JOptionPane.showMessageDialog(null,
                            error,
                            "Error de validación",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // OK
                    itsOkey = true;
                    JOptionPane.showMessageDialog(null,
                            "✅ Dato válido: " + reservationDataName,
                            "Correcto",
                            JOptionPane.INFORMATION_MESSAGE);
                    reservationDataName = capitalizeWords(reservationDataName);
                }
            } while (!itsOkey);

            String seleccion = (String) JOptionPane.showInputDialog(
                    null,   // no hay ventana padre
                    "Seleccione la clase:", // mensaje
                    "Clase de Reserva", // título
                    JOptionPane.QUESTION_MESSAGE, // tipo con icono de pregunta
                    null,   // sin icono personalizado
                    clases, // opciones del combo
                    clases[0]   // opción seleccionada por defecto
            );
            String reservationData = reservationDataSeat + ", " + reservationDataName + ", " + seleccion;

            if (has4Fields) {
                String reservationDataDestination = (String) JOptionPane.showInputDialog(
                        null,
                        "Ingrese el destino del vuelo",
                        "Destino",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        stream(Destinations.values()).map(Enum::name).toArray(),
                        stream(Destinations.values()).map(Enum::name).toArray()[0]
                );
                reservationData += ", " + reservationDataDestination;
            }

            writer.write(reservationData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pickHowManyRegisters() {
        SpinnerNumberModel model = new SpinnerNumberModel(
                1,   // valor inicial
                1,   // mínimo
                100, // máximo
                1    // incremento
        );

        JSpinner spinner = new JSpinner(model);

        int option = JOptionPane.showOptionDialog(
                null,
                spinner,
                "Seleccione el número de registros a crear",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.OK_OPTION) {
            int cantidad = (Integer) spinner.getValue();
            for (int i = 0; i < cantidad; i++) {
                writeReservation();
            }
        } else {
            System.out.println("Operación cancelada por el usuario.");
        }
    }

    public void logguer(ReservationClass... reservationClass) {
        List<String[]> reservas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // CSV esperado: asiento,nombre,clase,(destino opcional)
                String[] parts = line.split(",");
                reservas.add(parts);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("❌ No se encontró el archivo de reservas. Verifica la ruta o si existe.", e);
        } catch (IOException e) {
            throw new RuntimeException("💥 Error de entrada/salida al procesar el archivo de reservas.", e);
        }

        // Encabezado bonito
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║          📖 RESERVAS LOG             ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        // Detalle
        System.out.println("➡ Detalle de reservas:");
        System.out.println("─────────────────────────────");

        for (int i = 1; i < reservas.size(); i++) {
            String[] r = reservas.get(i);

            String asiento = r[0].trim();
            String pasajero = r[1].trim();
            String clase = r[2].trim();

            if (has4Fields) {
                String destino = (r.length > 3 && !r[3].trim().isEmpty()) ? r[3].trim() : "N/A";
                System.out.printf(
                        "#%d | 🪑 Asiento: %-5s 👤 Pasajero: %-15s 🎟 Clase: %-10s 🌍 Destino: %s%n",
                        (i), asiento, pasajero, clase, destino
                );
            } else {
                System.out.printf(
                        "#%d | 🪑 Asiento: %-5s 👤 Pasajero: %-15s 🎟 Clase: %-10s%n",
                        (i), asiento, pasajero, clase
                );
            }
        }

        // Estadísticas
        System.out.println("\n📊 Estadísticas");
        System.out.println("─────────────────────────────");
        System.out.printf("✔ Total de reservas            : %d%n", reservas.size() - 1);

        // Si hay claseFiltro, contamos esa clase
        if (reservationClass.length > 0) {
            String claseBuscada = reservationClass[0].name().toLowerCase();
            long count = reservas.stream()
                    .skip(1) // saltamos encabezado
                    .filter(r -> r.length > 2 && claseBuscada.equalsIgnoreCase(r[2].trim()))
                    .count();

            System.out.printf("✔ Pasajeros en %s        : %d%n", claseBuscada, count);
        }

        // Final
        System.out.println("\n🎯 Proceso completado con éxito");
    }

    // Método estático que recibe un enum de país
    public static void showReservationsByCountry(Destinations country) throws IOException {
        // 1. Construir nombre del archivo
        String fileName = buildFileName(country);

        // 2. Crear referencia al archivo (sin crearlo)
        File file = new File(fileName);

        // 3. Verificar si existe
        if (!file.exists() || !file.isFile()) {
            System.out.println("❌ No existe el archivo: " + fileName);
            return; // Salir del método
        }

        // 4. Si existe, mostrar contenido
        ReservationAll reservationsByCountry = new ReservationAll(fileName);
        reservationsByCountry.logguer();
    }

    // Método auxiliar: arma el nombre con prefijo "reservas_"
    private static String buildFileName(Destinations country) {
        String normalized = country.name().toLowerCase();  // enum a minúsculas
        return "reservas_" + normalized + ".txt";
    }

    @Override
    public String toString() {
        return "Reservation.ReservationAll{" +
                "fileName='" + fileName + '\'' +
                ", has4Fields=" + has4Fields +
                '}';
    }
}