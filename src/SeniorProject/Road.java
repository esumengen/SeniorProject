package SeniorProject;

import SeniorProject.Location;
import SeniorProject.Player;

public class Road extends Structure {
    private Location[] locations;

    public Road(Location startLocation, Location endLocation, Player player){
        super(player);

        this.locations = new Location[2];
        this.locations[0] = startLocation;
        this.locations[1] = endLocation;
    }

    public Location getStartLocation(){
        return locations[0];
     }

    public Location getEndLocation(){
        return locations[1];
    }
}