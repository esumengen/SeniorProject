package SeniorProject;

import SeniorProject.Actions.TradeWithPlayer;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.Negotiation.NegotiationAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Serializable, IObservable {
    private AI AI_instance;
    private NegotiationAgent NegotiationAgent_instance;
    private String name;
    private int index;
    private int victoryPoint;
    private PlayerType type;
    private ArrayList<Structure> structures = new ArrayList<>();
    private Resource resource;
    private ArrayList<DevelopmentCardType> developmentCards = new ArrayList<>();
    private PureBoard pureBoard;
    private PlayerState state = PlayerState.IDLE;
    private ArrayList<IObserver> observers;
    private int longestRoad_length = 0;

    public Player(int index) {
        this.index = index;
        this.type = PlayerType.AI;
        this.victoryPoint = 0;

        this.name = "Player " + (index + 1);

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

        System.out.println("\n---------\n" + this + "'s Move [" + (isInitial ? "[Initial] " : "") + "Turn " + currentState.getTurn() + "]");

        System.out.println(currentState);
        System.out.println();
        System.out.println("[My Affordable Moves]: " + currentState.getAffordableMoves(index));
        /*System.out.print("[My Possible Actions]: ");

        int size = currentState.getPossibleActions(index).size();
        for (int i = 0; i < size; i++)
            System.out.print("\n" + (i + 1) + ". " + currentState.getPossibleActions(index).get(i));

        if (size == 0)
            System.out.print("[]");

        System.out.println();*/
        ///endregion

        AI_instance.resetAI();
        AI_instance.virtualBoard.changeUpdate();
        AI_instance.possibleActions = AI_instance.virtualBoard.getState().getPossibleActions(AI_instance.getOwner().getIndex());

        for (IAction negotiationAction : AI_instance.getNegotiationActions()) {
            ((TradeWithPlayer) negotiationAction).setBoard(AI_instance.getVirtualBoard());
            AI_instance.doVirtually(negotiationAction);
        }
        ArrayList<IAction> actionList = AI_instance.createActions(isInitial);
        AI_instance.clearNegotiationActions();

        String fileName = "actions_agentPlatform_" + index + ".txt";
        String actionList_str = "";
        for (IAction action : actionList) {
            String action_str = action.getCommand();
            if (action_str != null)
                actionList_str += action_str + "\r\n";
        }

        Main.virtualBoard_last = AI_instance.virtualBoard;

        Global.createTextFile(fileName, actionList_str);

        ///region Print Actions
        if (!actionList_str.equals("")) {
            System.out.println("[" + this + "'s Choice]:");

            Scanner scanner = new Scanner(actionList_str);
            String action;
            while (scanner.hasNext()) {
                action = scanner.nextLine();
                System.out.println(ActionFactory.getAction(action, (Board) getPureBoard()));
            }

            //Global.createTextFile(System.nanoTime() / 10000 + "_" + fileName, actionList_str);
        } else
            System.out.println("[" + this + "'s Choice]: []");
        ///endregion

        System.out.println();
        for (Player player : Main.virtualBoard_last.getPlayers()) {
            int knights_count = 0;
            int victoryCards_count = 0;

            for (DevelopmentCardType developmentCardType : player.getDevelopmentCards()) {
                if (developmentCardType == DevelopmentCardType.KNIGHT)
                    knights_count++;
                else if (developmentCardType == DevelopmentCardType.VICTORYPOINT)
                    victoryCards_count++;
            }

            System.out.println(player + "'s Last Resource: " + player.getResource() + "  VP: " + player.getVictoryPoint() + "  LR: " + Main.virtualBoard_last.getLongestRoad(player.getIndex()).getKey()
                    + "  KN: " + knights_count + "  VC: " + victoryCards_count);
        }

        setState(PlayerState.IDLE);
    }

    public PlayerState getState() {
        return state;
    }

    void setState(PlayerState state) {
        this.state = state;
        updateSubscribers();
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
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

    void setGrain(int value) {
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
            return Global.PLAYER_COUNT - 1;
        else
            return index - 1;
    }

    public int getLumber() {
        return resource.get(ResourceType.LUMBER);
    }

    void setLumber(int value) {
        resource.replace(ResourceType.LUMBER, value);

        updateSubscribers();
    }

    public int getOre() {
        return resource.get(ResourceType.ORE);
    }

    void setOre(int value) {
        resource.replace(ResourceType.ORE, value);

        updateSubscribers();
    }

    public int getWool() {
        return resource.get(ResourceType.WOOL);
    }

    void setWool(int value) {
        resource.replace(ResourceType.WOOL, value);

        updateSubscribers();
    }

    public int getBrick() {
        return resource.get(ResourceType.BRICK);
    }

    void setBrick(int value) {
        resource.replace(ResourceType.BRICK, value);

        updateSubscribers();
    }

    public Resource getResource() {
        return resource;
    }

    void setResource(Resource resource) {
        this.resource = resource;
    }

    /*public void changeResource(ResourceType resourceType, Integer value) {
        if (resourceType != null)
            resource.replace(resourceType, resource.get(resourceType) + value);

        updateSubscribers();
    }*/

    void createAI(AI AI_instance) {
        this.AI_instance = AI_instance;
    }

    void createNegotiationAgent(NegotiationAgent negotiationAgent) {
        this.NegotiationAgent_instance = negotiationAgent;

        if (negotiationAgent != null) {
            negotiationAgent.setOwner(this);
            negotiationAgent.setBidRanking(getAI().getBidRanking());
        }
    }

    public AI getAI() {
        return AI_instance;
    }

    void setAI(AI ai) {
        this.AI_instance = ai;
    }

    public ArrayList<DevelopmentCardType> getDevelopmentCards() {
        return developmentCards;
    }

    public int getKnights() {
        int count = 0;

        for (DevelopmentCardType developmentCardType : developmentCards)
            if (developmentCardType == DevelopmentCardType.KNIGHT)
                count++;

        return count;
    }

    void setVictoryPoint(int victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    void updateSubscribers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public PureBoard getPureBoard() {
        return pureBoard;
    }

    void setPureBoard(PureBoard pureBoard) {
        this.pureBoard = pureBoard;
    }

    public int getLongestRoad_length() {
        return longestRoad_length;
    }

    void setLongestRoad_length(int longestRoad_length) {
        this.longestRoad_length = longestRoad_length;
    }

    void addDevelopmentCard(DevelopmentCardType developmentCardType) {
        developmentCards.add(developmentCardType);
    }

    @Override
    public String toString() {
        return "P" + (index + 1);
    }

    public NegotiationAgent getNegotiationAgent() {
        return NegotiationAgent_instance;
    }

    void setNegotiationAgent(NegotiationAgent negotiationAgent_instance) {
        NegotiationAgent_instance = negotiationAgent_instance;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }
}