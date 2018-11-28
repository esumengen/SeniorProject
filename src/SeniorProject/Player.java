package SeniorProject;

import SeniorProject.City;

import java.util.ArrayList;

public class Player {
    private String name;
    private int index;
    private int AIType;
    private ArrayList<Settlement> structures = new ArrayList<>();

    public Player (int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getAIType() {
        return AIType;
    }

    public ArrayList<Settlement> getStructures() {
        return structures;
    }
}
