package SeniorProject;

class Settlement extends Building {
    static final BuildingType TYPE = BuildingType.SETTLEMENT;

    Settlement(Location location, Player player) {
        super(location, player, TYPE);
    }
}