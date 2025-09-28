import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;

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
    /**
     * Crea un archivo en el directorio actual del programa (user.dir).
     */
    public File createFile() throws IOException {
        validateInputs(fileName);

        String workingDir = System.getProperty("user.dir"); // ruta del programa
        File directory = new File(workingDir);

        return createFileInternal(directory, fileName);
    }

    /**
     * Lógica central para crear archivos.
     */
    private static File createFileInternal(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);

        if (file.exists()) {
            throw new FileAlreadyExistsException("El archivo ya existe: " + file.getAbsolutePath());
        }

        try {
            if (file.createNewFile()) {
                return file;
            } else {
                throw new IOException("No se pudo crear el archivo por una razón desconocida.");
            }
        } catch (SecurityException e) {
            throw new AccessDeniedException("Permisos insuficientes para crear el archivo: " + file.getAbsolutePath());
        }
    }

    /**
     * Valida los parámetros de entrada.
     */
    private static void validateInputs(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El parámetro fileName no puede ser nulo o vacío.");
        }
    }
}