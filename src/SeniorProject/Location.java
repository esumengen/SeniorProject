package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable {
    private int index;
    private ArrayList<Land> adjacentLands;
    private ArrayList<Location> adjacentLocations;
    private ArrayList<Road> connectedRoads;
    private ArrayList<Structure> structures;
    private Player owner;
    private boolean isActive = false;

    public Location(int index) {
        this.adjacentLands = new ArrayList<>();
        this.adjacentLocations = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.index = index;
    }

    int getIndex() {
        return index;
    }

    public ArrayList<Land> getAdjacentLands() {
        return adjacentLands;
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjacentLocations;
    }

    public void addAdjacentLocations(Location location) {
        adjacentLocations.add(location);
    }
}
