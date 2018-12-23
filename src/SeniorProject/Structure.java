package SeniorProject;


import java.io.Serializable;

public abstract class Structure implements Serializable {
    private Player player;

    protected Structure(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}