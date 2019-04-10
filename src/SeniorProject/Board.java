package SeniorProject;

import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Board extends PureBoard implements Serializable {
    private ArrayList<Player> players;
    private int turn;
    private boolean isMain;
    private State state;
    private boolean isInitial;

    public Board(ArrayList<Player> players) {
        super();

        for (Location location : getLocations())
            location.setPureBoard(this);

        for (Land land : getLands())
            land.setPureBoard(this);

        this.players = players;

        for (Player player : this.players)
            player.setPureBoard(this);

        changeUpdate();

        turn = 1;
        isMain = false;
        isInitial = true;
    }

    public static boolean isAffordable(MoveType type, Resource resource) {
        Player player = new Player(2);
        player.setResource(resource);

        return isAffordable(type, player);
    }

    public static boolean isAffordable(MoveType type, Player player) {
        int brick = player.getResource().get(ResourceType.BRICK);
        int grain = player.getResource().get(ResourceType.GRAIN);
        int wool = player.getResource().get(ResourceType.WOOL);
        int ore = player.getResource().get(ResourceType.ORE);
        int lumber = player.getResource().get(ResourceType.LUMBER);

        switch (type) {
            case CreateSettlement:
                if (brick >= 1 && grain >= 1 && wool >= 1 && lumber >= 1)
                    return true;
                break;
            case CreateRoad:
                if (brick >= 1 && lumber >= 1)
                    return true;
                break;
            case UpgradeSettlement:
                if (ore >= 3 && grain >= 2)
                    return true;
                break;
            case DevelopmentCard:
                if (grain >= 1 && wool >= 1 && ore >= 1)
                    return true;
                break;
            case TradeBank:
                /*if (brick < 4 && grain < 4 && wool < 4 && ore < 4 && lumber < 4)
                    return false;*/

                if (brick / 4 + grain / 4 + wool / 4 + ore / 4 + lumber / 4 == 0)
                    return false;
                return true;
            case KnightCard:
                return false;
            case MonopolyCard:
                return false;
            case RoadBuildingCard:
                return false;
            case YearOfPlentyCard:
                return false;
        }

        return false;
    }

    public void createSettlement(Player player, Location location) {
        int settlementCount = countStructures(StructureType.SETTLEMENT, player);

        Settlement settlement = new Settlement(location, player);
        getStructures().add(settlement);

        location.setOwner(player);
        location.addStructures(settlement);

        // Immediate Harvesting
        if (settlementCount == 1) {
            for (Land land : location.getAdjacentLands())
                player.changeResource(land.getResourceType(), 1);
        }

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A settlement has been added on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");
    }

    public void createRoad(Player player, Location location_first, Location location_second) {
        Road road = new Road(location_first, location_second, player);
        location_first.addConnectedRoad(road);
        location_second.addConnectedRoad(road);

        getStructures().add(road);

        location_first.addStructures(road);
        location_second.addStructures(road);

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A road has been added between [Location " + location_first.getIndex() + " and Location " + location_second.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");
    }

    public void upgradeSettlement(Player player, Location location) {
        for (Structure structure : location.getStructures()) {
            if (structure instanceof Settlement) {
                City city = new City(location, structure.getPlayer());
                getStructures().set(getStructures().indexOf(structure), city);
                location.getStructures().set(location.getStructures().indexOf(structure), city);
                break;
            }
        }

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A settlement has been upgraded on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");
    }

    public void moveRobber(Player robber, Land land) {
        setRobbedLand(land);
        robber.getResource().add(land.getResourceType(), 1);

        syncPlayer(robber);
        changeUpdate();

        addLog("ACTION: The robber has been moved to [Land " + land.getIndex() + "] by [Player " + (robber.getIndex() + 1) + "]");
    }

    public void drawDevelopmentCard (Player player) {
        DevelopmentCardType developmentCardType = getDeck().pickDevelopmentCard();
        player.addDevelopmentCard(developmentCardType);

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A development card("+developmentCardType+") is drawn by [Player " + (player.getIndex() + 1) + "]");
    }

    public void useDevelopmentCard_KNIGHT(Player player, Land land) {
        moveRobber(player, land);

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: Knight card is used by [Player " + (player.getIndex() + 1) + "]");
    }

    public void useDevelopmentCard_MONOPOLY(Player player, ResourceType resourceType) {
        for(Player _player : players) {
            if (_player.getIndex() != player.getIndex() && _player.getResource().get(resourceType) > 0) {
                _player.getResource().add(resourceType, -1);
                player.getResource().add(resourceType, 1);
            }
        }

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: Monopoly card is used by [Player " + (player.getIndex() + 1) + "]");
    }

    public void useDevelopmentCard_ROADBUILDING(Player player, Location loc1, Location loc2, Location loc3, Location loc4) {
        createRoad(player, loc1, loc2);
        createRoad(player, loc3, loc4);

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: Road Building card is used by [Player " + (player.getIndex() + 1) + "]");
    }

    public void useDevelopmentCard_YEAROFPLENTY(Player player, ResourceType resourceType1, ResourceType resourceType2) {
        player.getResource().add(resourceType1, 1);
        player.getResource().add(resourceType2, 1);

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: Year Of Plenty card is used by [Player " + (player.getIndex() + 1) + "]");
    }

    public void tradeBank(int playerIndex, Resource givenResources, Resource takenResources) {
        if (((takenResources.get(ResourceType.GRAIN) + takenResources.get(ResourceType.LUMBER)
                + takenResources.get(ResourceType.WOOL) + takenResources.get(ResourceType.ORE)
                + takenResources.get(ResourceType.BRICK)) * 4) == givenResources.get(ResourceType.GRAIN)
                + givenResources.get(ResourceType.LUMBER) + givenResources.get(ResourceType.WOOL)
                + givenResources.get(ResourceType.ORE) + givenResources.get(ResourceType.BRICK)) {

            players.get(playerIndex).setGrain(players.get(playerIndex).getGrain() - givenResources.get(ResourceType.GRAIN)
                    + takenResources.get(ResourceType.GRAIN));
            players.get(playerIndex).setLumber(players.get(playerIndex).getLumber() - givenResources.get(ResourceType.LUMBER)
                    + takenResources.get(ResourceType.LUMBER));
            players.get(playerIndex).setWool(players.get(playerIndex).getWool() - givenResources.get(ResourceType.WOOL)
                    + takenResources.get(ResourceType.WOOL));
            players.get(playerIndex).setOre(players.get(playerIndex).getOre() - givenResources.get(ResourceType.ORE)
                    + takenResources.get(ResourceType.ORE));
            players.get(playerIndex).setBrick(players.get(playerIndex).getBrick() - givenResources.get(ResourceType.BRICK)
                    + takenResources.get(ResourceType.BRICK));

            syncPlayer(players.get(playerIndex));
            changeUpdate();

            addLog("ACTION: A trade with bank has been done by [Player " + (playerIndex + 1) + "]");
        } else {
            addLog("ACTION: A trade with bank has been failed by [Player " + (playerIndex + 1) + "]");
        }
    }

    void tradePlayer(int playerIndex1, int playerIndex2, Map<ResourceType, Integer> givenResources, Map<ResourceType, Integer> takenResources) {
        // TODO

        syncPlayer(players.get(playerIndex1));
        syncPlayer(players.get(playerIndex2));
        changeUpdate();

        addLog("TO DO ACTION: A trade with [Player " + playerIndex2 + 1 + " has been done by [Player " + (playerIndex1 + 1) + "]");
    }

    public void rollDice(Player player, int dice1, int dice2) {
        generateResource(dice1 + dice2);

        addLog("ACTION: Dice are rolled " + dice1 + " " + dice2 + " by [Player " + (player.getIndex() + 1) + "]");
    }

    private void generateResource(int diceNo) {
        for (Land land : getLands()) {
            if (land.getDiceNo() == diceNo && land != getRobbedLand()) {
                for (Location location : land.getAdjacentLocations()) {
                    if (location.hasOwner()) {
                        Player rewardedPlayer = location.getOwner();

                        rewardedPlayer.changeResource(land.getResourceType(), location.hasCity() ? 2 : 1);

                        addLog("?");
                    }
                }
            }
        }

        changeUpdate();

        addLog("New resources are generated.");
    }

    void addLog(String log) {
        if (isActive())
            Global.addLog(log);
    }

    static Board deepCopy (Board board) {
        return (Board) PureBoard.deepCopy(board);
    }

    void syncPlayer(Player player) {
        player.getStructures().clear();

        // Structure assignment
        for (Structure structure : getStructures()) {
            if (player == structure.getPlayer()) {
                player.getStructures().add(structure);
            }
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        String text = "Turn: " + getTurn();

        for (Player player : players)
            text += player + "'s Resource: " + player.getResource();

        return text;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;

        changeUpdate();
    }

    public void changeUpdate() {
        if (isInitial) {
            for (Player player : players) {
                if (countStructures(StructureType.SETTLEMENT, player) > 2
                || countStructures(StructureType.SETTLEMENT, players.get(players.size()-1)) > 1
                || countStructures(StructureType.ROAD, player) > 2
                || countStructures(StructureType.ROAD, players.get(players.size()-1)) > 1) {
                    isInitial = false;
                    break;
                }
            }
        }

        State.StateBuilder stateBuilder = new State.StateBuilder(this);
        state = stateBuilder.build();
    }

    public State getState() {
        return state;
    }
}