package DevelopmentCards;

import SeniorProject.Player;

enum CardType {
    KNIGHT, ROADBUILDING, YEAROFPLENT, MONOPOLY, VICTORYPOINT
}

public abstract class DevelopmentCard {
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

    public void delete() {
        player.getDevelopmentCards().remove(this);
    }
}
