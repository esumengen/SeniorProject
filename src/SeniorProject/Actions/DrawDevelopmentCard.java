package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.Deck;
import SeniorProject.DevelopmentCards.DevelopmentCardType;
import SeniorProject.IAction;
import SeniorProject.Resource;

import java.io.Serializable;

public class DrawDevelopmentCard implements IAction, Serializable {
    public static final Resource COST = new Resource(1, 0, 1, 1, 0);

    int playerIndex;
    Board board;
    DevelopmentCardType cardType;

    public DrawDevelopmentCard(int playerIndex, Deck deck, Board board) {
        this.playerIndex = playerIndex;
        this.board = board;

        if (deck != null)
            this.cardType = deck.learnDevelopmentCard();
        else
            this.cardType = null;
    }

    @Override
    public void execute() {
        board.drawDevelopmentCard(playerIndex, cardType);
    }

    @Override
    public String getCommand() {
        return board.getPlayers().get(playerIndex) + " [DR 99] " + cardType;
    }

    @Override
    public String toString() {
        return board.getPlayers().get(playerIndex) + " DR " + cardType;
    }

    public void setCardType(DevelopmentCardType cardType) {
        this.cardType = cardType;
    }
}