package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.ResourceType;

import java.io.Serializable;

public class DC_MONOPOLY implements IAction, Serializable {
    Player player;
    ResourceType resourceType;
    Board board;

    public DC_MONOPOLY(Player player, ResourceType resourceType, Board board) {
        this.player = player;
        this.resourceType = resourceType;
        this.board = board;
    }

    @Override
    public void execute() {
        board.useDevelopmentCard_MONOPOLY(player,resourceType);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
