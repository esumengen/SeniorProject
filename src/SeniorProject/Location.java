package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;

public class Location extends Node implements Serializable {

    private HarborType harborType;
    private ArrayList<Land> adjacentLands;
    private ArrayList<Location> adjacentLocations;
    private ArrayList<Road> connectedRoads;
    private ArrayList<Structure> structures;
    private Player owner;
    private boolean isActive = false;
    private boolean isCorner = false;
    private PureBoard pureBoard;

    public Location(int index) {
        super(index);
        this.adjacentLands = new ArrayList<>();
        this.adjacentLocations = new ArrayList<>();
        this.connectedRoads = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.harborType = null;
    }


    public ArrayList<Land> getAdjacentLands() {
        return adjacentLands;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public Building getBuilding() {
        for (Structure structure : structures) {
            if (structure instanceof Building)
                return (Building) structure;
        }

        return null;
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

    public Player getOwner() {
        return owner;
    }

    void setOwner(Player owner) {
        this.owner = owner;
    }

    boolean hasOwner() {
        return owner != null;
    }

    void setHarborType(HarborType harborType) {
        this.harborType = harborType;
    }

    public boolean hasCity() {
        for (Structure structure : structures) {
            if (structure instanceof City)
                return true;
        }

        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    void setActive(boolean active) {
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

    public void setPureBoard(PureBoard board) {
        this.pureBoard = board;
    }

    @Override
    public String toString() {
        return "Loc" + getIndex();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            return ((Location) obj).getIndex() == getIndex();
        }

        return false;
    }
}
