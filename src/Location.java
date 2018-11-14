import java.util.ArrayList;

public class Location {
    int index;
    ArrayList<Land> adjacentLands;
    ArrayList<Road> connectedRoads;
    ArrayList<Location> connectedLocations;
    ArrayList<Structure> structures;

    public Location(int index) {
        this.adjacentLands = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.connectedLocations = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Land> getAdjentLands() {
        return adjacentLands;
    }
}
