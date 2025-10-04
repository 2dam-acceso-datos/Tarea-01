package Reservation;

import java.io.File;
import java.io.IOException;

import Utils.Utils;

/**
 * Punto de entrada de la aplicación de reservas. El objetivo del método {@link #main(String[])} es
 * mostrar casos de uso autocontenidos que sirvan de guía rápida para comprender cómo interactúan
 * las distintas piezas del proyecto.
 */
public class Main {

    /**
     * Ejecuta ejemplos guiados del uso de la API de reservas. Para evitar que las demostraciones
     * sean invasivas, las operaciones interactivas (como el alta de reservas mediante cuadros de
     * diálogo) solo se ejecutan cuando se recibe el argumento {@code --interactive}.
     *
     * @param args argumentos de línea de comandos. Utilice {@code --interactive} para habilitar la captura manual.
     * @throws IOException si ocurre un problema al crear o manipular archivos de reservas.
     */
    public static void main(String[] args) throws IOException {
        boolean interactive = args != null && java.util.Arrays.stream(args)
                .anyMatch(arg -> "--interactive".equalsIgnoreCase(arg));

        runMasterReservationExample(interactive);
        runValidationExample();
    }

    /**
     * Demuestra cómo generar el archivo maestro, capturar información de reservas y explotar los
     * registros resultantes. Incluye el reparto automático en archivos por destino y una consulta
     * filtrada por país específico.
     *
     * @param interactive {@code true} para solicitar datos al usuario mediante Swing; de lo contrario
     *                    se omite la captura y se trabaja únicamente con la estructura del archivo.
     * @throws IOException si la creación del archivo maestro o la generación de archivos por destino falla.
     */
    private static void runMasterReservationExample(boolean interactive) throws IOException {
        ReservationAll masterReservation = new ReservationAll(true, "reservas_maestro.txt");
        File masterFile = masterReservation.createFile();

        masterReservation.writeHeaders(
                masterFile,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS,
                ReservationFields.DESTINATION
        );

        if (interactive) {
            masterReservation.pickHowManyRegisters();
        }

        masterReservation.createandFillFileByDestination(ReservationFields.DESTINATION);
        masterReservation.logguer(ReservationClass.BUSINESS);
        ReservationAll.showReservationsByCountry(Destinations.TOKIO);
    }

    /**
     * Ejemplo orientado a la validación de archivos preexistentes. El archivo
     * {@code reservas_maestro_con_errores.txt} debe contener registros deliberadamente incorrectos
     * para observar cómo la utilidad {@link Utils#processReservationFile(File, int)} registra y
     * clasifica los datos.
     *
     * @throws IOException si no se puede acceder al archivo de entrada o escribir los resultados.
     */
    private static void runValidationExample() throws IOException {
        ReservationAll erroneousReservations = new ReservationAll(false, "reservas_maestro_con_errores.txt");
        File errorFile = erroneousReservations.createFile();

        erroneousReservations.writeHeaders(
                errorFile,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS,
                ReservationFields.DESTINATION
        );

        if (errorFile.length() == 0) {
            System.out.println("⚠️ Añade algunos registros de prueba en 'reservas_maestro_con_errores.txt' para ver la validación en acción.");
            return;
        }

        Utils.processReservationFile(errorFile, 4);
    }
}
