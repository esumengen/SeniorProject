package SeniorProject;

import java.util.ArrayList;

enum LocationType {
    SETTLEMENT, CITY, EMPTY;
}

class Location {
    private int index;
    private ArrayList<Land> adjacentLands;
    private ArrayList<Road> connectedRoads;
    private ArrayList<Location> connectedLocations;
    private ArrayList<Structure> structures;
    private int ownerIndex;
    private LocationType type;

    public Location(int index) {
        this.adjacentLands = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.connectedLocations = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.index = index;
        this.ownerIndex = -1;
        this.type = LocationType.EMPTY;

    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Land> getAdjacentLands() {
        return adjacentLands;
    }

    public ArrayList<Location> getConnectedLocations() {
        return connectedLocations;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Road> getConnectedRoads() {
        return connectedRoads;
    }

    public void setOwnerIndex(int ownerIndex) {
        this.ownerIndex = ownerIndex;
    }

    public int getOwnerIndex() {
        return ownerIndex;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public LocationType getType() {
        return type;
    }
}
