package SeniorProject;

import SeniorProject.Actions.*;
import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {
    private Board resultBoard;
    private Map<Integer, ArrayList<IAction>> possibleActions;
    private Map<Integer, ArrayList<ActionType>> affordableMoves;

    public State(StateBuilder stateBuilder) {
        resultBoard = stateBuilder.copiedOwner;
        affordableMoves = stateBuilder.affordableMoves;
        possibleActions = stateBuilder.possibleActions;
    }

    public int getVictoryPoints(int playerIndex) {
        return resultBoard.getPlayers().get(playerIndex).getVictoryPoint();
    }

    public boolean hasRobberPlayedRecently () {
        return resultBoard.hasRobberPlayedRecently();
    }

    public int getTotalDice () {
        return resultBoard.getTotalDice();
    }

    public Map<ResourceType, Integer> getResources(int playerIndex) {
        return resultBoard.getPlayers().get(playerIndex).getResource();
    }

    public ArrayList<DevelopmentCardType> getDevelopmentCards(int playerIndex) {
        return resultBoard.getPlayers().get(playerIndex).getDevelopmentCards();
    }

    public Player getWinner() {
        return resultBoard.getWinner();
    }

    public int getKnights(int playerIndex) {
        return resultBoard.getPlayers().get(playerIndex).getKnights();
    }

    public int getVictoryCards(int playerIndex) {
        int count = 0;
        for (DevelopmentCardType developmentCardType : resultBoard.getPlayers().get(playerIndex).getDevelopmentCards())
            if (developmentCardType == DevelopmentCardType.VICTORYPOINT)
                count++;

        return count;
    }

    public int getVictoryCards_max() {
        int max = -Integer.MIN_VALUE;

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            int victoryCards = getVictoryCards(i);

            if (victoryCards > max)
                max = victoryCards;
        }

        return max;
    }

    public int getKnights_max() {
        int max = -Integer.MIN_VALUE;

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            if (resultBoard.getPlayers().get(i).getKnights() > max)
                max = resultBoard.getPlayers().get(i).getKnights();
        }

        return max;
    }

    public ArrayList<IAction> getPossibleActions(int playerIndex) {
        return possibleActions.get(playerIndex);
    }

    public ArrayList<ActionType> getAffordableMoves(int playerIndex) {
        return affordableMoves.get(playerIndex);
    }

    public boolean isInitial() {
        return resultBoard.isInitial();
    }

    public int getTurn() {
        return resultBoard.getTurn();
    }

    public Map<Integer, Integer> getLongestRoad_lengths() {
        HashMap<Integer, Integer> longestRoad_lengths = new HashMap<>();
        for (int i = 0; i < Global.PLAYER_COUNT; i++)
            longestRoad_lengths.put(i, resultBoard.getPlayers().get(i).getLongestRoad_length());

        return longestRoad_lengths;
    }

    public int getLongestRoad_max() {
        int max = -Integer.MIN_VALUE;

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            int longestRoad_length = resultBoard.getPlayers().get(i).getLongestRoad_length();

            if (longestRoad_length > max)
                max = longestRoad_length;
        }

        return max;
    }

    public int getLongestRoad_ownerIndex() {
        return resultBoard.getLastLongestRoad_owner().getIndex();
    }

    @Override
    public String toString() {
        String string = "";

        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            Player player = resultBoard.getPlayers().get(i);
            string += "P" + (i + 1) + "'s Resource: " + player.getResource() + "  VP: " + player.getVictoryPoint() + "  LR: " + player.getLongestRoad_length() + "  KN: " + player.getKnights() + "  VC: " + getVictoryCards(player.getIndex()) + ((i != Global.PLAYER_COUNT - 1) ? "\n" : "");
        }

        return string;
    }

    public static class StateBuilder {
        private Map<Integer, ArrayList<IAction>> possibleActions;
        private Map<Integer, ArrayList<ActionType>> affordableMoves;
        private Board realOwner;
        private Board copiedOwner;
        private boolean isCopied;

        public StateBuilder(Board board) {
            realOwner = board;
            copiedOwner = realOwner;

            possibleActions = new HashMap<>();
            affordableMoves = new HashMap<>();

            for (int playerIndex = 0; playerIndex < Global.PLAYER_COUNT; playerIndex++) {
                possibleActions.put(playerIndex, new ArrayList<>());
                affordableMoves.put(playerIndex, new ArrayList<>());
            }
        }

        public StateBuilder setResource(int playerIndex, Resource resource) {
            if (!isCopied) {
                copiedOwner = Board.deepCopy(realOwner);
                isCopied = true;
            }

            copiedOwner.getPlayers().get(playerIndex).setResource(resource);

            return this;
        }

        public State build() {
            Board board = copiedOwner == null ? realOwner : copiedOwner;

            for (Player player : board.getPlayers()) {
                ArrayList<IAction> possibleActions = this.possibleActions.get(player.getIndex());
                ArrayList<ActionType> affordableMoves = this.affordableMoves.get(player.getIndex());

                /// region Affordability Test
                if (board.isInitial()) {
                    int settlementCount_my = board.countStructures(StructureType.SETTLEMENT, player.getIndex());
                    int roadCount_my = board.countStructures(StructureType.ROAD, player.getIndex());

                    if (settlementCount_my + roadCount_my < (board.getTurn() + 1) * 2) {
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
                        possibleActions.add(new DrawDevelopmentCard(player.getIndex(), board.getDeck(), board));
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

                                    possibleActions.add(new TradeWithBank(givenResource, takenResource, player.getIndex(), board));

                                    amount += 4;
                                }
                            }
                        }
                    }
                }

                for (Location location : board.getLocations()) {
                    if (board.getTotalDice() == 7 && player.getIndex() == board.getDiceOwner().getIndex() && !board.hasRobberPlayedRecently()) {
                        ArrayList<Integer> temporaryLocationList_index = new ArrayList<>();
                        for (Land land : board.getLands()) {
                            if (land.getType() != LandType.SEA) {
                                possibleActions.add(new MoveRobber(land.getIndex(), board.getDiceOwner().getIndex(), -1, board));
                                        temporaryLocationList_index.add(location.getIndex());
                            }
                        }
                    } else {
                        if (affordableMoves.contains(ActionType.CreateRoad)) {
                            for (Location endLocation : location.getAdjacentLocations()) {
                                Road road = new Road(location, endLocation, player);

                                if (endLocation.getIndex() > location.getIndex() && board.isValid(road, board.isInitial())) {
                                    int[] locations = new int[2];
                                    locations[0] = location.getIndex();
                                    locations[1] = endLocation.getIndex();

                                    possibleActions.add(new CreateRoad(locations, player.getIndex(), board));
                                }
                            }
                        }

                        if (affordableMoves.contains(ActionType.CreateSettlement) && board.isValid(new Settlement(location, player), board.isInitial()))
                            possibleActions.add(new CreateSettlement(location.getIndex(), player.getIndex(), board));

                        if (affordableMoves.contains(ActionType.UpgradeSettlement) && location.hasOwner() && location.getOwner().getIndex() == player.getIndex() && location.getBuilding() != null && location.getBuilding().getType() == StructureType.SETTLEMENT)
                            possibleActions.add(new UpgradeSettlement(location.getIndex(), player.getIndex(), board));
                    }
                }
            }

            return new State(this);
        }
    }
}