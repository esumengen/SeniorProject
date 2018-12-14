package SeniorProject;

import java.util.ArrayList;

enum LandType {
    HILLS, MOUNTAINS, FIELDS, FOREST, PASTURE, DESERT, SEA, NONE;

    @Override
    public String toString() {
        return super.toString();
    }

    public ResourceType toResourceType() {
        return  ResourceType.values()[ordinal()];
    }
}

class Land {
    private int index;
    private LandType type;
    private ArrayList<Location> adjacentLocations;
    private int diceNo;
    private double pickProbability;

    public Land(int index){
        this.adjacentLocations = new ArrayList<>();
        this.index = index;
        this.type = LandType.NONE;
        this.pickProbability = calculateProbability();
    }

    public Land(int index, LandType type){
        this.adjacentLocations = new ArrayList<>();
        this.index = index;
        this.type = type;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjacentLocations;
    }

    public int getDiceNo() {
        return diceNo;
    }

    public LandType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public void setDiceNo(int diceNo) {
        this.diceNo = diceNo;
    }

    public void setType(LandType type) {
        this.type = type;
    }

    private double calculateProbability(){
        int numerator = 0;
        for(int i = 1; i <= 6; i++) {
            for(int j = 1; i <=6; j++) {
                if(i + j == diceNo)
                    numerator++;
            }
        }
        return numerator / (6 * 6);
    }

    public double getPickProbability() {
        return pickProbability;
    }
}