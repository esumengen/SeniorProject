package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;

enum LandType implements Serializable {
    FIELDS, FOREST, PASTURE, MOUNTAINS, HILLS, DESERT, SEA, NONE;

    @Override
    public String toString() {
        return super.toString();
    }

    public ResourceType toResourceType() {
        if (ordinal() < 5)
            return ResourceType.values()[ordinal()];
        else
            return null;
    }
}

public class Land implements Serializable {
    private int index;
    private LandType type;
    private ArrayList<Location> adjacentLocations;
    private int diceNo;
    private double diceChance;
    private int i;
    private int j;
    private PureBoard pureBoard;

    Land(int index) {
        this.adjacentLocations = new ArrayList<>();
        this.index = index;
        this.type = LandType.NONE;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjacentLocations;
    }

    public ArrayList<Building> getAllBuildings() {
        ArrayList<Building> buildings = new ArrayList<>();

        for (Player player : Main.players) {
            for (Location location : adjacentLocations) {
                if (location.getOwner() != null && location.getOwner().getIndex() == player.getIndex())
                    buildings.add(location.getBuilding());
            }
        }

        return buildings;
    }

    public int getDiceNo() {
        return diceNo;
    }

    void setDiceNo(int diceNo) {
        this.diceNo = diceNo;
        this.diceChance = calculateDiceChance();
    }

    public LandType getType() {
        return type;
    }

    void setType(LandType type) {
        this.type = type;
    }

    public ResourceType getResourceType() {
        return type.toResourceType();
    }

    public int getIndex() {
        return index;
    }

    public double calculateDiceChance() {
        double numerator = 0.0;

        for (int k = 1; k <= 6; k++) {
            for (int l = 1; l <= 6; l++) {
                if (k + l == diceNo) numerator++;
            }
        }

        return numerator / 36;
    }

    public double getDiceChance() {
        return diceChance;
    }

    public int getI() {
        return i;
    }

    void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    void setJ(int j) {
        this.j = j;
    }

    void setPureBoard(PureBoard board) {
        this.pureBoard = board;
    }

    public String toString() {
        return "Land" + index;
    }
}