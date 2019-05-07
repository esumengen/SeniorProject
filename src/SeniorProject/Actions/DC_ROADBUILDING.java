package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

import java.io.Serializable;

public class DC_ROADBUILDING implements IAction, Serializable {
    Player player;
    Location loc1, loc2, loc3, loc4;
    Board board;

    public DC_ROADBUILDING(Player player, Location loc1, Location loc2, Location loc3, Location loc4, Board board) {
        this.player = player;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.loc3 = loc3;
        this.loc4 = loc4;
        this.board = board;
    }

    @Override
    public void execute() {
        board.useDevelopmentCard_ROADBUILDING(player, loc1, loc2, loc3, loc4);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
