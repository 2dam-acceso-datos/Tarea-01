import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReservationAll reservePt1 = new ReservationAll("reservas.txt");
        File file = reservePt1.createFile();  // guardar referencia al archivo creado

        reservePt1.writeHeaders(
                file,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS
        );

        reservePt1.pickHowManyRegisters();
//        reservePt1.logguer();
        reservePt1.logguer(ReservationClass.BUSINESS);

        ReservationAll reservaPt2 = new ReservationAll(true, "reservas_maestro.txt");
    }
}