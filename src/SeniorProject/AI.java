package SeniorProject;

import java.util.Random;

public class AI implements I_AI{
    private Player owner;
    private Board board;

    public AI(Player player, Board board) {
        this.owner = player;
        this.board = board;
    }

    public String createMoves(boolean isInitial) {
        String moves = "";
        if(isInitial) {
            Location selectedLocation = selectSettlementLocation(isInitial);
            moves += "P" + (owner.getIndex() + 1) + " [CR " + ((selectedLocation.getIndex()< 10) ? ("0" + selectedLocation.getIndex())
                    : selectedLocation.getIndex()) + "] S\n";
            Road selectedRoad = initialSelectedRoad(selectedLocation);
            int roadStart = selectedRoad.getStartLocation().getIndex();
            int roadEnd = selectedRoad.getEndLocation().getIndex();
            moves += "P" + (owner.getIndex() + 1) + " [CR " + ((roadStart< 10) ? ("0" + roadStart) : roadStart) + " " + ((roadEnd< 10) ? ("0" + roadEnd) : roadEnd) +"] R\n";
        }
        return moves;
    }

    private Location selectSettlementLocation(boolean isInitial) {
        Location selectedLocation = new Location(-1);
        if(isInitial && isPHMove(isInitial)){
            double maxScore = 0.0;
            boolean containsBrick = false;
            for (Location location : board.getLocations()) {
                Settlement settlement = new Settlement(selectedLocation, owner);
                double score = 0.0;
                if(board.isValid(settlement)){
                    for (Land land : location.getAdjacentLands()) {
                        score += land.getDiceChance();
                        if(land.getType().equals(LandType.HILLS))
                            containsBrick = true;
                    }
                    if (score > maxScore && containsBrick) {
                        maxScore = score;
                        selectedLocation = location;
                    }
                    containsBrick = false;
                }
            }
            return selectedLocation;
        }
        else if(isInitial)   {
            double maxScore = 0.0;
            for (Location location : board.getLocations()) {
                double score = 0.0;
                if(!location.hasOwner()){
                    for (Land land : location.getAdjacentLands()) {
                        score += land.getDiceChance();
                    }
                    if (score > maxScore) {
                        maxScore = score;
                        selectedLocation = location;
                    }
                }
            }
            return selectedLocation;
        }
        else {
                while (true) {
                    int index = new Random(board.getLocations().size()).nextInt();
                    if(!board.getLocations().get(index).hasOwner() && board.getLocations().get(index).isActive() ) {
                        if(!board.getLocations().get(index).getAdjacentLocations().get(0).hasOwner() &&
                                !board.getLocations().get(index).getAdjacentLocations().get(1).hasOwner() &&
                                !board.getLocations().get(index).getAdjacentLocations().get(2).hasOwner()){
                            selectedLocation = board.getLocations().get(index);
                            break;
                        }
                    }
                }
            }
        return selectedLocation;
    }

    private Road initialSelectedRoad(Location location) {
        for (Location adjecentLocation : location.getAdjacentLocations()){
            if(!adjecentLocation.hasOwner()){
               return new Road(location, adjecentLocation, owner);
            }
        }
        return null;
    }

    public boolean isPHMove(boolean isInitial) {
        return isInitial && owner.getStructures().size() == 2;
    }
}
