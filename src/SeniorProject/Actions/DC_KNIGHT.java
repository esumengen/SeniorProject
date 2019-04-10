package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Land;
import SeniorProject.Player;

import java.io.Serializable;

public class DC_KNIGHT implements IAction, Serializable {
    Player player;
    Land land;
    Board board;

    public DC_KNIGHT(Player player, Land land, Board board) {
        this.player = player;
        this.land = land;
        this.board = board;
    }

    @Override
    public void execute() {
        board.useDevelopmentCard_KNIGHT(player, land);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
