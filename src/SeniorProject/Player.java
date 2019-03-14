package SeniorProject;

import DevelopmentCards.DevelopmentCard;

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
    THINKING, IDLE
}

public class Player implements Serializable, Observer {
    private BasicAI AI_instance;

    private String name;
    private int index;
    private int victoryPoint;
    private PlayerType type;
    private ArrayList<Structure> structures = new ArrayList<>();
    private Map<ResourceType, Integer> resources;
    private ArrayList<DevelopmentCard> developmentCards = new ArrayList<>();
    private int knight = 0;
    private Board board;
    private PlayerState state = PlayerState.IDLE;
    private ArrayList<Subscriber> subscribers;

    public Player(int index) {
        this.index = index;
        this.type = PlayerType.AI;
        this.victoryPoint = 0;

        this.name = "Player "+(index+1);

        subscribers = new ArrayList<>();

        resources = new HashMap<>();
        resources.put(ResourceType.BRICK, 0);
        resources.put(ResourceType.ORE, 0);
        resources.put(ResourceType.GRAIN, 0);
        resources.put(ResourceType.LUMBER, 0);
        resources.put(ResourceType.WOOL, 0);
    }

    public int getIndex() {
        return index;
    }

    void writeMove(boolean isInitial) {
        String actionList_str = AI_instance.createMoves(isInitial);
        AI_instance.clearVirtualBoards();

        String fileName = "actions_temp" + index + ".txt";
        Global.createTextFile(fileName, actionList_str);

        /*if (!actionList_str.equals(""))
            Global.createTextFile(System.nanoTime()/10000 + fileName, actionList_str);*/

        setState(PlayerState.IDLE);
    }

    PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
        updateSubscribers();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateSubscribers();
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
        return resources.get(ResourceType.GRAIN);
    }

    public void setGrain(int value) {
        resources.replace(ResourceType.GRAIN, value);

        updateSubscribers();
    }

    public int getLumber() {
        return resources.get(ResourceType.LUMBER);
    }

    public void setLumber(int value) {
        resources.replace(ResourceType.LUMBER, value);

        updateSubscribers();
    }

    public int getOre() {
        return resources.get(ResourceType.ORE);
    }

    public void setOre(int value) {
        resources.replace(ResourceType.ORE, value);

        updateSubscribers();
    }

    public int getWool() {
        return resources.get(ResourceType.WOOL);
    }

    public void setWool(int value) {
        resources.replace(ResourceType.WOOL, value);

        updateSubscribers();
    }

    public int getBrick() {
        return resources.get(ResourceType.BRICK);
    }

    public void setBrick(int value) {
        resources.replace(ResourceType.BRICK, value);

        updateSubscribers();
    }

    Map<ResourceType, Integer> getResources() {
        return resources;
    }

    void changeResource(ResourceType resourceType, Integer value) {
        if (resourceType != null)
            resources.replace(resourceType, resources.get(resourceType) + value);

        updateSubscribers();
    }

    void createAI(Board board) {
        this.board = board;
        this.AI_instance = new BasicAI(this, board);
    }

    public BasicAI getAI_instance() {
        return AI_instance;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public int getKnight() {
        return knight;
    }

    public void setKnight(int knight) {
        this.knight = knight;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public void setVictoryPoint(int victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void updateSubscribers() {
        for (Subscriber subscriber:subscribers) {
            subscriber.update();
        }
    }

    @Override
    public ArrayList<Subscriber> getSubscribers() {
        return subscribers;
    }
}