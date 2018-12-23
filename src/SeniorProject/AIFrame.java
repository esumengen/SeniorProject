package SeniorProject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class AIFrame extends JFrame {
    static ArrayList<Player> players;
    static JPanel aiPanel;
    // JPanel canAffordPanel;
    static JPanel resourcesPanel;

    public AIFrame (Board board) {
        setSize(800, 800);
        this.players = board.getPlayers();
        aiPanel = setAIPanel();
       // canAffordPanel = setCanAffordPanel();
        resourcesPanel = setResourcesPanel();

        setLayout(new GridLayout(3, 1));
        add(aiPanel);
        add(resourcesPanel);
       // add(canAffordPanel);

        setVisible(true);

    }

    private static JPanel setAIPanel () {
        aiPanel = new JPanel();
        aiPanel.setLayout(new GridLayout(players.size() + 1, 2));
        aiPanel.add(new Label("Name"));
        aiPanel.add(new Label("State"));

        for (Player player : players) {
            aiPanel.add(new Label("Player " + Integer.toString(player.getIndex())));
            aiPanel.add(new Label(player.getState().toString()));
        }

        return aiPanel;
    }

    public static JPanel setResourcesPanel() {
        resourcesPanel = new JPanel();
        resourcesPanel.setLayout(new GridLayout(players.size() + 1, players.get(0).getResources().size()));

        for (ResourceType resource : ResourceType.values())
            resourcesPanel.add(new Label(resource.toString()));
        for (Player player : players) {
            for (ResourceType resource : ResourceType.values())
                resourcesPanel.add(new Label(player.getResources().get(resource).toString()));
        }

        return resourcesPanel;
    }
/*
    private JPanel setCanAffordPanel () {
        canAffordPanel = new JPanel();
        canAffordPanel.setLayout(new GridLayout(players.size() + 1, AI.MoveType.values().length));

        for (MoveType type : AI.MoveType.values())
            resourcesPanel.add(new Label(type.toString()));
        for (Player player : players) {
            for (AI.MoveType type : AI.MoveType.values())
                resourcesPanel.add(new Label(player.getAi().getCanAfford().get(type.ordinal()).toString()));
        }

        return canAffordPanel;
    }*/

    public static void update(){
        setAIPanel();
        setResourcesPanel();
    }
}
