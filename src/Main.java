import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        final Formatter x;
        try {
            x = new Formatter("FoSho.txt");
            System.out.println("You created a file called FoSho.txt");
        } catch (Exception e) {
            System.out.println("You got an error");
        }

        Files.write(Paths.get("FoSho.txt"), "Beni Oku: Onur".getBytes(StandardCharsets.UTF_8));


    }
}
