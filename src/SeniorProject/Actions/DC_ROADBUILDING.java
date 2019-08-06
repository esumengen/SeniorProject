package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;
import SeniorProject.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class DC_ROADBUILDING implements IAction, Serializable {
    int playerIndex;
    Location loc1, loc2, loc3, loc4;
    Board board;

    public DC_ROADBUILDING(int playerIndex, Location loc1, Location loc2, Location loc3, Location loc4, Board board) {
        this.playerIndex = playerIndex;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.loc3 = loc3;
        this.loc4 = loc4;
        this.board = board;
    }

    @Override
    public void execute() {
        ArrayList<DevelopmentCardType> developmentCards = board.getPlayers().get(playerIndex).getDevelopmentCards();
        for (DevelopmentCardType developmentCard : developmentCards) {
            if (developmentCard == DevelopmentCardType.ROADBUILDING) {
                developmentCards.remove(developmentCard);
                board.useDevelopmentCard_ROADBUILDING(playerIndex, loc1, loc2, loc3, loc4);
            }
        }
    }

    @Override
    public String getCommand() {
        return null;
    }
}
