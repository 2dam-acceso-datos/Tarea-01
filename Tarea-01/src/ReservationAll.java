import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.AccessDeniedException;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Opciones para la columna de clases de la reserva.
            String[] clases = {"Económica", "Ejecutiva", "Primera Clase"};

            String reservationDataSeat;
            String reservationDataName;

            // Bucle hasta que el usuario ingrese algo válido
            do {
                reservationDataSeat = JOptionPane.showInputDialog("Ingrese el número de asiento");
            } while (reservationDataSeat == null || reservationDataSeat.trim().isEmpty());

            do {
                reservationDataName = JOptionPane.showInputDialog("Ingrese el nombre del pasajero");
            } while (reservationDataName == null || reservationDataName.trim().isEmpty());

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
                String reservationDataDestination = JOptionPane.showInputDialog("Ingrese el destino del vuelo");
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


    @Override
    public String toString() {
        return "ReservationAll{fileName='" + fileName + "'}";
    }
}