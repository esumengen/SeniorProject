package DevelopmentCards;

import SeniorProject.Player;

public class Knight extends DevelopmentCard {

    public Knight(Player player) {
        super(player);
        setCardType(CardType.KNIGHT);
    }
    @Override
    void use() {
        player.setKnight(player.getKnight() + 1);
        player.getAI_instance().useKnight();
        destroy();
    }
}