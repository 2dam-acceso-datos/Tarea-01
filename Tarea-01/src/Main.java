import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReservationAll reservePt1 = new ReservationAll("reservas.txt");
        File file = reservePt1.createFile();  // guardar referencia al archivo creado

        System.out.println(reservePt1);

        ReservationAll.writeHeaders(
                file,
                ReservationFields.SEAT_NUMBER,
                ReservationFields.PASSENGER_NAME,
                ReservationFields.CLASS
        );
    }
}