package SeniorProject;

import java.io.Serializable;

public class City extends Building implements Serializable {

    private static final BuildingType TYPE = BuildingType.CITY;

    public City(Location location, Player player){
        super(location, player, TYPE);
    }
}