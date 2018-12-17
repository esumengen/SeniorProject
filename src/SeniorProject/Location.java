package SeniorProject;

import java.util.ArrayList;

public class Location {
    private int index;
    private ArrayList<Land> adjacentLands;
    private ArrayList<Road> connectedRoads;
    private ArrayList<Location> connectedLocations;
    private ArrayList<Structure> structures;
    private Player owner;

    public Location(int index) {
        this.adjacentLands = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.connectedLocations = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.index = index;

    }

    int getIndex() {
        return index;
    }

    ArrayList<Land> getAdjacentLands() {
        return adjacentLands;
    }

    ArrayList<Location> getConnectedLocations() {
        return connectedLocations;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    ArrayList<Road> getConnectedRoads() {
        return connectedRoads;
    }

    void setOwner(Player owner) {
        this.owner = owner;
    }

    Player getOwner() {
        return owner;
    }

    boolean hasOwner () {
        return owner != null;
    }

    boolean hasCity () {
        for (Structure structure:structures) {
            if (structure instanceof City)
                return true;
        }

        return false;
    }
}
