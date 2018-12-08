package SeniorProject;

import java.util.ArrayList;

public class Player {
    private String name;
    private int index;
    private int AIType;
    private ArrayList<Structure> structures = new ArrayList<>();
    private int wheat, wood, stone, wool, brick = 0;

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

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getWool() {
        return wool;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }
}


