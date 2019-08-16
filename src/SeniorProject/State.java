package SeniorProject;

import SeniorProject.Actions.*;
import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {
    private int[] victoryPoints;
    private Map<Integer, Resource> allResources;
    private Map<Integer, Integer> knights;
    private Map<Integer, Integer> victoryCards;
    private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
    private Map<Integer, ArrayList<IAction>> allPossibleActions;
    private Map<Integer, ArrayList<ActionType>> allAffordableMoves;
    private Map<Integer, Integer> longestRoad_lengths;
    private int longestRoad_owner;
    private PureBoard pureBoard;
    private boolean isInitial;
    private int turn;
    private int totalDice;
    private boolean hasRobberPlayedRecently;

    // Initialization of action categories
    private ArrayList<MoveRobber> actions_robber = new ArrayList<>();
    private ArrayList<CreateSettlement> actions_settlement = new ArrayList<>();
    private ArrayList<CreateRoad> actions_road = new ArrayList<>();
    private ArrayList<UpgradeSettlement> actions_upgrade = new ArrayList<>();
    private ArrayList<TradeWithBank> tradeBank_actions = new ArrayList<>();
    private ArrayList<DrawDevelopmentCard> actions_draw = new ArrayList<>();

    public State(StateBuilder stateBuilder) {
        victoryPoints = stateBuilder.victoryPoints;
        allResources = stateBuilder.allResources;
        allDevelopmentCards = stateBuilder.allDevelopmentCards;
        allPossibleActions = stateBuilder.allPossibleActions;
        allAffordableMoves = stateBuilder.allAffordableMoves;
        longestRoad_lengths = stateBuilder.longestRoad_lengths;
        longestRoad_owner = stateBuilder.longestRoad_owner;
        isInitial = stateBuilder.isInitial;
        pureBoard = stateBuilder.pureBoard;
        turn = stateBuilder.turn;
        totalDice = stateBuilder.totalDice;
        hasRobberPlayedRecently = stateBuilder.hasRobberPlayedRecently;
        victoryCards = stateBuilder.victoryCards;
        knights = stateBuilder.knights;
    }

    public int getVictoryPoints(int playerIndex) {
        return victoryPoints[playerIndex];
    }

    public boolean hasRobberPlayedRecently () {
        return hasRobberPlayedRecently;
    }

    public int getTotalDice () {
        return totalDice;
    }

    public Map<ResourceType, Integer> getResources(int playerIndex) {
        return allResources.get(playerIndex);
    }

    public ArrayList<DevelopmentCardType> getDevelopmentCards(int playerIndex) {
        return allDevelopmentCards.get(playerIndex);
    }

    public Integer getKnights(int playerIndex) {
        return knights.get(playerIndex);
    }

    public Integer getVictoryCards(int playerIndex) {
        return victoryCards.get(playerIndex);
    }

    public Integer getVictoryCards_max() {
        Integer max = -Integer.MIN_VALUE;

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            if (victoryCards.get(i) > max)
                max = victoryCards.get(i);
        }

        return max;
    }

    public Integer getKnights_max() {
        Integer max = -Integer.MIN_VALUE;

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            if (knights.get(i) > max)
                max = knights.get(i);
        }

        return max;
    }

    public ArrayList<IAction> getPossibleActions(int playerIndex) {
        return allPossibleActions.get(playerIndex);
    }

    public ArrayList<ActionType> getAffordableMoves(int playerIndex) {
        return allAffordableMoves.get(playerIndex);
    }

    public boolean isInitial() {
        return isInitial;
    }

    public PureBoard getPureBoard() {
        return pureBoard;
    }

    @Override
    public String toString() {
        String string = ""/*"Turn "+getTurn()+"\n"*/;

        /*for (int i = 0; i < Global.PLAYER_COUNT; i++)
            string += "P" + (i + 1) + "'s Longest Road: " + longestRoad_lengths.get(i) + "\n";*/

        for (int i = 0; i < Global.PLAYER_COUNT; i++)
            string += "P" + (i + 1) + "'s Resource: " + allResources.get(i) + "  VP: " + victoryPoints[i] + "  LR: " + longestRoad_lengths.get(i) + "  KN: " + knights.get(i) + "  VC: " + victoryCards.get(i) + ((i != Global.PLAYER_COUNT - 1) ? "\n" : "");

        return string;
    }

    public int getTurn() {
        return turn;
    }

    public Map<Integer, Integer> getLongestRoad_lengths() {
        return longestRoad_lengths;
    }

    public int getLongestRoad_owner() {
        return longestRoad_owner;
    }

    public static class StateBuilder {
        private int[] victoryPoints;
        private Map<Integer, Resource> allResources;
        private Map<Integer, Integer> knights;
        private Map<Integer, Integer> victoryCards;
        private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
        private Map<Integer, ArrayList<IAction>> allPossibleActions;
        private Map<Integer, ArrayList<ActionType>> allAffordableMoves;
        private Map<Integer, Integer> longestRoad_lengths;
        private int longestRoad_owner;
        private PureBoard pureBoard;
        private boolean isInitial;
        private Board realOwner;
        private Board copiedOwner;
        private int turn;
        private int totalDice;
        private Player diceOwner;
        private boolean hasRobberPlayedRecently;

        /*public StateBuilder(State state) {
            initVariables();
            pureBoard = new PureBoard();

            victoryPoints = state.victoryPoints.clone();
            allResources = new HashMap<>(state.allResources);
            allDevelopmentCards = new HashMap<>(allDevelopmentCards);
            isInitial = state.isInitial;
        }*/

        public StateBuilder(Board board) {
            initVariables();
            realOwner = board;
            pureBoard = board;
            turn = board.getTurn();
            totalDice = board.getTotalDice();
            hasRobberPlayedRecently = board.hasRobberPlayedRecently();
            diceOwner = board.getDiceOwner();
            copiedOwner = Board.deepCopy(copiedOwner);

            for (int i = 0; i < Global.PLAYER_COUNT; i++) {
                victoryPoints[i] = board.getPlayers().get(i).getVictoryPoint();
                allResources.put(i, new Resource(board.getPlayers().get(i).getResource()));
                allDevelopmentCards.put(i, new ArrayList<>(board.getPlayers().get(i).getDevelopmentCards()));
                longestRoad_lengths.put(i, board.getLongestRoad(i).getKey());
                longestRoad_owner = board.getLongestRoad_owner();

                int knights_count = 0;
                int victoryCards_count = 0;
                for (DevelopmentCardType developmentCardType : board.getPlayers().get(i).getDevelopmentCards()) {
                    if (developmentCardType == DevelopmentCardType.KNIGHT)
                        knights_count++;
                    else if (developmentCardType == DevelopmentCardType.VICTORYPOINT)
                        victoryCards_count++;
                }

                knights.put(i, knights_count);
                victoryCards.put(i, victoryCards_count);
            }

            isInitial = board.isInitial();
        }

        public void setResource(int playerIndex, Resource resource) {
            copiedOwner.getPlayers().get(playerIndex).setResource(resource);
        }

        private void initVariables() {
            realOwner = null;
            victoryPoints = new int[Global.PLAYER_COUNT];
            allResources = new HashMap<>();
            allDevelopmentCards = new HashMap<>();
            allPossibleActions = new HashMap<>();
            allAffordableMoves = new HashMap<>();
            longestRoad_lengths = new HashMap<>();
            knights = new HashMap<>();
            victoryCards = new HashMap<>();
            isInitial = false;
            turn = Integer.MAX_VALUE;
            totalDice = 0;
            diceOwner = null;

            ArrayList<Player> players = new ArrayList<>();
            for (int playerIndex = 0; playerIndex < Global.PLAYER_COUNT; playerIndex++) {
                players.add(new Player(playerIndex));

                allResources.put(playerIndex, new Resource());

                allDevelopmentCards.put(playerIndex, new ArrayList<>());
                allPossibleActions.put(playerIndex, new ArrayList<>());
                allAffordableMoves.put(playerIndex, new ArrayList<>());
            }
        }

        /*public StateBuilder setVictoryPoints(int victoryPoint, int playerIndex) {
            this.victoryPoints[playerIndex] = victoryPoint;

            return this;
        }

        public StateBuilder setResource(Resource resource, int playerIndex) {
            allResources.replace(playerIndex, new Resource(resource));

            return this;
        }

        public StateBuilder setDevelopmentCards(ArrayList<DevelopmentCardType> developmentCards, int playerIndex) {
            allDevelopmentCards.replace(playerIndex, developmentCards);

            return this;
        }

        public StateBuilder initial(boolean initial) {
            isInitial = initial;

            return this;
        }*/

        public PureBoard getPureBoard() {
            return pureBoard;
        }

        /*public StateBuilder setPureBoard(PureBoard pureBoard) {
            this.pureBoard = pureBoard;

            return this;
        }*/

        public State build() {
            ArrayList<Player> players = Main.createPlayers();

            for (Player player : players) {
                player.setResource(allResources.get(player.getIndex()));

                if (realOwner != null)
                    player.setPureBoard(realOwner);

                ArrayList<IAction> possibleActions = allPossibleActions.get(player.getIndex());
                ArrayList<ActionType> affordableMoves = allAffordableMoves.get(player.getIndex());

                possibleActions.clear();
                affordableMoves.clear();

                /// region Affordability Test
                if (isInitial) {
                    int settlementCount_my = pureBoard.countStructures(StructureType.SETTLEMENT, player.getIndex());
                    int roadCount_my = pureBoard.countStructures(StructureType.ROAD, player.getIndex());

                    if (settlementCount_my + roadCount_my < turn * 2) {
                        if (settlementCount_my == roadCount_my)
                            affordableMoves.add(ActionType.CreateSettlement);
                        else
                            affordableMoves.add(ActionType.CreateRoad);
                    }
                } else {
                    if (Board.isAffordable(ActionType.CreateSettlement, player.getResource()))
                        affordableMoves.add(ActionType.CreateSettlement);
                    if (Board.isAffordable(ActionType.CreateRoad, player.getResource()))
                        affordableMoves.add(ActionType.CreateRoad);
                    if (Board.isAffordable(ActionType.UpgradeSettlement, player.getResource()))
                        affordableMoves.add(ActionType.UpgradeSettlement);
                    if (Board.isAffordable(ActionType.DrawDevCard, player.getResource()) && ((Board) player.getPureBoard()).getDeck().getSize() > 0) {
                        affordableMoves.add(ActionType.DrawDevCard);
                        possibleActions.add(new DrawDevelopmentCard(player.getIndex(), realOwner.getDeck(), realOwner));
                    }
                    if (Board.isAffordable(ActionType.TradeWithBank, player.getResource())) {
                        affordableMoves.add(ActionType.TradeWithBank);
                    }
                    if (Board.isAffordable(ActionType.TradeWithPlayer, player.getResource())) {
                        affordableMoves.add(ActionType.TradeWithPlayer);
                    }
                }
                /// endregion

                if (affordableMoves.contains(ActionType.TradeWithBank)) {
                    for (ResourceType resourceType : ResourceType.values()) {
                        int amount;

                        for (ResourceType resourceType2 : ResourceType.values()) {
                            if (!resourceType2.equals(resourceType)) {
                                amount = 4;

                                while (player.getResource().get(resourceType) >= amount) {
                                    Resource givenResource = new Resource();
                                    givenResource.put(resourceType, amount);

                                    Resource takenResource = new Resource();
                                    takenResource.put(resourceType2, amount / 4);

                                    possibleActions.add(new TradeWithBank(givenResource, takenResource, player.getIndex(), realOwner));

                                    amount += 4;
                                }
                            }
                        }
                    }
                }

                for (Location location : pureBoard.getLocations()) {
                    if (totalDice == 7 && player.getIndex() == diceOwner.getIndex() && !hasRobberPlayedRecently) {
                        ArrayList<Integer> temporaryLocationList_index = new ArrayList<>();
                        for (Land land : getPureBoard().getLands()) {
                            if (land.getType() != LandType.SEA) {
                                //for (Location _location : land.getAdjacentLocations()) {
                                //if ((!_location.hasOwner() || _location.getOwner().getIndex() != player.getIndex()) && !temporaryLocationList_index.contains(_location.getIndex())) {
                                possibleActions.add(new MoveRobber(land.getIndex(), diceOwner.getIndex(), -1/*_location.hasOwner() ? _location.getOwner().getIndex() : -1*/, realOwner));
                                        temporaryLocationList_index.add(location.getIndex());
                                //}
                                //}
                            }
                        }
                    } else {
                        if (affordableMoves.contains(ActionType.CreateRoad)) {
                            for (Location endLocation : location.getAdjacentLocations()) {
                                Road road = new Road(location, endLocation, player);

                                if (endLocation.getIndex() > location.getIndex() && pureBoard.isValid(road, isInitial)) {
                                    int[] locations = new int[2];
                                    locations[0] = location.getIndex();
                                    locations[1] = endLocation.getIndex();

                                    possibleActions.add(new CreateRoad(locations, player.getIndex(), realOwner));
                                }
                            }
                        }

                        if (affordableMoves.contains(ActionType.CreateSettlement) && pureBoard.isValid(new Settlement(location, player), isInitial))
                            possibleActions.add(new CreateSettlement(location.getIndex(), player.getIndex(), realOwner));

                        if (affordableMoves.contains(ActionType.UpgradeSettlement) && location.hasOwner() && location.getOwner().getIndex() == player.getIndex() && location.getBuilding() != null && location.getBuilding().getType() == StructureType.SETTLEMENT)
                            possibleActions.add(new UpgradeSettlement(location.getIndex(), player.getIndex(), realOwner));
                    }
                }
            }

            return new State(this);
        }
    }
}