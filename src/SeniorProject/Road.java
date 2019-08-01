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

    @Override
    public String toString() {
        return "Road(" + getStartLocation() + ", " + getEndLocation() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Road) {
            return ((Road) obj).getEndLocation().getIndex() == getEndLocation().getIndex()
                    && ((Road) obj).getStartLocation().getIndex() == getStartLocation().getIndex() ||
                    ((Road) obj).getStartLocation().getIndex() == getEndLocation().getIndex()
                            && ((Road) obj).getEndLocation().getIndex() == getStartLocation().getIndex();
        }

        return false;
    }
}
