package SeniorProject;


public abstract class Structure {
    private Player player;

    public Structure(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}