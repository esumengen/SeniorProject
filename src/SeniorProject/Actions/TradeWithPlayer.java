package SeniorProject.Actions;

import SeniorProject.*;

import java.io.Serializable;

public class TradeWithPlayer implements IAction, Serializable {
    Player playerGiver;
    Player playerTaker;
    Resource givenResources;
    Resource takenResources;
    Board board;

    public TradeWithPlayer(Resource givenResources, Resource takenResources, int playerGiver, int playerTaker, Board board) {
        this.givenResources = givenResources;
        this.takenResources = takenResources;
        this.playerGiver = new Player(playerGiver); // ?
        this.playerTaker = new Player(playerTaker); // ?
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
        this.playerGiver = board.getPlayers().get(playerGiver.getIndex());
        this.playerTaker = board.getPlayers().get(playerTaker.getIndex());
    }

    @Override
    public void execute() {
        board.tradePlayer(playerGiver.getIndex(), playerTaker.getIndex(), givenResources, takenResources);
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

        return "P" + (playerGiver.getIndex() + 1)
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
                + "] " + (playerTaker.getIndex() + 1);
    }

    @Override
    public String toString() {
        return playerGiver + " TRA(P) " + playerTaker + " Given: " + givenResources + ", Taken: " + takenResources;
    }
}