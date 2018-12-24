package DevelopmentCards;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Deck implements Serializable {

    HashMap<CardType, Integer> deck;

    public Deck() {
        deck = new HashMap<>();
        deck.put(CardType.KNIGHT, 14);
        deck.put(CardType.ROADBUILDING, 2);
        deck.put(CardType.YEAROFPLENT, 2);
        deck.put(CardType.MONOPOLY, 2);
        deck.put(CardType.VICTORYPOINT, 5);
    }

    public CardType pickCard() {
        Random randomGenerator = new Random(CardType.values().length);
        CardType randomCard = CardType.values()[randomGenerator.nextInt()];
        if (deck.get(randomCard) > 0) {
            deck.replace(randomCard, (deck.get(randomCard) - 1));
            return randomCard;
        }else {
            return pickCard();
        }
    }

}
