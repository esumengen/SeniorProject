package SeniorProject;

import java.io.Serializable;

public class Settlement extends Building implements Serializable {
    static final BuildingType TYPE = BuildingType.SETTLEMENT;

    public Settlement(Location location, Player player) {
        super(location, player, TYPE);
    }
}