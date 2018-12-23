package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum ResourceType {
    BRICK, ORE, GRAIN, LUMBER, WOOL
}

enum PlayerType {
    AI, HUMAN
}

enum PlayerState {
    THINKING, READY
}

public class Player implements Serializable {
    private String name;
    private int index;
    private PlayerType type;
    private ArrayList<Structure> structures = new ArrayList<>();
    private int grain, lumber, ore, wool, brick = 0;
    private Map<ResourceType, Integer> resources;
    private int knight = 0;
    private AI ai;
    private Board board;
    private PlayerState state = PlayerState.READY;

    public Player(int index) {
        this.index = index;
        this.type = PlayerType.AI;

        resources = new HashMap<>();
        resources.put(ResourceType.BRICK, this.brick);
        resources.put(ResourceType.ORE, this.ore);
        resources.put(ResourceType.GRAIN, this.grain);
        resources.put(ResourceType.LUMBER, this.lumber);
        resources.put(ResourceType.WOOL, this.wool);
    }

    public int getIndex() {
        return index;
    }

    void writeMove(boolean isInitial) {
        String actionList_str = ai.createMoves(isInitial);
        ai.clearVirtualBoards();

        String fileName = "actions_temp" + index + ".txt";
        Global.createTextFile(fileName, actionList_str);
        Global.createTextFile(System.nanoTime() + fileName, actionList_str);

        setState(PlayerState.READY);
    }

    PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    PlayerType getType() {
        return type;
    }

    void setType(PlayerType type) {
        this.type = type;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public int getGrain() {
        return grain;
    }

    public void setGrain(int grain) {
        this.grain = grain;
    }

    public int getLumber() {
        return lumber;
    }

    public void setLumber(int lumber) {
        this.lumber = lumber;
    }

    public int getOre() {
        return ore;
    }

    public void setOre(int ore) {
        this.ore = ore;
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

    Map<ResourceType, Integer> getResources() {
        return resources;
    }

    void addResource(ResourceType resourceType, Integer value) {
        resources.replace(resourceType, resources.get(resourceType) + value);
    }

    void createAI(Board board) {
        this.board = board;
        this.ai = new AI(this, board);
    }

    public int getKnight() {
        return knight;
    }
}