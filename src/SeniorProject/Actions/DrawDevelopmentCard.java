package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;

import java.io.Serializable;

public class DrawDevelopmentCard implements IAction, Serializable {
    Player player;
    Board board;

    public DrawDevelopmentCard(int playerIndex, Board board) {
        this.player = board.getPlayers().get(playerIndex);
        this.board = board;
    }

    @Override
    public void execute() {
        board.drawDevelopmentCard(player);
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String toString() {
        return player + " DRAW";
    }
}