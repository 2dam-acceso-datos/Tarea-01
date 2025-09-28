import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReservationAll reservePt1 = new ReservationAll("reservas.txt");

        reservePt1.createFile();
        
        System.out.println(reservePt1);
    }
}
