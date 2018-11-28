package SeniorProject;

import org.ini4j.Wini;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

class Board {
    private static final int PLAYER_COUNT = 4;
    private static final int landCount_horizontal_max = 5;
    private static final int landCount_vertical = 5;
    private static final int landCount_horizontal_min = landCount_horizontal_max - (int) Math.floor(landCount_vertical / 2);
    private static ArrayList<Land> lands = new ArrayList<>();
    private static ArrayList<Location> locations = new ArrayList<>();
    private static ArrayList<Structure> structures = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static int locationCount = calculateLocationCount();

    public Board() {
        for (int i = 0; i < PLAYER_COUNT; i++)
            players.add(new Player(i));

        //region Initialization of Lands&Locations
        int landIndex;

        // Create locations
        for (int i = 0; i < locationCount; i++) {
            Location location = new Location(i);
            locations.add(location);
        }

        // Create lands
        landIndex = 0;
        for (int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int) Math.floor(landCount_vertical / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = new Land(landIndex++);
                lands.add(land);
            }
        }

        // Bind lands&locations
        landIndex = 0;
        for (int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int) Math.floor(landCount_vertical / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = lands.get(landIndex);
                int result = 2 * landIndex + Global.fibonacci(i + 1);

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
        //endregion

        load(lands);

        System.out.println("\n"+this);
    }

    private static int calculateLocationCount() {
        int sum = 0;

        for(int i = landCount_horizontal_max; i >= landCount_horizontal_min; i--) {
            sum += 2 * (2 * i + 1);
        }
        return sum;
    }

    private void load(ArrayList<Land> lands) {
        try {
            Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

            String type_str;
            String dice_str;
            int diceNo;

            for (Land land : lands) {
                type_str = ini.get("LandTypes", Integer.toString(land.getIndex()), String.class);
                type_str = Global.getRidOf_quotationMarks(type_str);

                dice_str = ini.get("Dice", Integer.toString(land.getIndex()), String.class);
                dice_str = Global.getRidOf_quotationMarks(dice_str);
                diceNo = Integer.parseInt(dice_str);

                land.setType(LandType.valueOf(type_str.toUpperCase(Locale.ENGLISH)));
                land.setDiceNo(diceNo);
            }

            addLog("SUCCESS: The game is loaded to the AI.");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            addLog("ERROR: The game is not loaded to the AI.");
        }
    }

    private void addLog(String info) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(Global.get_working_path(Global.LOG_FILE), true);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(info);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();

                if (fileWriter != null)
                    fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Added a new log.");
        }
    }

    private void bind(Land land, Location location) {
        land.getAdjacentLocations().add(location);
        location.getAdjacentLands().add(land);
    }

    public void createSettlement(Player player, Location location) {
        Settlement settlement = new Settlement(location, player);
        structures.add(settlement);

        addLog("ACTION: A Settlement has been added on [Location " + location.getIndex() + "] by [Player " + player.getIndex() + "]");
    }

    public static ArrayList<Land> getLands() {
        return lands;
    }

    public static ArrayList<Location> getLocations() {
        return locations;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static ArrayList<Structure> getStructures() {
        return structures;
    }

    @Override
    public String toString() {
        String string = "";

        for (int i = 0; i < lands.size(); i++) {
            string += "[LAND " + lands.get(i).getIndex() + "]  \nType: "+lands.get(i).getType().toString()+"\nLocations: {";

            int j_max = lands.get(i).getAdjacentLocations().size()-1;
            for (int j = 0; j < j_max+1; j++) {
                string += lands.get(i).getAdjacentLocations().get(j).index + (j == j_max ? "" : ", ");
            }

            string += "}\n\n";
        }

        return string;
    }
}