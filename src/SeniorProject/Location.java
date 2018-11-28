package SeniorProject;

import java.util.ArrayList;

class Location {
    int index;
    private ArrayList<Land> adjacentLands;
    private ArrayList<Road> connectedRoads;
    private ArrayList<Location> connectedLocations;
    private ArrayList<Structure> structures;

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
}
