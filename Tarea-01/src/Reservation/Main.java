package Reservation;

import java.io.File;
import java.io.IOException;

import Utils.Utils;

/**
 * Punto de entrada de la aplicación de reservas. Mantiene el flujo original de ejercicios
 * solicitados para la práctica, con el fin de que pueda revisarse o reproducirse sin sorpresas.
 */
public class Main {

    /**
     * Ejecuta secuencialmente los tres ejercicios planteados en la práctica:
     * <ol>
     *     <li>Creación del archivo base de reservas (comentado para conservar la versión inicial).</li>
     *     <li>Generación del archivo maestro, distribución por destino y filtrado por clase.</li>
     *     <li>Validación de un archivo con datos erróneos utilizando las utilidades del proyecto.</li>
     * </ol>
     *
     * @param args argumentos de línea de comandos (no utilizados).
     * @throws IOException si se produce un problema al crear o manipular los archivos requeridos.
     */
    public static void main(String[] args) throws IOException {
//        ReservationAll reservePt1 = new ReservationAll("reservas.txt");
//        File file1 = reservePt1.createFile();  // guardar referencia al archivo creado
//        reservePt1.writeHeaders(
//                file1,
//                ReservationFields.SEAT_NUMBER,
//                ReservationFields.PASSENGER_NAME,
//                ReservationFields.CLASS
//        );
//        reservePt1.pickHowManyRegisters();
//        reservePt1.logguer();
//
        ReservationAll reservePt2 = new ReservationAll(true, "reservas_maestro.txt");
        File file2 = reservePt2.createFile();  // guardar referencia al archivo creado

        reservePt2.writeHeaders(
                file2,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS,
                ReservationFields.DESTINATION
        );
        reservePt2.pickHowManyRegisters();
        reservePt2.createandFillFileByDestination(ReservationFields.DESTINATION);
        reservePt2.logguer(ReservationClass.BUSINESS);
        ReservationAll.showReservationsByCountry(Destinations.TOKIO);

        ReservationAll reservePt3 = new ReservationAll(false, "reservas_maestro_con_errores.txt");
        File file3 = reservePt3.createFile();  // guardar referencia al archivo creado
        reservePt3.writeHeaders(
                file3,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS,
                ReservationFields.DESTINATION
        );
        // El fichero se le ha rellenado a mano con errores para probar la validación.

        Utils.processReservationFile(file3, 4);
    }
}
