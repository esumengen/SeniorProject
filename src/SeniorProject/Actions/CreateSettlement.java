package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

public class CreateSettlement implements IAction {
    Location location;
    Player player;

    public CreateSettlement (Location location, Player player) {
        this.location = location;
        this.player = player;
    }

    @Override
    public void execute () {
        ((Board) location.getPureBoard()).createSettlement(player, location);
    }

    @Override
    public String toString() {
        return player + " SE " + location;
    }
}