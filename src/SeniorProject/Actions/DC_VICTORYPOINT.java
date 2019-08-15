package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;

import java.io.Serializable;

public class DC_VICTORYPOINT implements IAction, Serializable {
    int playerIndex;
    Board board;

    public DC_VICTORYPOINT(int playerIndex, Board board) {
        this.playerIndex = playerIndex;
        this.board = board;
    }
    @Override
    public void execute() {
        return;
    }

    @Override
    public String getCommand() {
        return null;
    }
}
