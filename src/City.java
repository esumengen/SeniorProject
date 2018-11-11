public class City extends Building {
    public static final BuildingType TYPE = BuildingType.City;

    public City(Location location, Player player){
        super(location, player, TYPE);
    }
}