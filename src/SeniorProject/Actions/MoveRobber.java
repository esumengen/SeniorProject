package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class MoveRobber implements IAction, Serializable {
    Land land;
    Player player;
    Player victim;
    ResourceType resourceType;
    Board board;

    public MoveRobber(Land land, Player player, Player victim, ResourceType resourceType, Board board) {
        this.land = land;
        this.player = player;
        this.victim = victim;
        this.resourceType = resourceType;
        this.board = board;
    }

    @Override
    public void execute () {
        board.moveRobber(player, land);
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String toString() {
        return player + " ROB " + land;
    }
}