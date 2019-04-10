package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

import java.io.Serializable;

public class CreateRoad implements IAction, Serializable {
    private Location[] locations;
    Player player;
    Board board;

    public CreateRoad(Location[] locations, Player player, Board board) {
        this.locations = locations;
        this.player = player;
        this.board = board;
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
        return player + " RO " + locations[0] + " -> " + locations[1];
    }
}