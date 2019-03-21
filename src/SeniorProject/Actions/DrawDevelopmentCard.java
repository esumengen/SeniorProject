package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

public class DrawDevelopmentCard implements IAction {
    Player player;

    public DrawDevelopmentCard(Player player) {
        this.player = player;
    }

    @Override
    public void execute () {
        ((Board) player.getPureBoard()).drawDevelopmentCard(player);
    }
}