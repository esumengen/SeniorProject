package SeniorProject.Actions;

import SeniorProject.*;

public class CreateSettlement implements IAction {
    Location location;
    Player player;
    Board board;

    public CreateSettlement (Location location, Player player) {
        this.location = location;
        this.player = player;
    }

    @Override
    public void execute () {
        board.createSettlement(player, location);
    }
}