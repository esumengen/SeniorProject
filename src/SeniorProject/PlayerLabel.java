package SeniorProject;

import javax.swing.*;

public class PlayerLabel extends JLabel implements IObserver {
    ResourceType resourceType;
    Player player;
    boolean stateControl;

    public PlayerLabel (Player player) {
        super("");

        this.stateControl = false;
        this.player = player;

        player.addObserver(this);
    }

    public PlayerLabel (Player player, ResourceType resourceType) {
        this(player);

        this.resourceType = resourceType;
    }

    public void checkState(boolean stateControl) {
        this.stateControl = stateControl;
    }

    @Override
    public void update() {
        if (stateControl)
            setText(player.getState().toString());
        else if (resourceType != null) {
            setText(player.getResource().get(resourceType).toString());
        }
    }
}