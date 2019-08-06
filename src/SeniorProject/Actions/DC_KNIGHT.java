package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;

import java.io.Serializable;
import java.util.ArrayList;

public class DC_KNIGHT implements IAction, Serializable {
    int landIndex;
    int playerIndex;
    int victimIndex;
    Board board;

    public DC_KNIGHT(int landIndex, int playerIndex, int victimIndex, Board board) {
        this.landIndex = landIndex;
        this.playerIndex = playerIndex;
        this.victimIndex = victimIndex;
        this.board = board;
    }

    @Override
    public void execute() {
        ArrayList<DevelopmentCardType> developmentCardTypes = board.getPlayers().get(playerIndex).getDevelopmentCards();
        for (DevelopmentCardType developmentCardType : developmentCardTypes) {
            if (developmentCardType == DevelopmentCardType.KNIGHT) {
                developmentCardTypes.remove(developmentCardType);
                board.useDevelopmentCard_KNIGHT(landIndex, playerIndex, victimIndex);
            }
        }


    }

    @Override
    public String getCommand() {
        return null;
    }
}
