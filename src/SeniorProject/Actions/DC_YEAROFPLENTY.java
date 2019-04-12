package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.ResourceType;

import java.io.Serializable;

public class DC_YEAROFPLENTY implements IAction, Serializable {
    Player player;
    ResourceType resourceType1;
    ResourceType resourceType2;
    Board board;

    public DC_YEAROFPLENTY(Player player, ResourceType resourceType1, ResourceType resourceType2, Board board) {
        this.player = player;
        this.resourceType1 = resourceType1;
        this.resourceType2 = resourceType2;
        this.board = board;
    }

    @Override
    public void execute() {
        board.useDevelopmentCard_YEAROFPLENTY(player, resourceType1, resourceType2);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
