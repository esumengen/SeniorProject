package SeniorProject;

import java.io.Serializable;

public class Settlement extends Building implements Serializable {
    public static final StructureType TYPE = StructureType.SETTLEMENT;
    public static final Resource COST = new Resource(1, 1, 1, 0, 1);

    public Settlement(Location location, Player player) {
        super(location, player, TYPE);
        setType(StructureType.SETTLEMENT);
    }
}