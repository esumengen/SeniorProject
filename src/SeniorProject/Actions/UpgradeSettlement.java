package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

import java.io.Serializable;

public class UpgradeSettlement implements IAction, Serializable {
    Location location;
    Player player;
    Board board;

    public UpgradeSettlement(Location location, Player player, Board board) {
        this.location = location;
        this.player = player;
        this.board = board;
    }

    @Override
    public void execute() {
        board.upgradeSettlement(player, location);
    }

    @Override
    public String getCommand() {
        return "P" + (player.getIndex() + 1) + " [UP " + ((location.getIndex() < 10) ? ("0" + location.getIndex()) : location.getIndex()) + "] S";
    }

    @Override
    public String toString() {
        return player + " UPG " + location;
    }
}