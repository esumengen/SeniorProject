package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Land;
import SeniorProject.Player;

public class DC_KNIGHT implements IAction {
    Player player;
    Land land;
    Player victim;

    public DC_KNIGHT(Player player, Land land, Player victim) {
        this.player = player;
        this.land = land;
        this.victim = victim;
    }

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).useDevelopmentCard_KNIGHT(player, land, victim);
    }
}
