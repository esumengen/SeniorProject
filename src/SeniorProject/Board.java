package SeniorProject;

import SeniorProject.Actions.*;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.Negotiation.NegotiationAgent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class Board extends PureBoard implements Serializable {
    private ArrayList<Player> players;
    private int turn;
    private boolean isMain;
    private State state;
    private boolean isInitial;
    private int totalDice;
    private Player diceOwner;
    private boolean hasRobberPlayedRecently;

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

    static boolean isAffordable(MoveType type, Resource resource) {
        Player player = new Player(2);
        player.setResource(resource);

        return isAffordable(type, player);
    }

    static boolean isAffordable(MoveType type, Player player) {
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

                return brick / 4 + grain / 4 + wool / 4 + ore / 4 + lumber / 4 != 0;
            case KnightCard:
                return player.getDevelopmentCards().contains(DC_KNIGHT.class);
            case MonopolyCard:
                return player.getDevelopmentCards().contains(DC_MONOPOLY.class);
            case RoadBuildingCard:
                return player.getDevelopmentCards().contains(DC_ROADBUILDING.class);
            case YearOfPlentyCard:
                return player.getDevelopmentCards().contains(DC_YEAROFPLENTY.class);
        }

        return false;
    }

    static Board deepCopy(Board board) {
        ArrayList<AI> AIs = new ArrayList<>();
        ArrayList<NegotiationAgent> agents = new ArrayList<>();

        for (Player player : board.getPlayers()) {
            AIs.add(player.getAI());
            agents.add(player.getNegotiationAgent());
            player.setAI(null);
            player.setNegotiationAgent(null);
        }
        System.gc();

        Board _board = (Board) PureBoard.deepCopy(board);
        if (_board != null)
            _board.setMain(false);

        for (Player player : board.getPlayers()) {
            player.setAI(AIs.get(player.getIndex()));
            player.setNegotiationAgent(agents.get(player.getIndex()));
        }

        return _board;
    }

    public void createRoad(Player player, Location location_first, Location location_second) {
        Road road = new Road(location_first, location_second, player);
        location_first.addConnectedRoad(road);
        location_second.addConnectedRoad(road);

        getStructures().add(road);

        location_first.addStructures(road);
        location_second.addStructures(road);

        if (!isInitial) {
            player.getResource().add(ResourceType.BRICK, -1);
            player.getResource().add(ResourceType.LUMBER, -1);
        }

        syncPlayer(player);
        changeUpdate();

        location_first.addAdjacentNodes_player(player.getIndex(), location_second);
        location_second.addAdjacentNodes_player(player.getIndex(), location_first);

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

        if (!isInitial) {
            player.getResource().add(ResourceType.GRAIN, -2);
            player.getResource().add(ResourceType.ORE, -3);
        }

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A settlement has been upgraded on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");
    }

    public void moveRobber(int landIndex, int playerIndex, int victimIndex) {
        // victimIndex can be -1.

        setRobbedLand(getLands().get(landIndex));

        /*if (victimIndex != -1) {
            Random random = new Random();
            ResourceType resourceType = null;
            if (getPlayers().get(victimIndex).getResource().getSum() != 0) {
                int count = 0;

                while (count <= 0) {
                    resourceType = ResourceType.values()[random.nextInt(ResourceType.values().length)];

                    count = getPlayers().get(victimIndex).getResource().get(resourceType);
                }
            }

            if (resourceType != null) {
                getPlayers().get(playerIndex).getResource().add(resourceType, 1);
                getPlayers().get(victimIndex).getResource().add(resourceType, -1);
            }
        }*/

        hasRobberPlayedRecently = true;

        syncPlayer(getPlayers().get(playerIndex));
        changeUpdate();

        addLog("ACTION: The robber has been moved to [Land " + getLands().get(landIndex).getIndex() + "] by [Player " + (playerIndex + 1) + "]");
    }

    public void drawDevelopmentCard(Player player) {
        DevelopmentCardType developmentCardType = getDeck().pickDevelopmentCard();
        player.addDevelopmentCard(developmentCardType);

        if (!isInitial) {
            player.getResource().add(ResourceType.WOOL, -1);
            player.getResource().add(ResourceType.GRAIN, -1);
            player.getResource().add(ResourceType.ORE, -1);
        }

        if(developmentCardType == DevelopmentCardType.VICTORYPOINT)
            new DC_VICTORYPOINT(player.getIndex(), this).execute();

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A development card(" + developmentCardType + ") is drawn by [Player " + (player.getIndex() + 1) + "]");
    }

    public void useDevelopmentCard_KNIGHT(int landIndex, int playerIndex, int victimIndex) {
        getPlayers().get(playerIndex).setKnight(getPlayers().get(playerIndex).getKnight() + 1);
        moveRobber(landIndex, playerIndex, victimIndex);

        syncPlayer(players.get(playerIndex));
        changeUpdate();

        addLog("ACTION: Knight card is used by [Player " + (playerIndex + 1) + "]");
    }

    public void useDevelopmentCard_MONOPOLY(int playerIndex, ResourceType resourceType) {
        for (Player _player : players) {
            if (_player.getIndex() != playerIndex && _player.getResource().get(resourceType) > 0) {
                _player.getResource().add(resourceType, -1);
                players.get(playerIndex).getResource().add(resourceType, 1);
            }
        }

        syncPlayer(players.get(playerIndex));
        changeUpdate();

        addLog("ACTION: Monopoly card is used by [Player " + (playerIndex + 1) + "]");
    }

    public void useDevelopmentCard_ROADBUILDING(int playerIndex, Location loc1, Location loc2, Location loc3, Location loc4) {
        createRoad(players.get(playerIndex), loc1, loc2);
        createRoad(players.get(playerIndex), loc3, loc4);

        syncPlayer(players.get(playerIndex));
        changeUpdate();

        addLog("ACTION: Road Building card is used by [Player " + (playerIndex + 1) + "]");
    }

    public void useDevelopmentCard_VICTORYPOINT(int playerIndex) {
        players.get(playerIndex).setVictoryPoint(players.get(playerIndex).getVictoryPoint() + 1);

        syncPlayer(players.get(playerIndex));
        changeUpdate();

        addLog("ACTION: Victory Point card is used by [Player " + (playerIndex + 1) + "]");
    }

    public void useDevelopmentCard_YEAROFPLENTY(int playerIndex, ResourceType resourceType1, ResourceType resourceType2) {
        players.get(playerIndex).getResource().add(resourceType1, 1);
        players.get(playerIndex).getResource().add(resourceType2, 1);

        syncPlayer(players.get(playerIndex));
        changeUpdate();

        addLog("ACTION: Year Of Plenty card is used by [Player " + (playerIndex + 1) + "]");
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

    public void tradePlayer(int playerGiver, int playerTaker, Map<ResourceType, Integer> givenResources, Map<ResourceType, Integer> takenResources) {
        players.get(playerGiver).setGrain(players.get(playerGiver).getGrain() - givenResources.get(ResourceType.GRAIN)
                + takenResources.get(ResourceType.GRAIN));

        players.get(playerGiver).setLumber(players.get(playerGiver).getLumber() - givenResources.get(ResourceType.LUMBER)
                + takenResources.get(ResourceType.LUMBER));

        players.get(playerGiver).setWool(players.get(playerGiver).getWool() - givenResources.get(ResourceType.WOOL)
                + takenResources.get(ResourceType.WOOL));

        players.get(playerGiver).setOre(players.get(playerGiver).getOre() - givenResources.get(ResourceType.ORE)
                + takenResources.get(ResourceType.ORE));

        players.get(playerGiver).setBrick(players.get(playerGiver).getBrick() - givenResources.get(ResourceType.BRICK)
                + takenResources.get(ResourceType.BRICK));

        players.get(playerTaker).setGrain(players.get(playerTaker).getGrain() - takenResources.get(ResourceType.GRAIN)
                + givenResources.get(ResourceType.GRAIN));

        players.get(playerTaker).setLumber(players.get(playerTaker).getLumber() - takenResources.get(ResourceType.LUMBER)
                + givenResources.get(ResourceType.LUMBER));

        players.get(playerTaker).setWool(players.get(playerTaker).getWool() - takenResources.get(ResourceType.WOOL)
                + givenResources.get(ResourceType.WOOL));

        players.get(playerTaker).setOre(players.get(playerTaker).getOre() - takenResources.get(ResourceType.ORE)
                + givenResources.get(ResourceType.ORE));

        players.get(playerTaker).setBrick(players.get(playerTaker).getBrick() - takenResources.get(ResourceType.BRICK)
                + givenResources.get(ResourceType.BRICK));

        syncPlayer(players.get(playerGiver));
        syncPlayer(players.get(playerTaker));
        changeUpdate();

        addLog("ACTION: A trade with [Player " + (playerTaker + 1) + "] has been done by [Player " + (playerGiver + 1) + "]");
    }

    public void rollDice(Player player, int dice1, int dice2) {
        hasRobberPlayedRecently = false;
        totalDice = dice1 + dice2;
        diceOwner = player;

        generateResource(dice1 + dice2);

        addLog("ACTION: Dice are rolled " + dice1 + " " + dice2 + " by " + player);

        changeUpdate();
    }

    private void generateResource(int diceNo) {
        ArrayList<Resource> rewards = new ArrayList<>();
        for (int i = 0; i < Global.PLAYER_COUNT; i++)
            rewards.add(new Resource());

        for (Land land : getLands()) {
            if (land.getDiceNo() == diceNo && land != getRobbedLand()) {
                for (Location location : land.getAdjacentLocations()) {
                    if (location.hasOwner()) {
                        Player rewardedPlayer = location.getOwner();

                        int reward =  location.hasCity() ? 2 : 1;
                        rewardedPlayer.getResource().add(land.getResourceType(), reward);

                        Resource newCumulativeReward = new Resource(rewards.get(rewardedPlayer.getIndex()));
                        newCumulativeReward.put(land.getResourceType(), newCumulativeReward.get(land.getResourceType()) + reward);
                        rewards.set(rewardedPlayer.getIndex(), newCumulativeReward);
                    }
                }
            }
        }

        for (int i = 0; i < rewards.size(); i++) {
            /*if (isMain)
                System.out.println("Dice: " + getPlayers().get(i) + " has reached " + getPlayers().get(i).getResource() + ".");*/

            addLog("Dice: " + getPlayers().get(i) + " has gained " + rewards.get(i) + ".");
        }

        changeUpdate();
    }

    private void addLog(String log) {
        if (isActive())
            Global.addLog(log);
    }

    public void createSettlement(Player player, Location location) {
        int settlementCount = -1;
        if (isInitial)
            settlementCount = countStructures(StructureType.SETTLEMENT, player.getIndex());

        Settlement settlement = new Settlement(location, player);
        getStructures().add(settlement);

        location.setOwner(player);
        location.addStructures(settlement);

        // Immediate Harvesting
        if (settlementCount == 1) {
            for (Land land : location.getAdjacentLands())
                player.getResource().add(land.getResourceType(), 1);
        } else {
            if (!isInitial) {
                player.getResource().add(ResourceType.BRICK, -1);
                player.getResource().add(ResourceType.GRAIN, -1);
                player.getResource().add(ResourceType.WOOL, -1);
                player.getResource().add(ResourceType.LUMBER, -1);
            }
        }

        syncPlayer(player);
        changeUpdate();

        addLog("ACTION: A settlement has been added on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");
    }

    void syncPlayer(Player player) {
        player.getStructures().clear();

        // Structure assignment
        for (Structure structure : getStructures()) {
            if (player.getIndex() == structure.getPlayer().getIndex()) {
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

    int getTurn() {
        return turn;
    }

    void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isMain() {
        return isMain;
    }

    void setMain(boolean main) {
        isMain = main;
    }

    boolean isInitial() {
        return isInitial;
    }

    void setInitial(boolean initial) {
        if (isInitial != initial)
            changeUpdate();

        isInitial = initial;
    }

    void changeUpdate() {
        if (isInitial) {
            for (Player player : players) {
                if (countStructures(StructureType.SETTLEMENT, player.getIndex()) > 2
                        || countStructures(StructureType.ROAD, player.getIndex()) > 2) {
                    isInitial = false;
                    break;
                }
            }
        }

        State.StateBuilder stateBuilder = new State.StateBuilder(this);
        state = stateBuilder.build();
    }

    State getState() {
        return state;
    }

    private AbstractMap.SimpleEntry<Integer, Node> getFurthestNode(int playerIndex, Node node) {
        return getFurthestNode(playerIndex, node, false);
    }

    private AbstractMap.SimpleEntry<Integer, Node> getFurthestNode(int playerIndex, Node node, boolean debug) {
        ArrayList<Road> playersRoads = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();

        for (Structure structure : players.get(playerIndex).getStructures()) {
            if (structure instanceof Road)
                playersRoads.add((Road) structure);
        }

        for (Road road : playersRoads) {
            if (!nodes.contains(road.getEndLocation()))
                nodes.add(road.getEndLocation());

            if (!nodes.contains(road.getStartLocation()))
                nodes.add(road.getStartLocation());
        }

        Map<Node, Integer> distanceMap = new HashMap<>();

        for (Node _node : nodes)
            distanceMap.put(_node, _node.getIndex() == node.getIndex() ? 0 : -1);

        ArrayDeque<Node> stack = new ArrayDeque<>(500);
        stack.addFirst(node);

        ArrayDeque<ArrayList<Node>> chainStack = new ArrayDeque<>(500);
        chainStack.addFirst(new ArrayList<>());

        ArrayDeque<ArrayList<Pair<Integer, Integer>>> edgesStack = new ArrayDeque<>(500);
        edgesStack.addFirst(new ArrayList<>());

        while (!stack.isEmpty()) {
            Node _node = stack.removeFirst();
            ArrayList<Node> chain = chainStack.removeFirst();
            ArrayList<Pair<Integer, Integer>> edges = edgesStack.removeFirst();

            if (debug) {
                System.out.println("Taken Node: " + _node);
                System.out.println("Taken Chain: " + chain);
            }

            int newDistance = Math.max(distanceMap.get(_node), chain.size());
            distanceMap.put(_node, newDistance);

            ArrayList<Node> newChain = new ArrayList<>(chain);
            ArrayList<Pair<Integer, Integer>> newEdges = new ArrayList<>(edges);

            newChain.add(_node);
            if (chain.size() > 0) {
                newEdges.add(new ImmutablePair<>(_node.getIndex(), chain.get(chain.size() - 1).getIndex()));
                newEdges.add(new ImmutablePair<>(chain.get(chain.size() - 1).getIndex(), _node.getIndex()));
            }

            for (Node adjNode : _node.getAdjacentNodes_player(playerIndex)) {
                boolean doPush = false;

                if (!newEdges.contains(new ImmutablePair<>(_node.getIndex(), adjNode.getIndex()))) {
                    for (Node sec_adjNode : adjNode.getAdjacentNodes_player(playerIndex)) {
                        if (!chain.contains(sec_adjNode)) {
                            doPush = true;
                            break;
                        }
                    }
                }

                if (!newChain.contains(adjNode))
                    doPush = true;

                if (doPush) {
                    stack.addFirst(adjNode);
                    chainStack.addFirst(newChain);
                    edgesStack.addFirst(newEdges);

                    if (debug) {
                        System.out.println("    Added Node: " + adjNode);
                        System.out.println("    Added Chain: " + newChain);
                    }
                }
            }
        }

        int max = Integer.MIN_VALUE;
        Node maxNode = null;
        for (Node _node : distanceMap.keySet()) {
            int value = distanceMap.get(_node);
            if (value > max) {
                max = value;
                maxNode = _node;
            }
        }

        if (debug) {
            System.out.println("Max Distance: " + max);
            System.out.println("Max Node: " + maxNode);
        }

        return new AbstractMap.SimpleEntry<>(max, maxNode);
    }

    AbstractMap.SimpleEntry<Integer, Node> getLongestRoad(int playerIndex) {
        int structureCount = getPlayers().get(playerIndex).getStructures().size();
        if (structureCount < 4)
            return new AbstractMap.SimpleEntry<>((structureCount > 1) ? 1 : 0, new Node(-1));

        ArrayList<AbstractMap.SimpleEntry<Integer, Node>> results = new ArrayList<>();
        AbstractMap.SimpleEntry<Integer, Node> _r;

        for (Structure structure : getPlayers().get(playerIndex).getStructures()) {
            if (structure instanceof Road) {
                Road road = (Road) structure;

                if (road.getStartLocation().getConnectedRoads().size() == 1) {
                    _r = getFurthestNode(playerIndex, road.getStartLocation());
                    results.add(getFurthestNode(playerIndex, _r.getValue()));
                }

                if (road.getEndLocation().getConnectedRoads().size() == 1) {
                    _r = getFurthestNode(playerIndex, road.getEndLocation());
                    results.add(getFurthestNode(playerIndex, _r.getValue()));
                }
            }
        }

        _r = getFurthestNode(playerIndex, ((Building) getPlayers().get(playerIndex).getStructures().get(0)).getLocation());
        results.add(getFurthestNode(playerIndex, _r.getValue()));

        _r = getFurthestNode(playerIndex, ((Building) getPlayers().get(playerIndex).getStructures().get(2)).getLocation());
        results.add(getFurthestNode(playerIndex, _r.getValue()));

        int maxIndex = 0;
        int maxValue = 0;
        for (int i = 0; i < results.size(); i++) {
            SimpleEntry<Integer, Node> result = results.get(i);
            if (result.getKey() > maxValue) {
                maxIndex = i;
                maxValue = result.getKey();
            }
        }

        return results.get(maxIndex);
    }

    int getTotalDice() {
        return totalDice;
    }

    boolean hasRobberPlayedRecently() {
        return hasRobberPlayedRecently;
    }

    Player getDiceOwner() {
        return diceOwner;
    }
}