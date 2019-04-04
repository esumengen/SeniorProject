package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.ResourceType;

public class DC_YEAROFPLENTY implements IAction {
    Player player;
    ResourceType resourceType1;
    ResourceType resourceType2;

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).useDevelopmentCard_YEAROFPLENTY(player,resourceType1, resourceType2);
    }
}
