package SeniorProject;

import java.util.*;

enum ResourceType {
    BRICK, ORE, GRAIN, LUMBER, WOOL
}

enum PlayerType {
    AI, HUMAN
}

public class Player {
    private String name;
    private int index;
    private PlayerType type;
    private ArrayList<Structure> structures = new ArrayList<>();
    private int grain, lumber, ore, wool, brick = 0;
    private Map<ResourceType, Integer> resources;
    private AI ai;

    public Player (int index){
        this.index = index;
        this.type = PlayerType.AI;
        this.ai = new AI(this);

        resources = new HashMap<>();
        resources.put(ResourceType.BRICK, this.brick);
        resources.put(ResourceType.ORE, this.ore);
        resources.put(ResourceType.GRAIN, this.grain);
        resources.put(ResourceType.LUMBER, this.lumber);
        resources.put(ResourceType.WOOL, this.wool);
    }

    int getIndex() {
        return index;
    }

    void writeMove (boolean isInitial) {

        String actionList_str = ai.createMoves(isInitial);

        String fileName = "actions_temp" + index + ".txt";
        Global.createTextFile(fileName, actionList_str);
    }

    String getName() {
        return name;
    }

    PlayerType getType() {
        return type;
    }

    void setType(PlayerType type) {
        this.type = type;
    }

    ArrayList<Structure> getStructures() {
        return structures;
    }

    int getGrain() {
        return grain;
    }

    void setGrain(int grain) {
        this.grain = grain;
    }

    int getLumber() {
        return lumber;
    }

    void setLumber(int lumber) {
        this.lumber = lumber;
    }

    int getOre() {
        return ore;
    }

    void setOre(int ore) {
        this.ore = ore;
    }

    int getWool() {
        return wool;
    }

    void setWool(int wool) {
        this.wool = wool;
    }

    int getBrick() {
        return brick;
    }

    void setBrick(int brick) {
        this.brick = brick;
    }

    Map<ResourceType, Integer> getResources() {
        return resources;
    }

    void addResource(ResourceType resourceType, Integer value) {
        resources.replace(resourceType, resources.get(resourceType) + value);
    }
}