package SeniorProject;

import javax.swing.*;
import java.awt.*;


class DebugFrame extends JFrame {
    private JPanel aiPanel;
    private JPanel resourcesPanel;
    private Board board;

    DebugFrame(Board board) {
        this.board = board;

        setLayout(new GridLayout(3, 1));

        //region States Panel
        aiPanel = new JPanel();
        aiPanel.setLayout(new GridLayout(board.getPlayers().size() + 1, 2));

        aiPanel.add(new Label("Name"));
        aiPanel.add(new Label("State"));

        for (Player player : board.getPlayers()) {
            JLabel nameLabel = new JLabel(player.getName());

            PlayerLabel stateLabel = new PlayerLabel(player);

            if (player.getType() != PlayerType.HUMAN)
                stateLabel.checkState(true);
            else
                stateLabel.setText("-");

            stateLabel.update();

            aiPanel.add(nameLabel);
            aiPanel.add(stateLabel);
        }
        //endregion

        //region Resources Panel
        resourcesPanel = new JPanel();
        resourcesPanel.setLayout(new GridLayout(board.getPlayers().size() + 1, board.getPlayers().get(0).getResource().size()));

        for (ResourceType resource : ResourceType.values()) {
            JLabel resourceNameLabel = new JLabel(resource.toString());
            resourcesPanel.add(resourceNameLabel);
        }

        for (Player player : board.getPlayers()) {
            for (ResourceType resource : ResourceType.values()) {
                PlayerLabel resourcePanel = new PlayerLabel(player, resource);
                resourcePanel.update();
                resourcesPanel.add(resourcePanel);
            }
        }
        //endregion

        add(aiPanel);
        add(resourcesPanel);

        setSize(800, 600);
    }
}