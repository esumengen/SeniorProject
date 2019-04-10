package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class TradeWithBank implements IAction, Serializable {
    Player player;
    Resource givenResources;
    Resource takenResources;
    Board board;

    public TradeWithBank(Resource givenResources, Resource takenResources, Player player, Board board) {
        this.givenResources = givenResources;
        this.takenResources = takenResources;
        this.player = player;
        this.board = board;
    }

    @Override
    public void execute() {
        board.tradeBank(player.getIndex(), givenResources, takenResources);
    }

    @Override
    public String getCommand() {
        int grain = givenResources.get(ResourceType.GRAIN);
        int lumber = givenResources.get(ResourceType.LUMBER);
        int wool = givenResources.get(ResourceType.WOOL);
        int ore = givenResources.get(ResourceType.ORE);
        int brick = givenResources.get(ResourceType.BRICK);

        int takenGrain = takenResources.get(ResourceType.GRAIN);
        int takenLumber = takenResources.get(ResourceType.LUMBER);
        int takenWool = takenResources.get(ResourceType.WOOL);
        int takenOre = takenResources.get(ResourceType.ORE);
        int takenBrick = takenResources.get(ResourceType.BRICK);

        return "P" + (player.getIndex() + 1)
                + " [TR " + ((grain < 10) ? ("0" + grain) : grain)
                + " " + ((lumber < 10) ? ("0" + lumber) : lumber)
                + " " + ((wool < 10) ? ("0" + wool) : wool)
                + " " + ((ore < 10) ? ("0" + ore) : ore)
                + " " + ((brick < 10) ? ("0" + brick) : brick)
                + " " + ((takenGrain < 10) ? ("0" + takenGrain) : takenGrain)
                + " " + ((takenLumber < 10) ? ("0" + takenLumber) : takenLumber)
                + " " + ((takenWool < 10) ? ("0" + takenWool) : takenWool)
                + " " + ((takenOre < 10) ? ("0" + takenOre) : takenOre)
                + " " + ((takenBrick < 10) ? ("0" + takenBrick) : takenBrick)
                + "] B";
    }

    @Override
    public String toString() {
        return player + " TRA " + "Given: "+givenResources+", Taken: "+takenResources;
    }

}