package SeniorProject;


abstract class Structure {
    private Player player;

    Structure(Player player){
        this.player = player;
    }

    Player getPlayer() {
        return player;
    }
}