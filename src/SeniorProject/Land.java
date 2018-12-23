package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;

enum LandType implements Serializable {
    HILLS, MOUNTAINS, FIELDS, FOREST, PASTURE, DESERT, SEA, NONE;

    @Override
    public String toString() {
        return super.toString();
    }

    public ResourceType toResourceType() {
        return ResourceType.values()[ordinal()];
    }
}

public class Land implements Serializable {
    private int index;
    private LandType type;
    private ArrayList<Location> adjacentLocations;
    private int diceNo;
    private double diceChance = 0;

    public Land(int index) {
        this.adjacentLocations = new ArrayList<>();
        this.index = index;
        this.type = LandType.NONE;
    }

    Land(int index, LandType type) {
        this.adjacentLocations = new ArrayList<>();
        this.index = index;
        this.type = type;
    }

    ArrayList<Location> getAdjacentLocations() {
        return adjacentLocations;
    }

    int getDiceNo() {
        return diceNo;
    }

    void setDiceNo(int diceNo) {
        this.diceNo = diceNo;
        this.diceChance = calculateDiceChance();
    }

    LandType getType() {
        return type;
    }

    void setType(LandType type) {
        this.type = type;
    }

    ResourceType getResourceType() {
        return type.toResourceType();
    }

    int getIndex() {
        return index;
    }

    private double calculateDiceChance() {
        double numerator = 0.0;
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                if (i + j == diceNo) numerator++;
            }
        }
        return numerator / 36;
    }

    public double getDiceChance() {
        return diceChance;
    }
}