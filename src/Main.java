import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.*;

public class Main {
    static ArrayList<Land> lands = new ArrayList<>();
    static ArrayList<Location> locations = new ArrayList<>();
    static int landCount_horizontal_max = 5;
    static int landCount_vertical = 5;
    static final int landCount_horizontal_min = landCount_horizontal_max - (int)Math.floor(landCount_vertical / 2);
    static int landIndex;
    static int locationCount = calculateLocations();

    private final static int FOREST_COUNT = 4;
    private final static int FIELDS_COUNT = 4;
    private final static int PASTURE_COUNT = 4;
    private final static int HILLS_COUNT = 3;
    private final static int MOUNTAINS_COUNT = 3;
    private final static int DESERT_COUNT = 1;




    private static int calculateLocations(){
        int sum = 0;
        for(int i = landCount_horizontal_max; i >= landCount_horizontal_min; i--){
            sum += 2 * (2 * i + 1);
        }
        return sum;
    }

    public static int fib(int n){
        if(n == 0 || n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws IOException {

        for(int i = 0; i < locationCount; i++) {
            Location land  = new Location(i);
            locations.add(land);
        }

        landIndex = 0;
        for(int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int)Math.floor(landCount_vertical / 2));
            for(int j = 0; j < landCount_horizontal; j++) {
                Land land  = new Land(landIndex++);
                lands.add(land);
            }
        }


        landIndex = 0;
        for(int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int)Math.floor(landCount_vertical / 2));
            for(int j = 0; j < landCount_horizontal; j++) {
                Land land = lands.get(landIndex);
                int result = 2 * landIndex + fib(i + 1);

                land.getAdjentLocations().add(locations.get(result - 1));
                land.getAdjentLocations().add(locations.get(result));
                land.getAdjentLocations().add(locations.get(result + 1));
                result += 2 * landCount_horizontal + 2 - ((landCount_horizontal == landCount_horizontal_max) ? 1 : 0);
                land.getAdjentLocations().add(locations.get(result - 1));
                land.getAdjentLocations().add(locations.get(result));
                land.getAdjentLocations().add(locations.get(result + 1));

                landIndex++;
            }
        }

        for(int i = 0; i < locations.size(); i++) {
            for (int j = 0; j < lands.size(); j++) {
                for (int k = 0; k < lands.get(j).getAdjentLocations().size(); k++) {
                    if(locations.get(i).index == lands.get(j).getAdjentLocations().get(k).index) locations.get(i).getAdjentLands().add(lands.get(j));
                }
            }
        }
        load(lands, locations);

        for(int i = 0; i < lands.size(); i++){
            System.out.println(lands.get(i).type.toString() + " " + lands.get(i).index);
            for(int j = 0; j < lands.get(i).getAdjentLocations().size(); j++){
                System.out.println(lands.get(i).getAdjentLocations().get(j).index);
            }
        }


        for(int i = 0; i < locations.size(); i++) {
            System.out.println("Location " + locations.get(i).index);
            for (int j = 0; j < locations.get(i).getAdjentLands().size(); j++) {
                System.out.println(locations.get(i).getAdjentLands().get(j).index);
            }
        }





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

    static void load(ArrayList<Land> lands, ArrayList<Location> locations){
        for (Land land : lands) {
            land.type = LandType.values()[new Random().nextInt(LandType.values().length - 1)];
        }
    }
}
