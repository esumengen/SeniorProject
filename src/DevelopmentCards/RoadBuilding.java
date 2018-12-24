package DevelopmentCards;

import SeniorProject.Player;

public class RoadBuilding extends DevelopmentCard {

    public RoadBuilding(Player player) {
        super(player);
        setCardType(CardType.ROADBUILDING);
    }

    @Override
    void use() {
        player.getAI().useRoadBuild();
        player.getDevelopmentCards().remove(this);
    }
}