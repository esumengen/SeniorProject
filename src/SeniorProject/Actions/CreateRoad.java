package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class CreateRoad implements IAction, Serializable {
    private Location[] locations = new Location[2];
    Player player;
    Board board;

    public CreateRoad(int[] locationIndexes, int playerIndex, Board board) {
        this.locations[0] = board.getLocations().get(locationIndexes[0]);
        this.locations[1] = board.getLocations().get(locationIndexes[1]);
        this.board = board;
        this.player = board.getPlayers().get(playerIndex);
    }

    @Override
    public void execute () {
        board.createRoad(player, locations[0], locations[1]);
    }

    @Override
    public String getCommand() {
        return "P" + (player.getIndex() + 1) + " [CR " + ((locations[0].getIndex() < 10) ? ("0" + locations[0].getIndex()) : locations[0].getIndex()) + " " + ((locations[1].getIndex() < 10) ? ("0" + locations[1].getIndex()) : locations[1].getIndex()) + "] R";
    }

    @Override
    public String toString() {
        return player + " RO " + locations[0].getIndex() + " -> " + locations[1].getIndex();
    }
}