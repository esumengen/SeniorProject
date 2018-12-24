package SeniorProject;

import java.io.Serializable;

public class City extends Building implements Serializable {

    private static final StructureType TYPE = StructureType.CITY;

    public City(Location location, Player player) {
        super(location, player, TYPE);
        setType(StructureType.CITY);
    }
}