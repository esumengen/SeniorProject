package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;
import SeniorProject.ResourceType;

import java.io.Serializable;
import java.util.ArrayList;

public class DC_MONOPOLY implements IAction, Serializable {
    int playerIndex;
    ResourceType resourceType;
    Board board;

    public DC_MONOPOLY(int playerIndex, ResourceType resourceType, Board board) {
        this.playerIndex = playerIndex;
        this.resourceType = resourceType;
        this.board = board;
    }

    @Override
    public void execute() {
        ArrayList<DevelopmentCardType> developmentCards = board.getPlayers().get(playerIndex).getDevelopmentCards();
        for (DevelopmentCardType developmentCard : developmentCards) {
            if (developmentCard == DevelopmentCardType.MONOPOLY) {
                board.useDevelopmentCard_MONOPOLY(playerIndex, resourceType);
                developmentCards.remove(developmentCard);
            }
        }
    }

    @Override
    public String getCommand() {
        return null;
    }
}