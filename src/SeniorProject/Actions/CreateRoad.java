package SeniorProject.Actions;

import SeniorProject.Board;
import SeniorProject.IAction;
import SeniorProject.Location;
import SeniorProject.Player;

public class CreateRoad implements IAction {
    private Location[] locations;
    Player player;

    public CreateRoad(Location[] locations, Player player) {
        this.locations = locations;
        this.player = player;
    }

    @Override
    public void execute () {
        ((Board) locations[0].getPureBoard()).createRoad(player, locations[0], locations[1]);
    }

    @Override
    public String toString() {
        return player + " RO " + locations[0] + "-" + locations[1];
    }
}