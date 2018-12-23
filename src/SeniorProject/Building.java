package SeniorProject;

import java.io.Serializable;

public abstract class Building extends Structure implements Serializable {
    private Location location;
    private BuildingType type;

    public Building(Location location, Player player, BuildingType type) {
        super(player);
        this.location = location;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public BuildingType getType() {
        return type;
    }
}