package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;

import java.io.Serializable;

public class MoveRobber implements IAction, Serializable {
    int landIndex;
    int playerIndex;
    int victimIndex;
    Board board;

    public MoveRobber(int landIndex, int playerIndex, int victimIndex, Board board) {
        this.landIndex = landIndex;
        this.playerIndex = playerIndex;
        this.victimIndex = victimIndex;
        this.board = board;
    }

    @Override
    public void execute() {
        board.moveRobber(landIndex, playerIndex, victimIndex);
    }

    @Override
    public String getCommand() {
        return board.getPlayers().get(playerIndex) + " [MO " + ((landIndex < 10) ? ("0" + landIndex) : landIndex) + " " + ((victimIndex < 10) ? (victimIndex == -1 ? victimIndex : ("0" + victimIndex)) : victimIndex) + "] T";
    }

    public int getLandIndex() {
        return landIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getVictimIndex() {
        return victimIndex;
    }

    @Override
    public String toString() {
        return board.getPlayers().get(playerIndex) + " ROB " + board.getLands().get(landIndex)/* + " VIC " + ((victimIndex == -1) ? "Nobody" : board.getPlayers().get(victimIndex))*/;
    }
}