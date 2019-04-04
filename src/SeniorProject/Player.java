package SeniorProject;

import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable, IObservable {
    private BasicAI AI_instance;
    private String name;
    private int index;
    private int victoryPoint;
    private PlayerType type;
    private ArrayList<Structure> structures = new ArrayList<>();
    private Resource resource;
    private ArrayList<DevelopmentCardType> developmentCards = new ArrayList<>();
    private int knight = 0;
    private PureBoard pureBoard;
    private PlayerState state = PlayerState.IDLE;
    private ArrayList<IObserver> observers;

    public Player(int index) {
        this.index = index;
        this.type = PlayerType.AI;
        this.victoryPoint = 0;

        this.name = "Player "+(index+1);

        observers = new ArrayList<>();

        resource = new Resource(0, 0, 0, 0, 0);
    }

    public int getIndex() {
        return index;
    }

    void writeMove(boolean isInitial) {
        String actionList_str = AI_instance.createMoves(isInitial);
        AI_instance.clearVirtualBoards();

        String fileName = "actions_temp" + index + ".txt";
        Global.createTextFile(fileName, actionList_str);

        if (!actionList_str.equals(""))
            Global.createTextFile(System.nanoTime()/10000 + fileName, actionList_str);

        setState(PlayerState.IDLE);
    }

    PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
        updateSubscribers();
    }

    void setResource(Resource resource) {
        this.resource = resource;
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
        return resource.get(ResourceType.GRAIN);
    }

    public void setGrain(int value) {
        resource.replace(ResourceType.GRAIN, value);

        updateSubscribers();
    }

    public int getNextIndex() {
        if (index == Global.PLAYER_COUNT - 1)
            return 0;
        else
            return index + 1;
    }

    public int getLumber() {
        return resource.get(ResourceType.LUMBER);
    }

    public void setLumber(int value) {
        resource.replace(ResourceType.LUMBER, value);

        updateSubscribers();
    }

    public int getOre() {
        return resource.get(ResourceType.ORE);
    }

    public void setOre(int value) {
        resource.replace(ResourceType.ORE, value);

        updateSubscribers();
    }

    public int getWool() {
        return resource.get(ResourceType.WOOL);
    }

    public void setWool(int value) {
        resource.replace(ResourceType.WOOL, value);

        updateSubscribers();
    }

    public int getBrick() {
        return resource.get(ResourceType.BRICK);
    }

    public void setBrick(int value) {
        resource.replace(ResourceType.BRICK, value);

        updateSubscribers();
    }

    Resource getResource() {
        return resource;
    }

    void changeResource(ResourceType resourceType, Integer value) {
        if (resourceType != null)
            resource.replace(resourceType, resource.get(resourceType) + value);

        updateSubscribers();
    }

    void createAI() {
        this.AI_instance = new BasicAI(this, (Board) pureBoard);
    }

    public BasicAI getAI_instance() {
        return AI_instance;
    }

    public ArrayList<DevelopmentCardType> getDevelopmentCards() {
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
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public void updateSubscribers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    @Override
    public ArrayList<IObserver> getObservers() {
        return observers;
    }

    public PureBoard getPureBoard() {
        return pureBoard;
    }

    public void setPureBoard(PureBoard pureBoard) {
        this.pureBoard = pureBoard;
    }

    public void addDevelopmentCard(DevelopmentCardType developmentCardType) {
        developmentCards.add(developmentCardType);
    }

    @Override
    public String toString() {
        return "P" + (index + 1);
    }
}