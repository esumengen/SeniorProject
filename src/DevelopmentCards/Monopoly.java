package DevelopmentCards;

import SeniorProject.Player;

public class Monopoly extends DevelopmentCard {

    public Monopoly(Player player) {
        super(player);
        setCardType(CardType.MONOPOLY);
    }

    @Override
    void use() {
        player.getAI_instance().useMonopoly_move();
        destroy();
    }
}
