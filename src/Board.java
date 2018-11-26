import org.ini4j.Wini;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class Board {
    static ArrayList<Land> lands = new ArrayList<>();
    static ArrayList<Location> locations = new ArrayList<>();

    static StructerList structures = new StructerList();
    static final String path = "/Users/emresumengen/Desktop/deneme";
//  static final String path = "C:\\Users\\Bekir Onur Gölgedar\\AppData\\Local\\Catan";
    static Formatter x;

    static final int landCount_horizontal_max = 5;
    static final int landCount_vertical = 5;
    static final int landCount_horizontal_min = landCount_horizontal_max - (int)Math.floor(landCount_vertical / 2);
    static int landIndex;
    static int locationCount = calculateLocations();

    private final static int FOREST_COUNT = 4;
    private final static int FIELDS_COUNT = 4;
    private final static int PASTURE_COUNT = 4;
    private final static int HILLS_COUNT = 3;
    private final static int MOUNTAINS_COUNT = 3;
    private final static int DESERT_COUNT = 1;
    private final static int[] DICE_FORMAT_5x5 = {11, 12, 9, 4, 6, 5, 10, 3, 11, 4, 8, 10, 8, 9, 3, 5, 2, 6};

    public Board(){

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

                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));
                result += 2 * landCount_horizontal + 2 - ((landCount_horizontal == landCount_horizontal_max) ? 1 : 0);
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));
                landIndex++;
            }
        }

        load(lands, locations);

        for(int i = 0; i < lands.size(); i++){
            System.out.println(lands.get(i).type.toString() + " " + lands.get(i).index);
            for(int j = 0; j < lands.get(i).getAdjentLocations().size(); j++){
                System.out.println(lands.get(i).getAdjentLocations().get(j).index);

            }
        }
    }



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

    static void load(ArrayList<Land> lands, ArrayList<Location> locations){
        try {
//          Wini ini = new Wini(new File(path+"\\environment.ini"));
            Wini ini = new Wini(new File(path+"/environment.ini"));

            String type_str;
            String dice_str;
            int diceNo;

            for (Land land : lands) {
                type_str = ini.get("LandTypes", Integer.toString(land.index), String.class);
                type_str = String.copyValueOf(type_str.toCharArray(), 1, type_str.length()-2);
                land.type = LandType.valueOf(type_str.toUpperCase(Locale.ENGLISH));

                dice_str = ini.get("Dice", Integer.toString(land.index), String.class);
                diceNo = Integer.parseInt(String.copyValueOf(dice_str.toCharArray(), 1, dice_str.length()-2));

                land.diceNo = diceNo;
            }
            communicate("Environment okundu.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            communicate("Environment okunamadı.");
        }
    }

    private static void communicate(String s) {
        try {
//          File file = new File(path+"\\communication.txt");
            File file = new File(path+"/communication.txt");
            PrintWriter printWriter = new PrintWriter(file);

            printWriter.println(s);
            printWriter.close();

            System.out.println("You created a file called communication.txt");
        } catch (Exception e) {
            new Warning(e.getMessage());
        }

    }

    private static void bind(Land land, Location location){
        land.getAdjentLocations().add(location);
        location.getAdjentLands().add(land);
    }
}
