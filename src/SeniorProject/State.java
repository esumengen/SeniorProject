package SeniorProject;

import SeniorProject.Actions.*;
import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State {
    private int[] victoryPoints;
    private Map<Integer, Resource> allResources;
    private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
    private Map<Integer, ArrayList<IAction>> allPossibleActions;
    private Map<Integer, ArrayList<MoveType>> allAffordableMoves;
    private PureBoard pureBoard;
    private boolean isInitial;

    public State (StateBuilder stateBuilder) {
        victoryPoints = stateBuilder.victoryPoints;
        allResources = stateBuilder.allResources;
        allDevelopmentCards = stateBuilder.allDevelopmentCards;
        allPossibleActions = stateBuilder.allPossibleActions;
        allAffordableMoves = stateBuilder.allAffordableMoves;
        isInitial = stateBuilder.isInitial;
    }

    public int getVictoryPoints(int playerIndex) {
        return victoryPoints[playerIndex];
    }

    public void setVictoryPoints(int playerIndex, int value) {
        victoryPoints[playerIndex] = value;
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

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public PureBoard getPureBoard() {
        return pureBoard;
    }

    public void setPureBoard(PureBoard pureBoard) {
        this.pureBoard = pureBoard;
    }

    @Override
    public String toString() {
        String string = "";

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            string += "P" + (i + 1) + ": " + allResources.get(i) + ((i != Global.PLAYER_COUNT - 1) ? "\n" : "");
        }

        return string;
    }

    public static class StateBuilder {
        private int[] victoryPoints;
        private Map<Integer, Resource> allResources;
        private Map<Integer, ArrayList<DevelopmentCardType>> allDevelopmentCards;
        private Map<Integer, ArrayList<IAction>> allPossibleActions;
        private Map<Integer, ArrayList<MoveType>> allAffordableMoves;
        private PureBoard pureBoard;
        private boolean isInitial;

        public StateBuilder () {
            victoryPoints = new int[Global.PLAYER_COUNT];
            allResources = new HashMap<>();
            allDevelopmentCards = new HashMap<>();
            allPossibleActions = new HashMap<>();
            allAffordableMoves = new HashMap<>();
            isInitial = false;

            initPureBoard();
        }

        public StateBuilder(State state) {
            victoryPoints = state.victoryPoints.clone();
            allResources = new HashMap<>(state.allResources);
            allDevelopmentCards = new HashMap<>(allDevelopmentCards);
            isInitial = state.isInitial;

            initPureBoard();
        }

        public void initPureBoard() {
            ArrayList<Player> players = new ArrayList<>();
            for (int playerIndex = 0; playerIndex < Global.PLAYER_COUNT; playerIndex++) {
                players.add(new Player(playerIndex));

                allResources.put(playerIndex, new Resource());

                allDevelopmentCards.put(playerIndex, new ArrayList<>());
                allPossibleActions.put(playerIndex, new ArrayList<>());
                allAffordableMoves.put(playerIndex, new ArrayList<>());
            }

            pureBoard = new Board(players);
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

                ArrayList<IAction> possibleActions = allPossibleActions.get(player.getIndex());
                ArrayList<MoveType> affordableMoves = allAffordableMoves.get(player.getIndex());

                /// region Affordability Test
                if (isInitial) {
                    affordableMoves.add(MoveType.CreateSettlement);
                    affordableMoves.add(MoveType.CreateRoad);
                }
                else {
                    if (Board.isAffordable(MoveType.CreateSettlement, player.getResource()))
                        affordableMoves.add(MoveType.CreateSettlement);
                    if (Board.isAffordable(MoveType.CreateRoad, player.getResource()))
                        affordableMoves.add(MoveType.CreateRoad);
                    if (Board.isAffordable(MoveType.UpgradeSettlement, player.getResource()))
                        affordableMoves.add(MoveType.UpgradeSettlement);
                    if (Board.isAffordable(MoveType.DevelopmentCard, player.getResource())) {
                        affordableMoves.add(MoveType.DevelopmentCard);
                        possibleActions.add(new DrawDevelopmentCard(player));
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
                        int amount = 4;

                        while (player.getResource().get(resourceType) >= amount) {
                            Resource givenResource = new Resource();
                            givenResource.put(resourceType, amount);

                            Resource takenResource = new Resource();
                            takenResource.put(resourceType, amount / 4);

                            possibleActions.add(new TradeWithBank(givenResource, takenResource, player));

                            amount += 4;
                        }
                    }
                }

                for (Location location : pureBoard.getLocations()) {
                    if (affordableMoves.contains(MoveType.CreateRoad)) {
                        for (Location endLocation : location.getAdjacentLocations()) {
                            Road road = new Road(location, endLocation, player);

                            if (pureBoard.isValid(road, isInitial)) {
                                Location[] locations = new Location[2];
                                locations[0] = location;
                                locations[1] = endLocation;

                                possibleActions.add(new CreateRoad(locations, player));
                            }
                        }
                    }

                    if (affordableMoves.contains(MoveType.CreateSettlement) && pureBoard.isValid(new Settlement(location, player), isInitial))
                        possibleActions.add(new CreateSettlement(location, player));

                    if (affordableMoves.contains(MoveType.UpgradeSettlement) && location.hasOwner() && location.getOwner().getIndex() == player.getIndex())
                        possibleActions.add(new UpgradeSettlement(location, player));
                }
            }

            return new State(this);
        }
    }
}