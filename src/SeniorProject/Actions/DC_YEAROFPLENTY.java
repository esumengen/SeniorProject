package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;
import SeniorProject.Player;
import SeniorProject.ResourceType;

import java.io.Serializable;
import java.util.ArrayList;

public class DC_YEAROFPLENTY implements IAction, Serializable {
    int playerIndex;
    ResourceType resourceType1;
    ResourceType resourceType2;
    Board board;

    public DC_YEAROFPLENTY(int playerIndex, ResourceType resourceType1, ResourceType resourceType2, Board board) {
        this.playerIndex = playerIndex;
        this.resourceType1 = resourceType1;
        this.resourceType2 = resourceType2;
        this.board = board;
    }

    @Override
    public void execute() {
        ArrayList<DevelopmentCardType> developmentCards = board.getPlayers().get(playerIndex).getDevelopmentCards();
        for (DevelopmentCardType developmentCard : developmentCards) {
            if (developmentCard == DevelopmentCardType.YEAROFPLENTY) {
                developmentCards.remove(developmentCard);
                board.useDevelopmentCard_YEAROFPLENTY(playerIndex, resourceType1, resourceType2);
            }
        }
    }

    @Override
    public String getCommand() {
        return null;
    }
}
