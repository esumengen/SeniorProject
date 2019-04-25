package SeniorProject;

import java.util.ArrayList;

public class Node {
    private int index;
    private ArrayList<ArrayList<Node>> adjecentNodes;

    public Node (int index) {
        this.index = index;
        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            adjecentNodes.add(new ArrayList<>());
        }
    }

    public ArrayList<Node> getAdjecentNodes_player(int playerIndex) {
        return adjecentNodes.get(playerIndex);
    }

    public int getIndex() {
        return index;
    }
}
