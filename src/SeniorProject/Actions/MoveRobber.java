package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class MoveRobber implements IAction, Serializable {
    int landIndex;
    int playerIndex;
    int victimIndex;
    ResourceType resourceType;
    Board board;

    public MoveRobber(int landIndex, int playerIndex, int victimIndex, ResourceType resourceType, Board board) {
        this.landIndex = landIndex;
        this.playerIndex = playerIndex;
        this.victimIndex = victimIndex;
        this.resourceType = resourceType;
        this.board = board;
    }

    @Override
    public void execute() {
        board.moveRobber(landIndex, playerIndex, victimIndex, resourceType);
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String toString() {
        return board.getPlayers().get(playerIndex) + " ROB " + board.getLands().get(landIndex);
    }
}