package SeniorProject;

import DevelopmentCards.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BasicAI implements AI, Serializable {
    public enum MoveType {
        CreateSettlement, CreateRoad, UpgradeSettlement, DevelopmentCard, Trade, KnightCard, RoadBuildingCard, YearOfPlentyCard, MonopolyCard
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

        virtualBoard.setActive(false);
        result = "";

        ArrayList<MoveType> moves = new ArrayList<>();

        if (isInitial) {
            moves.add(MoveType.CreateSettlement);
            moves.add(MoveType.CreateRoad);
        } else {
            if (Board.isAffordable(MoveType.CreateSettlement, owner))
                moves.add(MoveType.CreateSettlement);
            if (Board.isAffordable(MoveType.CreateRoad, owner))
                moves.add(MoveType.CreateRoad);
            if (Board.isAffordable(MoveType.UpgradeSettlement, owner))
                moves.add(MoveType.UpgradeSettlement);
            /*if (Board.isAffordable(MoveType.DevelopmentCard, owner))
                moves.add(MoveType.DevelopmentCard);*/
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
        while (count < 9999) {
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
        while (count < 999999) {
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

    public void useMonopoly() {
        // TODO: 24-Dec-18
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