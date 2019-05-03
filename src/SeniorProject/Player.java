package SeniorProject;

import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

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
    private int longestRoad_length = 0;

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
        Board board = (Board) getPureBoard();

        ///region Print States
        State.StateBuilder stateBuilder = new State.StateBuilder(board);
        State currentState = stateBuilder.build();

        System.out.println("\n---------\n" + this + "'s " + (isInitial ? "[Initial]" : "") + " Turn "+currentState.getTurn());

        System.out.println(currentState);
        System.out.println("[My Affordable Moves]: " + currentState.getAffordableMoves(index));
        System.out.print("[My Possible Actions]: ");

        int size = currentState.getPossibleActions(index).size();
        for (int i = 0; i < size; i++)
            System.out.print("\n" + (i + 1) + ". " + currentState.getPossibleActions(index).get(i));

        if (size == 0)
            System.out.print("[]");

        System.out.println();
        ///endregion

        ArrayList<IAction> actionList = AI_instance.createActions(isInitial);

        String fileName = "actions_temp" + index + ".txt";
        String actionList_str = "";
        for (IAction action : actionList) {
            String action_str = action.getCommand();
            if (action_str != null)
                actionList_str += action_str + "\r\n";
        }

        AI_instance.clearVirtualBoards();

        Global.createTextFile(fileName, actionList_str);

        ///region Print Actions
        if (!actionList_str.equals("")) {
            System.out.println("["+this+"'s Choice}:");

            Scanner scanner = new Scanner(actionList_str);
            String action;
            while (scanner.hasNext()) {
                action = scanner.nextLine();
                System.out.println(ActionFactory.getAction(action, (Board) getPureBoard()));
            }

            System.out.println();

            Global.createTextFile(System.nanoTime() / 10000 + fileName, actionList_str);
        }
        else
            System.out.println("[" + this + "'s Choice]: []");
        ///endregion

        /*for (Player player : board.getPlayers()) {
            for (ResourceType resourceType : player.getResource().keySet()) {
                if (player.getResource().get(resourceType) < 0) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\nERROR! Negative values.");
                }
            }
        }*/

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

    public int getPreviousIndex() {
        if (index == 0)
            return Global.PLAYER_COUNT-1;
        else
            return index - 1;
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

    public Resource getResource() {
        return resource;
    }

    /*public void changeResource(ResourceType resourceType, Integer value) {
        if (resourceType != null)
            resource.replace(resourceType, resource.get(resourceType) + value);

        updateSubscribers();
    }*/

    void createAI() {
        this.AI_instance = new BasicAI(this, (Board) pureBoard);
    }

    public void setAI(BasicAI ai) {
        this.AI_instance = ai;
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

    public void setLongestRoad_length(int longestRoad_length) {
        this.longestRoad_length = longestRoad_length;
    }

    public int getLongestRoad_length() {
        return longestRoad_length;
    }

    public void addDevelopmentCard(DevelopmentCardType developmentCardType) {
        developmentCards.add(developmentCardType);
    }

    @Override
    public String toString() {
        return "P" + (index + 1);
    }
}