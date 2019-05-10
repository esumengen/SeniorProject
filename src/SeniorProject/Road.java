package SeniorProject;

import java.io.Serializable;

public class Road extends Structure implements Serializable {
    public static final Resource COST = new Resource(0, 1, 0, 0, 1);

    private Location[] locations;

    public Road(Location startLocation, Location endLocation, Player player) {
        super(player);

        this.locations = new Location[2];
        this.locations[0] = startLocation;
        this.locations[1] = endLocation;

        setType(StructureType.ROAD);
    }

    public Location getStartLocation() {
        return locations[0];
    }

    public Location getEndLocation() {
        return locations[1];
    }


}
