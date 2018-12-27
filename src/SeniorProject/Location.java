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
    private boolean isCorner = false;

    public Location(int index) {
        this.adjacentLands = new ArrayList<>();
        this.adjacentLocations = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Land> getAdjacentLands() {
        return adjacentLands;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void addStructures(Structure structure) {
        structures.add(structure);
    }

    ArrayList<Road> getConnectedRoads() {
        return connectedRoads;
    }

    public void addConnectedRoad(Road road) {
        connectedRoads.add(road);
    }

    Player getOwner() {
        return owner;
    }

    void setOwner(Player owner) {
        this.owner = owner;
    }

    boolean hasOwner() {
        return owner != null;
    }

    boolean hasCity() {
        for (Structure structure : structures) {
            if (structure instanceof City)
                return true;
        }

        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void makeCorner() {
        isCorner = true;
    }

    public boolean isCorner() {
        return isCorner;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjacentLocations;
    }

    public void addAdjacentLocations(Location location) {
        adjacentLocations.add(location);
    }
}
