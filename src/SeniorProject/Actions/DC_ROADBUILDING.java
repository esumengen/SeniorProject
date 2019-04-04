package SeniorProject.Actions;

        import SeniorProject.Board;
        import SeniorProject.IAction;
        import SeniorProject.Location;
        import SeniorProject.Player;

public class DC_ROADBUILDING implements IAction {
    Player player;
    Location loc1,loc2, loc3, loc4;

    public DC_ROADBUILDING(Player player, Location loc1, Location loc2, Location loc3, Location loc4) {
        this.player = player;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.loc3 = loc3;
        this.loc4 = loc4;
    }

    @Override
    public void execute() {
        ((Board) player.getPureBoard()).useDevelopmentCard_ROADBUILDING(player, loc1, loc2, loc3, loc4);
    }
}
