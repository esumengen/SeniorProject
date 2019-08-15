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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node)
            return ((Node) obj).getIndex() == index;

        return false;
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

    @Override
    public String toString() {
        return "Index: " + index;
    }
}
