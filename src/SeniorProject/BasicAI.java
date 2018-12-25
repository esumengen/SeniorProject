package SeniorProject;

import DevelopmentCards.DevelopmentCard;
import DevelopmentCards.Monopoly;
import DevelopmentCards.RoadBuilding;
import DevelopmentCards.YearOfPlenty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BasicAI implements AI, Serializable {
    private Player owner;
    private Board board;
    private String result = "";
    private Board virtualBoard;
    private ArrayList<Boolean> possibleMoves;

    private Random randomGenerator = new Random();

    public BasicAI(Player player, Board board) {
        this.owner = player;
        this.board = board;

        this.possibleMoves = new ArrayList<>();
        for (int i = 0; i < MoveType.values().length; i++) {
            possibleMoves.add(false);
        }
    }

    void updatePossibleMoves() {
        Player virtualOwner = virtualBoard.getPlayers().get(owner.getIndex());
        for (int i = 0; i < possibleMoves.size(); i++) {
            MoveType type = MoveType.values()[i];

            switch (type) {
                case CreateSettlement: {
                    if (virtualOwner.getResources().get(ResourceType.BRICK) >= 1 && virtualOwner.getResources().get(ResourceType.GRAIN) >= 1
                            && virtualOwner.getResources().get(ResourceType.WOOL) >= 1 && virtualOwner.getResources().get(ResourceType.LUMBER) >= 1) {
                        possibleMoves.set(type.ordinal(), true);
                        break;
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case CreateRoad: {
                    if (virtualOwner.getResources().get(ResourceType.BRICK) >= 1 && virtualOwner.getResources().get(ResourceType.LUMBER) >= 1) {
                        possibleMoves.set(type.ordinal(), true);
                        break;
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case UpgradeSettlement: {
                    if (virtualOwner.getResources().get(ResourceType.ORE) >= 3 && virtualOwner.getResources().get(ResourceType.GRAIN) >= 2) {
                        possibleMoves.set(type.ordinal(), true);
                        break;
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case DevelopmentCard: {
                    if (virtualOwner.getResources().get(ResourceType.GRAIN) >= 1 && virtualOwner.getResources().get(ResourceType.WOOL) >= 1
                            && owner.getResources().get(ResourceType.ORE) >= 1) {
                        possibleMoves.set(type.ordinal(), true);
                        break;
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case Trade: {
                    possibleMoves.set(type.ordinal(), true);
                    break;
                }
                case KnightCard: {
                    if (virtualOwner.getKnight() > 0)
                        possibleMoves.set(type.ordinal(), true);

                    possibleMoves.set(type.ordinal(), false);
                }
                case UseMonopoly: {
                    for (DevelopmentCard card : owner.getDevelopmentCards()) {
                        if (card instanceof Monopoly) {
                            possibleMoves.set(type.ordinal(), true);
                            break;
                        }
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case UseRoadBuilding: {
                    for (DevelopmentCard card : owner.getDevelopmentCards()) {
                        if (card instanceof RoadBuilding) {
                            possibleMoves.set(type.ordinal(), true);
                            break;
                        }
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
                case UseYearOfPlenty: {
                    for (DevelopmentCard card : owner.getDevelopmentCards()) {
                        if (card instanceof YearOfPlenty) {
                            possibleMoves.set(type.ordinal(), true);
                            break;
                        }
                    }
                    possibleMoves.set(type.ordinal(), false);
                    break;
                }
            }
        }

    }

    public void clearVirtualBoards() {
        virtualBoard = null;
        System.gc();
    }

    public String createMoves(boolean isInitial) {
        clearVirtualBoards();
        this.virtualBoard = Board.deepCopy(board);
        virtualBoard.setActive(false);
        updatePossibleMoves();
        result = "";

        ArrayList<MoveType> moves = new ArrayList<>();

        if (isInitial) {
            moves.add(MoveType.CreateSettlement);
            moves.add(MoveType.CreateRoad);
        } else {

        }

        for (int i = 0; i < moves.size(); i++) {
            MoveType move = moves.get(i);

            switch (move) {
                case CreateSettlement:
                    createSettlement_move(isInitial);
                    break;

                case CreateRoad:
                    if (isInitial) {
                        Player virtualSelf = virtualBoard.getPlayers().get(owner.getIndex());
                        createRoad_move(((Settlement) virtualSelf.getStructures().get(virtualSelf.getStructures().size() - 1)).getLocation());
                    } else
                        createRoad_move();
                    break;

                case UpgradeSettlement:
                    upgradeSettlement_move();
                    break;

                case DevelopmentCard:
                    break;

                case Trade:
                    break;

                case KnightCard:
                    break;

                case UseRoadBuilding:
                    break;

                case UseYearOfPlenty:
                    break;

                case UseMonopoly:
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
        int count = 0;
        while (count < 100) {
            Structure structure = owner.getStructures().get(Math.abs(randomGenerator.nextInt(owner.getStructures().size())));

            if (structure instanceof Settlement) { result += "P" + (owner.getIndex() + 1) + " [CR " + ((((Settlement) structure).getLocation().getIndex() < 10) ? ("0" + ((Settlement) structure).getLocation().getIndex()) : ((Settlement) structure).getLocation().getIndex()) + "] S\r\n";
                virtualBoard.createSettlement(virtualBoard.getPlayers().get(owner.getIndex()), virtualBoard.getLocations().get(((Settlement) structure).getLocation().getIndex()));
                return true;
            }
            count++;
        }
        return false;
    }

    private boolean developmentCard_Move() {
        return false;
    }

    private Location getMostValuableLocation() {
        return getMostValuableLocation(false);
    }

    private Location getMostValuableLocation(boolean isInitial) {
        Location mostValuableLocation = null;
        double maxValue = 0;

        for (Location location : virtualBoard.getLocations()) {
            Settlement settlement_temp = new Settlement(location, owner);

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
        Road road_temp = null;

        int count = 0;
        while (count < 250) {
            Location startLocation = virtualBoard.getLocations().get(Math.abs(randomGenerator.nextInt()) % virtualBoard.getLocations().size());
            if (!startLocation.isActive() && startLocation.getOwner() != owner)
                break;

            Location endLocation = startLocation.getAdjacentLocations().get(Math.abs(randomGenerator.nextInt()) % startLocation.getAdjacentLocations().size());

            road_temp = new Road(startLocation, endLocation, owner);
            if (virtualBoard.isValid(road_temp)) {
                break;
            }

            count++;
        }

        int roadStart = road_temp.getStartLocation().getIndex();
        int roadEnd = road_temp.getEndLocation().getIndex();

        if (road_temp != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((roadStart < 10) ? ("0" + roadStart) : roadStart) + " " + ((roadEnd < 10) ? ("0" + roadEnd) : roadEnd) + "] R\r\n";
            virtualBoard.createRoad(virtualBoard.getPlayers().get(owner.getIndex()), virtualBoard.getLocations().get(roadStart), virtualBoard.getLocations().get(roadEnd));

            return true;
        }

        return false;
    }

    private boolean createRoad_move(Location location) {
        Road road_temp;

        while (true) {
            ArrayList<Location> adjacentList = location.getAdjacentLocations();
            Location secondLocation = adjacentList.get(Math.abs(randomGenerator.nextInt()) % adjacentList.size());

            if (!secondLocation.hasOwner()) {
                road_temp = new Road(location, secondLocation, owner);
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

    public ArrayList<Boolean> getPossibleMoves() {
        updatePossibleMoves();
        return possibleMoves;
    }

    public boolean isPHMove() {
        return owner.getStructures().size() == 2;
    }

    public void useKnight() {
    }

    public void useMonopoly() {
        // TODO: 24-Dec-18  
    }

    public void useRoadBuilding() {
        // TODO: 24-Dec-18  
    }

    public void useYearOfPlenty() {
    }

    public enum MoveType {
        CreateSettlement, CreateRoad, UpgradeSettlement, DevelopmentCard, Trade, KnightCard, UseMonopoly, UseRoadBuilding, UseYearOfPlenty;
    }

}
