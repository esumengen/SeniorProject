package SeniorProject.Actions;

import SeniorProject.*;

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
}