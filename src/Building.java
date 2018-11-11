public abstract class Building extends Structure {
    private Location location;
    private BuildingType type;

    public Building(Location location, Player player, BuildingType type){
        super(player);
        this.location = location;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }
}
