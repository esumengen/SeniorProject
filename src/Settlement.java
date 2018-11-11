public class Settlement extends Building {
    public static final BuildingType TYPE = BuildingType.Settlement;

    public Settlement(Location location, Player player) {
        super(location, player, TYPE);
    }
}