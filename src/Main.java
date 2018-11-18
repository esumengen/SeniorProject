import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.*;

/*
---First Priority Decisions
buildSettlement()
buildRoad()
upgradeSettlement()
pickCard()
---Second Priority Decisions
tradeNegotiation()
tradeBank()

* */

public class Main {
    static ArrayList<Land> lands = new ArrayList<>();
    static ArrayList<Location> locations = new ArrayList<>();

    static StructerList structures = new StructerList();
    static final String path = "C:\\Users\\Bekir Onur Gölgedar\\AppData\\Local\\Catan\n";
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

    public static void main(String[] args) {


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

        /*for(int i = 0; i < lands.size(); i++){
            System.out.println(lands.get(i).type.toString() + " " + lands.get(i).index);
            for(int j = 0; j < lands.get(i).getAdjentLocations().size(); j++){
                System.out.println(lands.get(i).getAdjentLocations().get(j).index);

            }
        }*/


        /*for(int i = 0; i < locations.size(); i++) {
            System.out.println("Location " + locations.get(i).index);
            for (int j = 0; j < locations.get(i).getAdjentLands().size(); j++) {
                System.out.println(locations.get(i).getAdjentLands().get(j).index);
            }
        }*/



/*        System.out.println("Hello World!");

        final Formatter x;
        try {
            x = new Formatter("communication.txt");
            System.out.println("You created a file called FoSho.txt");
        } catch (Exception e) {
            System.out.println("You got an error");
        }

        Files.write(Paths.get("FoSho.txt"), "Beni Oku: Onur".getBytes(StandardCharsets.UTF_8));
*/

    }

    static void load(ArrayList<Land> lands, ArrayList<Location> locations){
        try{
            Wini ini = new Wini(new File(path));
            String type_str;
            int diceNo;

            for (Land land : lands) {
                type_str = ini.get("LandTypes", Integer.toString(land.index), String.class);
                type_str = String.copyValueOf(type_str.toCharArray(), 1, type_str.length()-2);

                diceNo = ini.get("Dice", Integer.toString(land.index), int.class);

                land.type = LandType.valueOf(type_str.toUpperCase());
                land.diceNo = diceNo;

                System.out.println(land.diceNo);
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
            x = new Formatter("C:\\Users\\Bekir Onur Gölgedar\\AppData\\Local\\Catan\\communication.txt");
            System.out.println("You created a file called communication.txt");
            Files.write(Paths.get("C:\\Users\\Bekir Onur Gölgedar\\AppData\\Local\\Catan\\communication.txt"), s.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            System.out.println("You got an error");
        }

    }

    private static void bind(Land land, Location location){
        land.getAdjentLocations().add(location);
        location.getAdjentLands().add(land);
    }

    private static void addBuilding(Player player, Location location, BuildingType buildingType) throws Exception{
        Building building;
        if (buildingType == BuildingType.Settlement)
            building = new Settlement(location, player);
        else if (buildingType == BuildingType.City)
            building = new City(location, player);
        else
            throw new Exception("Unknown BuildingType");

        structures.add(building);
    }

    /*private static City upgradeSettlement(Settlement settlement){
        return new
    }*/

    private static void addRoad(){

    }
}
