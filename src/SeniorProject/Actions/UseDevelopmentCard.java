package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;
import SeniorProject.Player;

public class UseDevelopmentCard implements IAction {
    Player player;
    DevelopmentCardType developmentCardType;

    public UseDevelopmentCard(DevelopmentCardType developmentCardType, Player player) {
        this.developmentCardType = developmentCardType;
        this.player = player;
    }

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).useDevelopmentCard(developmentCardType, player);
    }

    @Override
    public String toString() {
        return player + " USE " + developmentCardType;
    }
}