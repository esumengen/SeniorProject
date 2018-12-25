package DevelopmentCards;

import SeniorProject.Player;

import java.io.Serializable;

enum CardType {
    KNIGHT, ROADBUILDING, YEAROFPLENT, MONOPOLY, VICTORYPOINT
}

public abstract class DevelopmentCard implements Serializable {
    private CardType cardType;
    protected Player player;

    public DevelopmentCard (Player player) {
        this.player = player;
    }
    abstract void use();

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void destroy() {
        player.getDevelopmentCards().remove(this);
    }
}