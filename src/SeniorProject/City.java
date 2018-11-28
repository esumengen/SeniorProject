package SeniorProject;

class City extends Building {

    private static final BuildingType TYPE = BuildingType.CITY;

    public City(Location location, Player player){
        super(location, player, TYPE);
    }
}