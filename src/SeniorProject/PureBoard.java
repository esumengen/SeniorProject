package SeniorProject;

import SeniorProject.DevelopmentCards.Deck;
import org.ini4j.Wini;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class PureBoard implements Serializable {
    private static final int LAND_COUNT_HORIZONTAL_MAX = 7;
    private static final int LAND_COUNT_VERTICAL = 7;
    private static final int LAND_COUNT_HORIZONTAL_MIN = LAND_COUNT_HORIZONTAL_MAX - (int) Math.floor(LAND_COUNT_VERTICAL / 2);

    private ArrayList<Land> lands = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Structure> structures = new ArrayList<>();
    private Deck deck;
    private Land robbedLand;
    private boolean isActive;
    private int longestRoad_owner;
    private ArrayList<Integer> topLocationIndexes = new ArrayList<>();

    public PureBoard(ArrayList<Land> lands, ArrayList<Location> locations) {
        init();

        this.lands = lands;
        this.locations = locations;
    }

    public PureBoard() {
        init();

        //region Initialization of Lands&Locations
        int landIndex;

        // Create locations
        int locationCount = calculateLocationCount();
        for (int i = 0; i < locationCount; i++) {
            Location location = new Location(i);
            location.setPureBoard(this);
            locations.add(location);
        }

        // Create lands
        landIndex = 0;
        for (int i = 0; i < LAND_COUNT_VERTICAL; i++) {
            int landCount_horizontal = LAND_COUNT_HORIZONTAL_MAX - Math.abs(i - (int) Math.floor(LAND_COUNT_VERTICAL / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = new Land(landIndex++);
                land.setI(i);
                land.setJ(j);
                land.setPureBoard(this);
                lands.add(land);
            }
        }

        // Bind lands&locations
        landIndex = 0;
        for (int i = 0; i < LAND_COUNT_VERTICAL; i++) {
            int landCount_horizontal = LAND_COUNT_HORIZONTAL_MAX - Math.abs(i - (int) Math.floor(LAND_COUNT_VERTICAL / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = lands.get(landIndex);

                int result = getTopLocation_index(landIndex);

                makeAdjacent(locations.get(result - 1), locations.get(result));
                makeAdjacent(locations.get(result + 1), locations.get(result));
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));

                int oldResult = result;
                result += 2 * landCount_horizontal + 2 - ((landCount_horizontal == LAND_COUNT_HORIZONTAL_MAX) ? 1 : 0);

                makeAdjacent(locations.get(result), locations.get(result - 1));
                makeAdjacent(locations.get(result), locations.get(result + 1));
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));

                makeAdjacent(locations.get(oldResult - 1), locations.get(result - 1));
                makeAdjacent(locations.get(oldResult + 1), locations.get(result + 1));

                landIndex++;
            }
        }
        //endregion

        load(lands);

        //region Mark the corner locations
        for (Land land : lands) {
            if (land.getType() == LandType.SEA) {
                for (Location location : land.getAdjacentLocations())
                    location.makeCorner();
            } else {
                for (Location location : land.getAdjacentLocations())
                    location.setActive(true);
            }
        }
        //endregion
    }

    private static int calculateLocationCount() {
        int sum = 0;

        for (int i = LAND_COUNT_HORIZONTAL_MAX; i >= LAND_COUNT_HORIZONTAL_MIN; i--) {
            sum += 2 * (2 * i + 1);
        }
        return sum;
    }

    public static PureBoard deepCopy(Serializable object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);

            outputStrm.writeObject(object);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);

            PureBoard pureBoard = (PureBoard) objInputStream.readObject();
            pureBoard.setActive(false);

            return pureBoard;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void init() {
        this.isActive = false;
        this.deck = new Deck();
    }

    private void makeAdjacent(Location location1, Location location2) {
        if (!location1.getAdjacentLocations().contains(location2))
            location1.addAdjacentLocations(location2);

        if (!location2.getAdjacentLocations().contains(location1))
            location2.addAdjacentLocations(location1);
    }

    private void load(ArrayList<Land> lands) {
        try {
            Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

            String type_str;
            String dice_str;
            int diceNo;
            String harbor_str;

            for (Land land : lands) {
                type_str = ini.get("LandTypes", Integer.toString(land.getIndex()), String.class);
                type_str = Global.getRidOf_quotationMarks(type_str);

                dice_str = ini.get("Dice", Integer.toString(land.getIndex()), String.class);
                dice_str = Global.getRidOf_quotationMarks(dice_str);
                diceNo = Integer.parseInt(dice_str);

                land.setType(LandType.valueOf(type_str.toUpperCase(Locale.ENGLISH)));
                land.setDiceNo(diceNo);

                if (land.getType() == LandType.DESERT) robbedLand = land;
            }

            for (Location location : locations) {
                harbor_str = ini.get("HarborTypes", Integer.toString(location.getIndex()), String.class);
                harbor_str = Global.getRidOf_quotationMarks(harbor_str);
                if (!harbor_str.equals("null")) {
                    location.setHarborType(HarborType.valueOf(harbor_str));
                }
            }

            //Global.addLog("SUCCESS: The game is loaded to the AI.");
        } catch (Exception e) {
            new Message(e.getMessage() + " - 7");

            //Global.addLog("ERROR: The game is not loaded to the AI.");
        }
    }

    private int getTopLocation_index(int landIndex) {
        if (topLocationIndexes.size() > landIndex)
            return topLocationIndexes.get(landIndex);
        else if (landIndex == 0) {
            topLocationIndexes.add(1);
            return 1;
        } else {
            int result = getTopLocation_index(landIndex - 1) + 2;

            int i = lands.get(landIndex).getI();
            int j = lands.get(landIndex).getJ();

            if (j == 0) {
                if (i == LAND_COUNT_VERTICAL / 2 + 1)
                    result += 2;
                else if (i < LAND_COUNT_VERTICAL / 2 + 1)
                    result += 1;
                else
                    result += 3;
            }

            topLocationIndexes.add(result);
            return result;
        }
    }

    private void bind(Land land, Location location) {
        land.getAdjacentLocations().add(location);
        location.getAdjacentLands().add(land);
    }

    boolean isValid(Structure structure) {
        return isValid(structure, false);
    }

    boolean isValid(Structure structure, boolean isInitial) {
        if (structure instanceof Road) {
            Road road = (Road) structure;
            Location end = road.getEndLocation();
            Location start = road.getStartLocation();
            boolean areBothLocationsActive = start.isActive() && end.isActive();

            // İki ucu aktif olmak zorunda.
            if (!areBothLocationsActive)
                return false;

            // İki ucu komşu olmak zorunda.
            if (!start.getAdjacentLocations().contains(end))
                return false;

            // Aynı yoldan başka olmamalı.
            if (roadExits(road))
                return false;

            // Eğer başlangıç hamlesiyse, iki ucundan birinde mutlaka bir ev olmalı ve hiç yol olmamalı.
            if (isInitial) {
                if (!(countStructures(StructureType.SETTLEMENT, start) == 1 && countStructures(StructureType.ROAD, start) == 0)
                        && !(countStructures(StructureType.SETTLEMENT, end) == 1 && countStructures(StructureType.ROAD, end) == 0))
                    return false;
            }

            // Herhangi bir ucunda rakip bina olmamalı. (?)
            /*if ((start.hasOwner() && start.getOwner().getIndex() != road.getPlayer().getIndex())
                    || (end.hasOwner() && end.getOwner().getIndex() != road.getPlayer().getIndex()))
                return false;*/

            // İki ucunda en az bir tane kendi yapısı olmalı.
            if (countStructures(StructureType.ROAD, start, road.getPlayer().getIndex()) + countStructures(StructureType.ROAD, end, road.getPlayer().getIndex())
                    + countStructures(StructureType.SETTLEMENT, start, road.getPlayer().getIndex()) + countStructures(StructureType.SETTLEMENT, end, road.getPlayer().getIndex()) == 0)
                return false;
        } else if (structure instanceof Building) {
            Building settlement = (Building) structure;

            // Bu location aktif olmalı.
            if (!settlement.getLocation().isActive())
                return false;

            // Etrafında bina olmamalı.
            if (!isAdjacentFree(settlement.getLocation()))
                return false;

            // Başlangıç durumu değilse
            if (!isInitial) {
                // Tam o location'a bağlı en az bir yolu bulunmalı.
                if (countStructures(StructureType.ROAD, settlement.getLocation(), settlement.getPlayer().getIndex()) == 0)
                    return false;
            } else {
                // Köşede olmamalı.
                /*if (((Building) structure).getLocation().isCorner())
                    return false;*/
            }
        }

        return true;
    }

    boolean roadExits(Road newRoad) {
        for (Structure structure : structures) {
            if (structure.getType() == StructureType.ROAD) {
                Road road = (Road) structure;

                if ((road.getStartLocation() == newRoad.getStartLocation() && (road.getEndLocation() == newRoad.getEndLocation()))
                        || ((road.getStartLocation() == newRoad.getEndLocation()) && (road.getEndLocation() == newRoad.getStartLocation()))) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean isAdjacentFree(Location location) {
        /*for (Player player:players) {
            for (Structure structure:player.getStructures()) {
                if (structure instanceof Building)
                    if (((Building) structure).getLocation().getIndex() == location.getIndex())
                        return false;
            }
        }*/
        if (location.hasOwner()) {
            return false;
        }

        for (Location adjacentLocation : location.getAdjacentLocations()) {
            if (adjacentLocation.hasOwner())
                return false;
        }

        return true;
    }

    int countStructures(StructureType type, int playerIndex) {
        return countStructures(type, null, playerIndex);
    }

    int countStructures(StructureType type, Location location) {
        return countStructures(type, location, -1);
    }

    int countStructures(StructureType type, Location location, int playerIndex) {
        int count = 0;

        if (location == null) {
            for (Location _location : locations)
                count += countStructures(type, _location, playerIndex);

            if (type == StructureType.ROAD)
                count /= 2;
        } else {
            if (type == StructureType.ROAD) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof Road && (playerIndex == -1 || structure.getPlayer().getIndex() == playerIndex))
                        count++;
                }
            } else if (type == StructureType.SETTLEMENT) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof Settlement && (playerIndex == -1 || structure.getPlayer().getIndex() == playerIndex))
                        count++;
                }
            } else if (type == StructureType.CITY) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof City && (playerIndex == -1 || structure.getPlayer().getIndex() == playerIndex))
                        count++;
                }
            }
        }

        return count;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    void syncPlayer(Player player) {
        player.getStructures().clear();

        // Structure assignment
        for (Structure structure : structures) {
            if (player == structure.getPlayer()) {
                player.getStructures().add(structure);
            }
        }
    }

    public ArrayList<Land> getLands() {
        return lands;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < lands.size(); i++) {
            string += "[LAND " + lands.get(i).getIndex() + "]\nScore: " + lands.get(i).getDiceChance() + "\nType: " + lands.get(i).getType().toString() + "\nLocations: {";
            int j_max = lands.get(i).getAdjacentLocations().size() - 1;
            for (int j = 0; j < j_max + 1; j++) {
                string += lands.get(i).getAdjacentLocations().get(j).getIndex() + (j == j_max ? "" : ", ");
            }
            string += "}\n\n";
        }

        return string;
    }

    public Land getRobbedLand() {
        return robbedLand;
    }

    public void setRobbedLand(Land land) {
        robbedLand = land;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getLongestRoad_owner() {
        return longestRoad_owner;
    }

    public void setLongestRoad_owner(int longestRoad_owner) {
        this.longestRoad_owner = longestRoad_owner;
    }
}