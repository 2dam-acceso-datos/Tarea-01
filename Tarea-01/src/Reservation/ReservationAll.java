package Reservation;

import javax.swing.*;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

import static Utils.Utils.*;
import static java.util.Arrays.stream;

/**
 * Gestiona la creación y explotación de archivos de reservas. Esta clase ofrece utilidades para
 * generar ficheros CSV, capturar información mediante cuadros de diálogo y realizar operaciones de
 * reporte o particionado por destino.
 */
public class ReservationAll {
    private final String fileName;
    private boolean has4Fields = false;

    /**
     * Crea una instancia asociada a un archivo de reservas que se inicializa con los campos
     * estándar (número de asiento, nombre y clase).
     *
     * @param fileName nombre del archivo que se gestionará.
     */
    public ReservationAll(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Constructor sin argumentos deshabilitado para evitar instancias incompletas.
     */
    public ReservationAll() {
        throw new UnsupportedOperationException("Introduce los campos necesarios y el nombre del archivo");
    }

    /**
     * Crea una instancia configurando explícitamente si se trabajará con un cuarto campo (destino).
     *
     * @param has4Fields {@code true} si el archivo gestionará también destinos.
     * @param fileName   nombre del archivo que se gestionará.
     */
    public ReservationAll(boolean has4Fields, String fileName) {
        this.has4Fields = has4Fields;
        this.fileName = fileName;
    }

    /**
     * Genera el archivo asociado a la instancia dentro del directorio de trabajo actual. Si el
     * archivo ya existe se reutiliza.
     *
     * @return el objeto {@link File} correspondiente al archivo de reservas.
     * @throws IOException si el archivo no puede crearse por permisos u otra incidencia de E/S.
     */
    public File createFile() throws IOException {
        validateInputs();

        String workingDir = System.getProperty("user.dir");
        File directory = new File(workingDir);

        return createFileInternal(directory, fileName);
    }

    /**
     * Crea físicamente un archivo en el directorio indicado. Se expone como método privado para
     * facilitar las pruebas unitarias y el reuso en otros contextos.
     *
     * @param directory directorio en el que se generará el archivo.
     * @param fileName  nombre del archivo a crear.
     * @return referencia al archivo solicitado.
     * @throws IOException si el archivo no puede crearse.
     */
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

    /**
     * Genera un archivo independiente por cada destino encontrado en el archivo maestro. Cada
     * fichero incluirá únicamente las reservas que coinciden con dicho destino.
     *
     * @param field campo que se utilizará para discriminar los destinos (habitualmente {@link ReservationFields#DESTINATION}).
     * @throws IOException si hay problemas al leer o escribir los archivos generados.
     */
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

    /**
     * Obtiene los destinos únicos presentes en el archivo gestionado y agrupa sus registros
     * asociados.
     *
     * @param field campo que se analizará para construir el índice.
     * @return mapa donde la clave es el destino y el valor la lista de reservas asociadas.
     * @throws IOException si ocurre un problema al leer el archivo maestro.
     */
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

    /**
     * Verifica que los parámetros mínimos para trabajar con archivos de reserva están presentes.
     * Lanza una excepción en caso contrario.
     */
    private void validateInputs() {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName no puede ser nulo o vacío.");
        }
    }

    /**
     * Escribe los encabezados proporcionados en el archivo de reservas. Solo se ejecuta si el
     * archivo está vacío para evitar duplicar información.
     *
     * @param file    archivo sobre el que se escribirán los encabezados.
     * @param headers lista de campos a registrar como cabecera.
     * @throws IOException si ocurre un problema de escritura.
     */
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

    /**
     * Solicita al usuario los datos de una reserva y los persiste en el archivo asociado a la
     * instancia. El método se apoya en validaciones de formato para cada campo y muestra mensajes de
     * confirmación o error según corresponda.
     */
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

    /**
     * Pregunta al usuario cuántas reservas desea crear y repite el proceso de captura tantas veces
     * como se haya indicado.
     */
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

    /**
     * Muestra un informe en consola con el contenido del archivo de reservas. Opcionalmente puede
     * recibir una clase para calcular estadísticas filtradas.
     *
     * @param reservationClass clase de reserva a filtrar (opcional).
     */
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

    /**
     * Muestra por consola las reservas almacenadas en el archivo correspondiente al país indicado.
     * Si el archivo no existe se informa de ello.
     *
     * @param country destino del que se desean listar las reservas.
     * @throws IOException si ocurre un problema al acceder al archivo.
     */
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

    /**
     * Genera el nombre de archivo estandarizado para un destino concreto.
     *
     * @param country destino a convertir en nombre de archivo.
     * @return nombre normalizado del archivo.
     */
    private static String buildFileName(Destinations country) {
        String normalized = country.name().toLowerCase();  // enum a minúsculas
        return "reservas_" + normalized + ".txt";
    }

    /**
     * Proporciona una representación legible del estado interno, útil para depuración.
     *
     * @return cadena con el nombre del archivo y si gestiona destinos.
     */
    @Override
    public String toString() {
        return "Reservation.ReservationAll{" +
                "fileName='" + fileName + '\'' +
                ", has4Fields=" + has4Fields +
                '}';
    }
}
