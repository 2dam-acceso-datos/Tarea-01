import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class ReservationAll {
    private String fileName;

    // ------------- CONSTRUCTORS ----------------
    public ReservationAll(String fileName) {
        this.fileName = fileName;
    }

    public ReservationAll() {
        throw new UnsupportedOperationException("Introduce los campos necesarios y el nombre del archivo");
    }

    //  ---------------- METHODS ----------------
    public File createFile() throws IOException {
        validateInputs(fileName);

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

    private static void validateInputs(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName no puede ser nulo o vacío.");
        }
    }

    // Método para escribir encabezados
    public static void writeHeaders(File file, ReservationFields... headers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < headers.length; i++) {
                writer.write(headers[i].name());
                if (i < headers.length - 1) {
                    writer.write(", ");
                }
            }
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return "ReservationAll{fileName='" + fileName + "'}";
    }
}