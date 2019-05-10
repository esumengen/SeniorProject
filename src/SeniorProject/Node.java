package SeniorProject;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
    private int index;
    private ArrayList<ArrayList<Node>> adjacentNodes;
    private ArrayList<Node> adjacentNodes_manual;

    public Node(int index) {
        this.index = index;
        adjacentNodes = new ArrayList<>();
        adjacentNodes_manual = new ArrayList<>();

        for (int i = 0; i < Global.PLAYER_COUNT; i++)
            adjacentNodes.add(new ArrayList<>());
    }

    public ArrayList<Node> getAdjacentNodes_player(int playerIndex) {
        return adjacentNodes.get(playerIndex);
    }

    public void addAdjacentNodes_player(int playerIndex, Node node) {
        adjacentNodes.get(playerIndex).add(node);
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Node> getAdjacentNodes_manual() {
        return adjacentNodes_manual;
    }

    public void setAdjacentNodes_manual(ArrayList<Node> adjacentNodes_manual) {
        this.adjacentNodes_manual = adjacentNodes_manual;
    }
}
