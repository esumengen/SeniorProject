package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.ResourceType;

public class DC_MONOPOLY implements IAction {
    Player player;
    ResourceType resourceType;

    public DC_MONOPOLY(Player player, ResourceType resourceType) {
        this.player = player;
        this.resourceType = resourceType;
    }

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).useDevelopmentCard_MONOPOLY(player,resourceType);
    }
}
