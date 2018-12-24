package DevelopmentCards;

import SeniorProject.Player;

public class YearOfPlenty extends DevelopmentCard {

    public YearOfPlenty(Player player) {
        super(player);
        setCardType(CardType.YEAROFPLENT);
    }

    @Override
    void use() {
        player.getAI().useYearOfPlenty();
        player.getDevelopmentCards().remove(this);
    }
}
