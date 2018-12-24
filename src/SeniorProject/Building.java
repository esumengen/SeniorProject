package SeniorProject;

import java.io.Serializable;

public abstract class Building extends Structure implements Serializable {
    private Location location;
    private StructureType type;

    public Building(Location location, Player player, StructureType type) {
        super(player);
        this.location = location;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public StructureType getType() {
        return type;
    }
}