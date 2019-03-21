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
    private double diceChance = 0;
    private int i;
    private int j;
    private PureBoard pureBoard;

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

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public PureBoard getPureBoard() {
        return pureBoard;
    }

    public void setPureBoard(PureBoard board) {
        this.pureBoard = board;
    }
}