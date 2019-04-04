package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.Resource;

public class TradeWithBank implements IAction {
    Player player;
    Resource givenResources;
    Resource takenResources;

    public TradeWithBank(Resource givenResources, Resource takenResources, Player player) {
        this.givenResources = givenResources;
        this.takenResources = takenResources;
        this.player = player;
    }

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).tradeBank(player.getIndex(), givenResources, takenResources);
    }

    @Override
    public String toString() {
        return player + " TRA";
    }

}