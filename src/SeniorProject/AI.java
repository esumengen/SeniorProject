package SeniorProject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class AI implements IAI{
    private Player owner;
    private Board board;
    private String result = "";
    private Board virtualBoard;

    private Random randomizer = new Random();

    public AI(Player player, Board board) {
        this.owner = player;
        this.board = board;
        this.virtualBoard = Board.deepCopy(board);
    }

    enum MoveType {
        CreateSettlement, CreateRoad
    }

    public String createMoves(boolean isInitial) {
        ArrayList<MoveType> moves = new ArrayList<>();

        if (isInitial) {
            moves.add(MoveType.CreateSettlement);
            //moves.add(MoveType.CreateRoad);
        }

        for (MoveType move : moves) {
            switch (move) {
                case CreateSettlement: {
                    createSettlement_move(isInitial);
                }

                case CreateRoad: {
                    if (isInitial) {
                        Player virtualSelf = virtualBoard.getPlayers().get(owner.getIndex());
                        createRoad_move(((Settlement) virtualSelf.getStructures().get(virtualSelf.getStructures().size()-1)).getLocation());
                    }
                    else
                        createRoad_move();
                }
            }
        }

        return result;
    }

    private boolean createSettlement_move(boolean isInitial) {
        Location targetLocation;

        targetLocation = getMostValuableLocation(isInitial);

        if (targetLocation != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((targetLocation.getIndex() < 10) ? ("0" + targetLocation.getIndex()) : targetLocation.getIndex()) + "] S\n";
            virtualBoard.createSettlement(virtualBoard.getPlayers().get(owner.getIndex()), virtualBoard.getLocations().get(targetLocation.getIndex()));

            return true;
        }

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
            if(virtualBoard.isValid(settlement_temp, isInitial)) {
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

    private boolean createRoad_move () {
        Road road_temp = null;

        int count = 0;
        while (count < 250) {
            Location startLocation = virtualBoard.getLocations().get(randomizer.nextInt() % virtualBoard.getLocations().size());
            if (!startLocation.isActive() && startLocation.getOwner() != owner)
                break;

            Location endLocation = startLocation.getAdjacentLocations().get(randomizer.nextInt() % startLocation.getAdjacentLocations().size());

            road_temp = new Road(startLocation, endLocation, owner);
            if (virtualBoard.isValid(road_temp)) {
                break;
            }

            count++;
        }

        int roadStart = road_temp.getStartLocation().getIndex();
        int roadEnd = road_temp.getEndLocation().getIndex();

        if (road_temp != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((roadStart < 10) ? ("0" + roadStart) : roadStart) + " " + ((roadEnd < 10) ? ("0" + roadEnd) : roadEnd) + "] R\n";
            virtualBoard.createRoad(virtualBoard.getPlayers().get(owner.getIndex()), virtualBoard.getLocations().get(roadStart), virtualBoard.getLocations().get(roadEnd));

            return true;
        }

        return false;
    }

    private boolean createRoad_move (Location location) {
        Road road_temp = null;

        while (true) {
            ArrayList<Location> adjacentList = location.getAdjacentLocations();
            Location secondLocation = adjacentList.get(randomizer.nextInt() % adjacentList.size());

            if (!secondLocation.hasOwner()) {
                road_temp = new Road(location, secondLocation, owner);
                break;
            }
        }

        int roadStart = road_temp.getStartLocation().getIndex();
        int roadEnd = road_temp.getEndLocation().getIndex();

        if (road_temp != null) {
            result += "P" + (owner.getIndex() + 1) + " [CR " + ((roadStart < 10) ? ("0" + roadStart) : roadStart) + " " + ((roadEnd < 10) ? ("0" + roadEnd) : roadEnd) + "] R\n";
            return true;
        }

        return false;
    }

    public boolean isPHMove() {
        return owner.getStructures().size() == 2;
    }
}
