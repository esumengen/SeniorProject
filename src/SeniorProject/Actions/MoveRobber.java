package SeniorProject.Actions;

import SeniorProject.*;

public class MoveRobber implements IAction {
    Land land;
    Player player;
    Player victim;
    ResourceType resourceType;

    public MoveRobber(Land land, Player player, Player victim, ResourceType resourceType) {
        this.land = land;
        this.player = player;
        this.victim = victim;
        this.resourceType = resourceType;
    }

    @Override
    public void execute () {
        ((Board) land.getPureBoard()).moveRobber(player, land, victim, resourceType);
    }
}