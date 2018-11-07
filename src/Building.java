public class Building extends Structure {
    private Location location;

    public Building(Location location, int ownerIndex){
        super(ownerIndex);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
