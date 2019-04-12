package SeniorProject.DevelopmentCards;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Deck implements Serializable {

    private HashMap<DevelopmentCardType, Integer> deck;

    public Deck() {
        deck = new HashMap<>();
        deck.put(DevelopmentCardType.KNIGHT, 14);
        deck.put(DevelopmentCardType.ROADBUILDING, 2);
        deck.put(DevelopmentCardType.YEAROFPLENTY, 2);
        deck.put(DevelopmentCardType.MONOPOLY, 2);
        deck.put(DevelopmentCardType.VICTORYPOINT, 5);
    }

    public DevelopmentCardType pickDevelopmentCard() {
        Random randomGenerator = new Random(DevelopmentCardType.values().length);
        DevelopmentCardType randomCard = DevelopmentCardType.values()[randomGenerator.nextInt(DevelopmentCardType.values().length - 1)];

        if (deck.get(randomCard) > 0) {
            deck.replace(randomCard, (deck.get(randomCard) - 1));
            return randomCard;
        } else {
            return pickDevelopmentCard();
        }
    }

    public DevelopmentCardType pickDevelopmentCard(Integer cardTypeIndex) {
        DevelopmentCardType cardType = DevelopmentCardType.values()[cardTypeIndex];

        if (deck.get(cardType) > 0) {
            deck.replace(cardType, (deck.get(cardType) - 1));
            return cardType;
        } else {
            return null;
        }
    }
}
