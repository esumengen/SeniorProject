package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

public class UpgradeSettlement implements IAction {
    Location location;
    Player player;

    public UpgradeSettlement(Location location, Player player) {
        this.location = location;
        this.player = player;
    }

    @Override
    public void execute() {
        ((Board) location.getPureBoard()).upgradeSettlement(player, location);
    }
}