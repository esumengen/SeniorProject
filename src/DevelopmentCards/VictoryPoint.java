package DevelopmentCards;

import SeniorProject.Player;

public class VictoryPoint extends DevelopmentCard{

    public VictoryPoint(Player player) {
        super(player);
        setCardType(CardType.VICTORYPOINT);
        use();
    }

    @Override
    void use() {
        player.setVictoryPoint(player.getVictoryPoint() + 1);
    }
}
