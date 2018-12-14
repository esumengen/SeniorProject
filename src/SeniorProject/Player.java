package SeniorProject;

import java.util.*;

enum ResourceType {
    BRICK , STONE, WHEAT, WOOD, WOOL
}

public class Player extends TimerTask{
    private String name;
    private int index;
    private int AIType;
    private ArrayList<Structure> structures = new ArrayList<>();
    private int wheat, wood, stone, wool, brick = 0;
    private Map<ResourceType, Integer> resources;
    Timer timer;

    public Player (int index){
        this.index = index;
        resources = new HashMap<>();
        resources.put(ResourceType.BRICK, this.brick);
        resources.put(ResourceType.STONE, this.stone);
        resources.put(ResourceType.WHEAT, this.wheat);
        resources.put(ResourceType.WOOD, this.wood);
        resources.put(ResourceType.WOOL, this.wool);
        timer = new Timer();
        timer.schedule(this,0, 250);

    }

    @Override
    public void run() {

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
        addResource(resourceType, resources.get(resourceType) + multiplier);
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void addResource(ResourceType resourceType, Integer value) {
        resources.replace(resourceType, resources.get(resourceType) + value);
    }
}


