package Reservation;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReservationAll reservePt1 = new ReservationAll("reservas.txt");
        File file1 = reservePt1.createFile();  // guardar referencia al archivo creado
        reservePt1.writeHeaders(
                file1,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS
        );
        reservePt1.pickHowManyRegisters();
        reservePt1.logguer();

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
        reservePt2.logguer(Reservation.ReservationClass.BUSINESS);

        ReservationAll.showReservationsByCountry(Destinations.TOKIO);

    }
}