package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;

import java.io.Serializable;
import java.util.ArrayList;

public class DC_VICTORYPOINT implements IAction, Serializable {
    int playerIndex;
    Board board;

    public DC_VICTORYPOINT(int playerIndex, Board board) {
        this.playerIndex = playerIndex;
        this.board = board;
    }
    @Override
    public void execute() {
        ArrayList<DevelopmentCardType> developmentCards = board.getPlayers().get(playerIndex).getDevelopmentCards();
        for (DevelopmentCardType developmentCard : developmentCards) {
            if (developmentCard == DevelopmentCardType.VICTORYPOINT) {
                developmentCards.remove(developmentCard);
                board.useDevelopmentCard_VICTORYPOINT(playerIndex);
            }
        }
    }

    @Override
    public String getCommand() {
        return null;
    }
}
