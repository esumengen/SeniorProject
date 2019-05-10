package SeniorProject;

import SeniorProject.Actions.CreateRoad;
import SeniorProject.Actions.CreateSettlement;
import SeniorProject.Actions.TradeWithBank;
import SeniorProject.Actions.UpgradeSettlement;
import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {
    private int[] victoryPoints;
    private Map<Integer, Resource> allResources;
    private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
    private Map<Integer, ArrayList<IAction>> allPossibleActions;
    private Map<Integer, ArrayList<MoveType>> allAffordableMoves;
    private Map<Integer, Integer> longestRoad_lengths;
    private int longestRoad_owner;
    private PureBoard pureBoard;
    private boolean isInitial;
    private int turn;

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
    }

    public int getVictoryPoints(int playerIndex) {
        return victoryPoints[playerIndex];
    }

    public Map<ResourceType, Integer> getResources(int playerIndex) {
        return allResources.get(playerIndex);
    }

    public ArrayList<DevelopmentCardType> getDevelopmentCards(int playerIndex) {
        return allDevelopmentCards.get(playerIndex);
    }

    public ArrayList<IAction> getPossibleActions(int playerIndex) {
        return allPossibleActions.get(playerIndex);
    }

    public ArrayList<MoveType> getAffordableMoves(int playerIndex) {
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
            string += "P" + (i + 1) + "'s Resource: " + allResources.get(i) + ((i != Global.PLAYER_COUNT - 1) ? "\n" : "");

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
        private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
        private Map<Integer, ArrayList<IAction>> allPossibleActions;
        private Map<Integer, ArrayList<MoveType>> allAffordableMoves;
        private Map<Integer, Integer> longestRoad_lengths;
        private int longestRoad_owner;
        private PureBoard pureBoard;
        private boolean isInitial;
        private Board realOwner;
        private int turn;

        public StateBuilder() {
            initVariables();
            pureBoard = new PureBoard();
        }

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

            for (int i = 0; i < Global.PLAYER_COUNT; i++) {
                victoryPoints[i] = board.getPlayers().get(i).getVictoryPoint();
                allResources.put(i, new Resource(board.getPlayers().get(i).getResource()));
                allDevelopmentCards.put(i, new ArrayList<>(board.getPlayers().get(i).getDevelopmentCards()));
                longestRoad_lengths.put(i, board.getLongestRoad(i).getKey());
                longestRoad_owner = board.getLongestRoad_owner();
            }

            isInitial = board.isInitial();
        }

        private void initVariables() {
            realOwner = null;
            victoryPoints = new int[Global.PLAYER_COUNT];
            allResources = new HashMap<>();
            allDevelopmentCards = new HashMap<>();
            allPossibleActions = new HashMap<>();
            allAffordableMoves = new HashMap<>();
            longestRoad_lengths = new HashMap<>();
            isInitial = false;
            turn = Integer.MAX_VALUE;

            ArrayList<Player> players = new ArrayList<>();
            for (int playerIndex = 0; playerIndex < Global.PLAYER_COUNT; playerIndex++) {
                players.add(new Player(playerIndex));

                allResources.put(playerIndex, new Resource());

                allDevelopmentCards.put(playerIndex, new ArrayList<>());
                allPossibleActions.put(playerIndex, new ArrayList<>());
                allAffordableMoves.put(playerIndex, new ArrayList<>());
            }
        }

        public StateBuilder setVictoryPoints(int victoryPoint, int playerIndex) {
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
        }

        public PureBoard getPureBoard() {
            return pureBoard;
        }

        public StateBuilder setPureBoard(PureBoard pureBoard) {
            this.pureBoard = pureBoard;

            return this;
        }

        public State build() {
            ArrayList<Player> players = Main.createPlayers();

            for (Player player : players) {
                player.setResource(allResources.get(player.getIndex()));

                if (realOwner != null)
                    player.setPureBoard(realOwner);

                ArrayList<IAction> possibleActions = allPossibleActions.get(player.getIndex());
                ArrayList<MoveType> affordableMoves = allAffordableMoves.get(player.getIndex());

                possibleActions.clear();
                affordableMoves.clear();

                /// region Affordability Test
                if (isInitial) {
                    int settlementCount_my = pureBoard.countStructures(StructureType.SETTLEMENT, player.getIndex());
                    int roadCount_my = pureBoard.countStructures(StructureType.ROAD, player.getIndex());

                    if (settlementCount_my + roadCount_my < turn * 2) {
                        if (settlementCount_my == roadCount_my)
                            affordableMoves.add(MoveType.CreateSettlement);
                        else
                            affordableMoves.add(MoveType.CreateRoad);
                    }
                } else {
                    if (Board.isAffordable(MoveType.CreateSettlement, player.getResource()))
                        affordableMoves.add(MoveType.CreateSettlement);
                    if (Board.isAffordable(MoveType.CreateRoad, player.getResource()))
                        affordableMoves.add(MoveType.CreateRoad);
                    if (Board.isAffordable(MoveType.UpgradeSettlement, player.getResource()))
                        affordableMoves.add(MoveType.UpgradeSettlement);
                    if (Board.isAffordable(MoveType.DevelopmentCard, player.getResource())) {
                        affordableMoves.add(MoveType.DevelopmentCard);
                        //possibleActions.add(new DrawDevelopmentCard(player.getIndex(), realOwner));
                    }
                    if (Board.isAffordable(MoveType.KnightCard, player.getResource())) {
                        affordableMoves.add(MoveType.KnightCard);
                        //possibleActions.add(new UseDevelopmentCard(DevelopmentCardType.KNIGHT, player));
                    }
                    if (Board.isAffordable(MoveType.YearOfPlentyCard, player.getResource())) {
                        affordableMoves.add(MoveType.YearOfPlentyCard);
                        //possibleActions.add(new UseDevelopmentCard(DevelopmentCardType.YEAROFPLENTY, player));
                    }
                    if (Board.isAffordable(MoveType.RoadBuildingCard, player.getResource())) {
                        affordableMoves.add(MoveType.RoadBuildingCard);
                        //possibleActions.add(new UseDevelopmentCard(DevelopmentCardType.ROADBUILDING, player));
                    }
                    if (Board.isAffordable(MoveType.MonopolyCard, player.getResource())) {
                        affordableMoves.add(MoveType.MonopolyCard);
                        //possibleActions.add(new UseDevelopmentCard(DevelopmentCardType.MONOPOLY, player));
                    }
                    if (Board.isAffordable(MoveType.TradeBank, player.getResource())) {
                        affordableMoves.add(MoveType.TradeBank);
                    }
                    if (Board.isAffordable(MoveType.TradePlayer, player.getResource())) {
                        affordableMoves.add(MoveType.TradePlayer);
                        // TODO
                    }
                }
                /// endregion

                if (affordableMoves.contains(MoveType.TradeBank)) {
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
                    if (affordableMoves.contains(MoveType.CreateRoad)) {
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

                    if (affordableMoves.contains(MoveType.CreateSettlement) && pureBoard.isValid(new Settlement(location, player), isInitial))
                        possibleActions.add(new CreateSettlement(location.getIndex(), player.getIndex(), realOwner));

                    if (affordableMoves.contains(MoveType.UpgradeSettlement) && location.hasOwner() && location.getOwner().getIndex() == player.getIndex())
                        possibleActions.add(new UpgradeSettlement(location.getIndex(), player.getIndex(), realOwner));
                }
            }

            return new State(this);
        }
    }
}