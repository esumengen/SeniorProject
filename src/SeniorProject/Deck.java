package SeniorProject;

import SeniorProject.DevelopmentCards.DevelopmentCardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {
    private ArrayList<DevelopmentCardType> deck;

    public Deck() {
        deck = new ArrayList<>();

        for (int i = 0; i < 14 * 5; i++)
            deck.add(DevelopmentCardType.KNIGHT);

        for (int i = 0; i < 2 * 5; i++)
            deck.add(DevelopmentCardType.ROADBUILDING);

        for (int i = 0; i < 2 * 5; i++)
            deck.add(DevelopmentCardType.YEAROFPLENTY);

        for (int i = 0; i < 2 * 5; i++)
            deck.add(DevelopmentCardType.MONOPOLY);

        for (int i = 0; i < 5 * 5; i++)
            deck.add(DevelopmentCardType.VICTORYPOINT);

        Collections.shuffle(deck);
    }

    /*public DevelopmentCardType pickDevelopmentCard() {
        if (deck.size() > 0) {
            DevelopmentCardType cardType = deck.get(0);
            deck.remove(cardType);
            return cardType;
        }

        new Message("The deck is empty. (Err: 21)");
        return null;
    }*/

    // Bug!
    public DevelopmentCardType learnDevelopmentCard() {
        return deck.get(0);
    }

    DevelopmentCardType pickDevelopmentCard(DevelopmentCardType cardType) {
        if (deck.contains(cardType))
            deck.remove(cardType);
        else {
            new Message("The deck has no " + cardType + ". (Err: 31)");
            return null;
        }

        return cardType;
    }

    public int getSize() {
        return deck.size();
    }
}