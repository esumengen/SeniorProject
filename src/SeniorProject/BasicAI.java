package SeniorProject;

import DevelopmentCards.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BasicAI implements AI, Serializable {
    public enum MoveType {
        CreateSettlement, CreateRoad, UpgradeSettlement, DevelopmentCard, TradeBank, TradePlayer, KnightCard, RoadBuildingCard, YearOfPlentyCard, MonopolyCard
    }

    private Player owner;
    private Board board;
    private String result = "";
    private Board virtualBoard;

    private Random randomGenerator = new Random();

    public BasicAI(Player player, Board board) {
        this.owner = player;
        this.board = board;
    }

    public void clearVirtualBoards() {
        virtualBoard = null;
        System.gc();
    }

    public String createMoves(boolean isInitial) {
        clearVirtualBoards();
        this.virtualBoard = Board.deepCopy(board);
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        virtualBoard.setActive(false);
        result = "";

        ArrayList<MoveType> moves = new ArrayList<>();

        if (isInitial) {
            moves.add(MoveType.CreateSettlement);
            moves.add(MoveType.CreateRoad);
        } else {
            if (Board.isAffordable(MoveType.CreateSettlement, virtualOwner))
                moves.add(MoveType.CreateSettlement);
            if (Board.isAffordable(MoveType.CreateRoad, virtualOwner))
                moves.add(MoveType.CreateRoad);
            if (Board.isAffordable(MoveType.UpgradeSettlement, virtualOwner))
                moves.add(MoveType.UpgradeSettlement);
            if (Board.isAffordable(MoveType.TradeBank, virtualOwner))
                moves.add(MoveType.TradeBank);
        }

        for (int i = 0; i < moves.size(); i++) {
            MoveType move = moves.get(i);

            boolean moveSuccess = false;
            switch (move) {
                case CreateSettlement:
                    moveSuccess = createSettlement_move(isInitial);

                    if (moveSuccess && !isInitial) {
                        owner.changeResource(ResourceType.BRICK, -1);
                        owner.changeResource(ResourceType.GRAIN, -1);
                        owner.changeResource(ResourceType.WOOL, -1);
                        owner.changeResource(ResourceType.LUMBER, -1);
                    }
                    break;

                case CreateRoad:
                    if (isInitial) {
                        Player virtualSelf = virtualBoard.getPlayers().get(owner.getIndex());

                        createRoad_move(((Settlement) virtualSelf.getStructures().get(virtualSelf.getStructures().size() - 1)).getLocation());
                    } else {
                        moveSuccess = createRoad_move();

                        if (moveSuccess) {
                            owner.changeResource(ResourceType.BRICK, -1);
                            owner.changeResource(ResourceType.LUMBER, -1);
                        }
                    }
                    break;

                case UpgradeSettlement:
                    moveSuccess = upgradeSettlement_move();

                    if (moveSuccess) {
                        owner.changeResource(ResourceType.ORE, -3);
                        owner.changeResource(ResourceType.GRAIN, -2);
                    }
                    break;

                case DevelopmentCard:
                    break;

                case TradeBank:
                    moveSuccess = tradeBank_move();
                    break;
            }

            moves.remove(i);
            i--;
        }

        return result;
    }

    private boolean createSettlement_move(boolean isInitial) {
        Location targetLocation;

        targetLocation = getMostValuableLocation(isInitial);

        if (targetLocation != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((targetLocation.getIndex() < 10) ? ("0" + targetLocation.getIndex()) : targetLocation.getIndex()) + "] S\r\n";
            virtualBoard.createSettlement(virtualBoard.getPlayers().get(owner.getIndex()), virtualBoard.getLocations().get(targetLocation.getIndex()));

            return true;
        }

        return false;
    }

    private boolean upgradeSettlement_move() {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        int count = 0;
        while (count < 5000) {
            Structure structure = virtualOwner.getStructures().get(Global.randomGenerator.nextInt(virtualOwner.getStructures().size()));

            if (structure instanceof Settlement) {
                result += "P" + (owner.getIndex() + 1) + " [UP " + ((((Settlement) structure).getLocation().getIndex() < 10) ? ("0" + ((Settlement) structure).getLocation().getIndex()) : ((Settlement) structure).getLocation().getIndex()) + "] S\r\n";
                virtualBoard.upgradeSettlement(virtualOwner, ((Settlement) structure).getLocation());

                return true;
            }

            count++;
        }

        return false;
    }

    private boolean tradeBank_move() {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        HashMap<ResourceType, Integer> localResources = new HashMap<>(virtualOwner.getResources());

        int grain = 0;
        int lumber = 0;
        int wool = 0;
        int ore = 0;
        int brick = 0;

        for (ResourceType resourceType:ResourceType.values()) {
            ResourceType targetResource = (Global.randomGenerator.nextInt(2) == 0) ? ResourceType.BRICK : ResourceType.LUMBER;

            if (resourceType != ResourceType.BRICK && resourceType != ResourceType.LUMBER && localResources.get(resourceType) > 6 && resourceType != targetResource) {
                int grainChange = (targetResource == ResourceType.GRAIN ? 1 : 0)-(resourceType == ResourceType.GRAIN ? 4 : 0);
                grain += grainChange;
                localResources.put(ResourceType.GRAIN, localResources.get(ResourceType.GRAIN)+grainChange);

                int lumberChange = (targetResource == ResourceType.LUMBER ? 1 : 0)-(resourceType == ResourceType.LUMBER ? 4 : 0);
                lumber += (targetResource == ResourceType.LUMBER ? 1 : 0)-(resourceType == ResourceType.LUMBER ? 4 : 0);
                localResources.put(ResourceType.LUMBER, localResources.get(ResourceType.LUMBER)+lumberChange);

                int woolChange = (targetResource == ResourceType.WOOL ? 1 : 0)-(resourceType == ResourceType.WOOL ? 4 : 0);
                wool += (targetResource == ResourceType.WOOL ? 1 : 0)-(resourceType == ResourceType.WOOL ? 4 : 0);
                localResources.put(ResourceType.WOOL, localResources.get(ResourceType.WOOL)+woolChange);

                int oreChange = (targetResource == ResourceType.ORE ? 1 : 0)-(resourceType == ResourceType.ORE ? 4 : 0);
                ore += (targetResource == ResourceType.ORE ? 1 : 0)-(resourceType == ResourceType.ORE ? 4 : 0);
                localResources.put(ResourceType.ORE, localResources.get(ResourceType.ORE)+oreChange);

                int brickChange = (targetResource == ResourceType.BRICK ? 1 : 0)-(resourceType == ResourceType.BRICK ? 4 : 0);
                brick += (targetResource == ResourceType.BRICK ? 1 : 0)-(resourceType == ResourceType.BRICK ? 4 : 0);
                localResources.put(ResourceType.BRICK, localResources.get(ResourceType.BRICK)+brickChange);
            }
        }

        if (grain+lumber+wool+ore+brick != 0) {
            int takenGrain = localResources.get(ResourceType.GRAIN) - virtualOwner.getResources().get(ResourceType.GRAIN);
            takenGrain = takenGrain < 0 ? 0 : takenGrain;

            int takenLumber = localResources.get(ResourceType.LUMBER) - virtualOwner.getResources().get(ResourceType.LUMBER);
            takenLumber = takenLumber < 0 ? 0 : takenLumber;

            int takenWool = localResources.get(ResourceType.WOOL) - virtualOwner.getResources().get(ResourceType.WOOL);
            takenWool = takenWool < 0 ? 0 : takenWool;

            int takenOre = localResources.get(ResourceType.ORE) - virtualOwner.getResources().get(ResourceType.ORE);
            takenOre = takenOre < 0 ? 0 : takenOre;

            int takenBrick = localResources.get(ResourceType.BRICK) - virtualOwner.getResources().get(ResourceType.BRICK);
            takenBrick = takenBrick < 0 ? 0 : takenBrick;

            grain = grain > 0 ? 0 : -grain;
            lumber = lumber > 0 ? 0 : -lumber;
            wool = wool > 0 ? 0 : -wool;
            ore = ore > 0 ? 0 : -ore;
            brick = brick > 0 ? 0 : -brick;

            result += "P" + (owner.getIndex() + 1)
                    + " [TR " + ((grain < 10) ? ("0" + grain) : grain)
                    + " " + ((lumber < 10) ? ("0" + lumber) : lumber)
                    + " " + ((wool < 10) ? ("0" + wool) : wool)
                    + " " + ((ore < 10) ? ("0" + ore) : ore)
                    + " " + ((brick < 10) ? ("0" + brick) : brick)
                    + " " + ((takenGrain < 10) ? ("0" + takenGrain) : takenGrain)
                    + " " + ((takenLumber < 10) ? ("0" + takenLumber) : takenLumber)
                    + " " + ((takenWool < 10) ? ("0" + takenWool) : takenWool)
                    + " " + ((takenOre < 10) ? ("0" + takenOre) : takenOre)
                    + " " + ((takenBrick < 10) ? ("0" + takenBrick) : takenBrick)
                    + "] B\r\n";

            virtualBoard.tradeBank(virtualOwner.getIndex(), grain, lumber, wool, ore, brick
                    , takenGrain
                    , takenLumber
                    , takenWool
                    , takenOre
                    , takenBrick);

            return true;
        }

        return false;
    }

    private boolean developmentCard_move() {
        return false;
    }

    private Location getMostValuableLocation() {
        return getMostValuableLocation(false);
    }

    private Location getMostValuableLocation(boolean isInitial) {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        Location mostValuableLocation = null;
        double maxValue = 0;

        for (Location location : virtualBoard.getLocations()) {
            Settlement settlement_temp = new Settlement(location, virtualOwner);

            double value = 0;
            if (virtualBoard.isValid(settlement_temp, isInitial)) {
                for (Land land : location.getAdjacentLands()) {
                    value += land.getDiceChance();
                }

                if (value > maxValue) {
                    maxValue = value;
                    mostValuableLocation = location;
                }
            }
        }

        return mostValuableLocation;
    }

    private boolean createRoad_move() {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        ArrayList<Location> possibleLocations = new ArrayList<>();
        for (int i = 0; i < virtualOwner.getStructures().size(); i++) {
            if (virtualOwner.getStructures().get(i) instanceof Building) {
                possibleLocations.add(((Building) virtualOwner.getStructures().get(i)).getLocation());
            }
            else if (virtualOwner.getStructures().get(i) instanceof Road) {
                possibleLocations.add(((Road) virtualOwner.getStructures().get(i)).getStartLocation());
                possibleLocations.add(((Road) virtualOwner.getStructures().get(i)).getEndLocation());
            }
        }

        Location startLocation = null;
        Location endLocation = null;
        Road road_temp = null;

        int count = 0;
        while (count < 5000) {
            startLocation = possibleLocations.get(Global.randomGenerator.nextInt(possibleLocations.size()));
            endLocation = startLocation.getAdjacentLocations().get(randomGenerator.nextInt(startLocation.getAdjacentLocations().size()));
            road_temp = new Road(startLocation, endLocation, virtualOwner);

            if (virtualBoard.isValid(road_temp)) {
                break;
            }
            else {
                startLocation = null;
                endLocation = null;
                road_temp = null;
            }

            count++;
        }

        if (road_temp != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((startLocation.getIndex() < 10) ? ("0" + startLocation.getIndex()) : startLocation.getIndex()) + " " + ((endLocation.getIndex() < 10) ? ("0" + endLocation.getIndex()) : endLocation.getIndex()) + "] R\r\n";
            virtualBoard.createRoad(virtualOwner, startLocation, endLocation);

            return true;
        }

        return false;
    }

    private boolean createRoad_move(Location location) {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());

        Road road_temp;

        while (true) {
            ArrayList<Location> adjacentList = location.getAdjacentLocations();
            Location secondLocation = adjacentList.get(Math.abs(randomGenerator.nextInt()) % adjacentList.size());

            if (!secondLocation.hasOwner()) {
                road_temp = new Road(location, secondLocation, virtualOwner);
                break;
            }
        }

        int roadStart = road_temp.getStartLocation().getIndex();
        int roadEnd = road_temp.getEndLocation().getIndex();

        if (road_temp != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((roadStart < 10) ? ("0" + roadStart) : roadStart) + " " + ((roadEnd < 10) ? ("0" + roadEnd) : roadEnd) + "] R\r\n";
            return true;
        }

        return false;
    }

    public boolean isPHMove() {
        return owner.getStructures().size() == 2;
    }

    public void useMonopoly_move() {
        ResourceType chosenType = chooseResourceType();
        result += "P" + (owner.getIndex() + 1) + " [DC "+ "MP" + "] " + chosenType.ordinal() + "\r\n";

        for (Player player : virtualBoard.getPlayers()) {
            if(player.getIndex() != owner.getIndex()) {
                if (player.getResources().get(chosenType) > 0) {
                    player.getResources().put(chosenType, player.getResources().get(chosenType) - 1);
                    virtualBoard.getPlayers().get(owner.getIndex()).getResources().put(chosenType, virtualBoard.getPlayers().get(owner.getIndex()).getResources().get(chosenType) + 1);
                }
            }
        }
    }

    private ResourceType chooseResourceType() {
        // TODO: 16-Mar-19

        return ResourceType.BRICK;
    }

    public void useRoadBuild() {
        // TODO: 24-Dec-18  
    }

    public void useYearOfPlenty() {
        // TODO: 24-Dec-18
    }

    public void useKnight() {
        // TODO: 25-Dec-18
    }
}