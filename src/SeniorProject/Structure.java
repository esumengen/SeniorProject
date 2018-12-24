package SeniorProject;

import java.io.Serializable;

public abstract class Structure implements Serializable {
    private Player player;
    private StructureType type;

    protected Structure(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public StructureType getType() {
        return type;
    }

    public void setType(StructureType type) {
        this.type = type;
    }
}