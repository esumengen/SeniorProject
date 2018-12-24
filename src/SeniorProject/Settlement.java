package SeniorProject;

import java.io.Serializable;

public class Settlement extends Building implements Serializable {
    static final StructureType TYPE = StructureType.SETTLEMENT;

    public Settlement(Location location, Player player) {
        super(location, player, TYPE);
        setType(StructureType.SETTLEMENT);
    }
}