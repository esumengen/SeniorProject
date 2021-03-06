package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class CreateSettlement implements IAction, Serializable {
    public static final Resource COST = Settlement.COST;

    Player player;
    Board board;
    private Location location;

    public CreateSettlement(int locationIndex, int playerIndex, Board board) {
        this.location = board.getLocations().get(locationIndex);
        this.player = board.getPlayers().get(playerIndex);
        this.board = board;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void execute() {
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