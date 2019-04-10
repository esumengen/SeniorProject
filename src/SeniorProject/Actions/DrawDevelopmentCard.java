package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;

import java.io.Serializable;

public class DrawDevelopmentCard implements IAction, Serializable {
    Player player;
    Board board;

    public DrawDevelopmentCard(Player player, Board board) {
        this.player = player;
        this.board = board;
    }

    @Override
    public void execute () {
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