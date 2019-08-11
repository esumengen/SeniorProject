package SeniorProject;

import java.io.Serializable;

public class City extends Building implements Serializable {
    public static final StructureType TYPE = StructureType.CITY;
    public static final Resource COST = new Resource(3, 0, 0, 2, 0);

    City(Location location, Player player) {
        super(location, player, TYPE);
        setType(StructureType.CITY);
    }
}