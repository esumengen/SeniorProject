package SeniorProject;

public class Settlement extends Building {
    public static final BuildingType TYPE = BuildingType.SETTLEMENT;

    public Settlement(Location location, Player player) {
        super(location, player, TYPE);
    }
}