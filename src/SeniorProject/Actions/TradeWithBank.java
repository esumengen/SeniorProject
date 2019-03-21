package SeniorProject.Actions;

import SeniorProject.*;
import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.util.ArrayList;
import java.util.Map;

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
}