package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class CreateRoad implements IAction, Serializable {
    Player player;
    Board board;
    private Location[] locations = new Location[2];

    public CreateRoad(int[] locationIndexes, int playerIndex, Board board) {
        this.locations[0] = board.getLocations().get(locationIndexes[0]);
        this.locations[1] = board.getLocations().get(locationIndexes[1]);
        this.board = board;
        this.player = board.getPlayers().get(playerIndex);
    }

    public Location[] getLocations() {
        return locations;
    }

    public Player getPlayer() {
        return player;
    }

    public Road getRoad() {
        return new Road(locations[0], locations[1], player);
    }

    @Override
    public void execute() {
        board.createRoad(player, locations[0], locations[1]);
    }

    @Override
    public String getCommand() {
        return "P" + (player.getIndex() + 1) + " [CR " + ((locations[0].getIndex() < 10) ? ("0" + locations[0].getIndex()) : locations[0].getIndex()) + " " + ((locations[1].getIndex() < 10) ? ("0" + locations[1].getIndex()) : locations[1].getIndex()) + "] R";
    }

    @Override
    public String toString() {
        return player + " RO " + locations[0] + " -> " + locations[1];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CreateRoad) {
            return ((CreateRoad) obj).getPlayer().getIndex() == getPlayer().getIndex()
                    &&
                    (((CreateRoad) obj).getLocations()[0].getIndex() == getLocations()[0].getIndex() && ((CreateRoad) obj).getLocations()[1].getIndex() == getLocations()[1].getIndex()
                    || ((CreateRoad) obj).getLocations()[0].getIndex() == getLocations()[1].getIndex() && ((CreateRoad) obj).getLocations()[1].getIndex() == getLocations()[0].getIndex());
        }

        return false;
    }
}