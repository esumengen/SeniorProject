package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class UpgradeSettlement implements IAction, Serializable {
    public static final Resource COST = City.COST;

    Location location;
    Player player;
    Board board;

    public UpgradeSettlement(int locationIndex, int playerIndex, Board board) {
        this.location = board.getLocations().get(locationIndex);
        this.player = board.getPlayers().get(playerIndex);
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