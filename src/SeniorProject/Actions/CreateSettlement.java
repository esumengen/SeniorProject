package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

import java.io.Serializable;

public class CreateSettlement implements IAction, Serializable {
    Location location;
    Player player;
    Board board;

    public CreateSettlement (Location location, Player player, Board board) {
        this.location = location;
        this.player = player;
        this.board = board;
    }

    @Override
    public void execute () {
        board.createSettlement(player, location);
    }

    @Override
    public String toString() {
        return player + " SE " + location;
    }

    @Override
    public String getCommand() {
        return "P" + (player.getIndex() + 1) + " [CR " + ((location.getIndex() < 10) ? ("0" + location.getIndex()) : location.getIndex()) + "] S";
    }
}