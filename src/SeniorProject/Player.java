package SeniorProject;

import java.util.ArrayList;
import java.util.Map;

enum ResourceType {
    BRICK , STONE, WHEAT, WOOD, WOOL
}

public class Player {
    private String name;
    private int index;
    private int AIType;
    private ArrayList<Structure> structures = new ArrayList<>();
    private int wheat, wood, stone, wool, brick = 0;
    private Map<ResourceType, Integer> resources;

    public Player (int index){
        this.index = index;
        this.resources.put(ResourceType.BRICK, this.brick);
        this.resources.put(ResourceType.STONE, this.stone);
        this.resources.put(ResourceType.WHEAT, this.wheat);
        this.resources.put(ResourceType.WOOD, this.wood);
        this.resources.put(ResourceType.WOOL, this.wool);

    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getAIType() {
        return AIType;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getWool() {
        return wool;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }

    public void generateResource(ResourceType resourceType, LocationType locationType) {
        int multiplier = 1;
        if(locationType.equals(LocationType.CITY))
            multiplier = 2;
        resources.replace(resourceType, resources.get(resourceType) + multiplier);
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void addResource(ResourceType resourceType, Integer value) {
        resources.replace(resourceType, resources.get(resourceType) + value);
    }
}


