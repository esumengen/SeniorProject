import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.*;

public class Main {
    static ArrayList<Land> lands = new ArrayList<>();
    static ArrayList<Location> locations = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        int landCount_horizontal_max = 5;
        int landCount_vertical = 5;
        final int landCount_horizontal_min = landCount_horizontal_max - (int)Math.floor(landCount_vertical / 2);
        int landIndex = 0;
        int locationIndex = 0;

        for(int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int)Math.floor(landCount_vertical / 2));
            for(int j = 0; j < landCount_horizontal; j++) {
                Land land  = new Land(landIndex++, LandType.values()[new Random().nextInt(LandType.values().length)]);
                lands.add(land);

                int repeatCount = 0;
                while (repeatCount < 3 - ((j != 0) ? 1 : 0)) {
                    Location location = new Location(locationIndex++);
                    locations.add(location);
                    repeatCount++;

                    land.getAdjentLocations().add(location);
                    location.getAdjentLands().add(land);

                   if((repeatCount != 0 || j != 0) && i != 0 ) {
                        Land topRight = lands.get(landIndex - (landCount_horizontal_max - Math.abs(i - (int) Math.floor(landCount_vertical / 2))));
                        topRight.getAdjentLocations().add(location);
                        if((j != 0 || i > landCount_vertical / 2) && repeatCount != 2){
                            Land topLeft = lands.get(landIndex - 1 -  (landCount_horizontal_max - Math.abs(i - 1 - (int) Math.floor(landCount_vertical / 2))));
                            topLeft.getAdjentLocations().add(location);
                        }

                    }

                }

            }
        }

        for (int i = 0; i < 3; i++) {
            int repeatCount = 0;
            while (repeatCount < 3 - ((i != 0) ? 1 : 0)) {
                Location location = new Location(locationIndex++);
                locations.add(location);
                repeatCount++;
            }
        }

        for(int i = 0; i < lands.size(); i++){
            System.out.println(lands.get(i).type.toString() + " " + lands.get(i).index);
            for(int j = 0; j < lands.get(i).getAdjentLocations().size(); j++){
                System.out.println(lands.get(i).getAdjentLocations().get(j).index);
            }

        }
        System.out.println(lands.size() + " " + landIndex);



/*        System.out.println("Hello World!");

        final Formatter x;
        try {
            x = new Formatter("FoSho.txt");
            System.out.println("You created a file called FoSho.txt");
        } catch (Exception e) {
            System.out.println("You got an error");
        }

        Files.write(Paths.get("FoSho.txt"), "Beni Oku: Onur".getBytes(StandardCharsets.UTF_8));
*/

    }
}
