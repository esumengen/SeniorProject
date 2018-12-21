import SeniorProject.Building;
import SeniorProject.BuildingType;
import SeniorProject.Location;
import SeniorProject.Player;
import org.junit.Assert;
import org.junit.Test;

public class TestBuilding {
    @Test
    public void testGetLocation(){
        Player player = new Player(1);
        Location location = new Location(2);
        Building building = new Building(location, player, BuildingType.SETTLEMENT) {
            @Override
            public Location getLocation() {
                return super.getLocation();
            }
        };
        Assert.assertEquals(location, building.getLocation());
    }

    @Test
    public void testGetType(){
        Player player = new Player(1);
        Location location = new Location(2);
        Building building = new Building(location, player, BuildingType.SETTLEMENT) {
            public BuildingType getType() {
                return super.getType();
            }
        };
        Assert.assertEquals(BuildingType.SETTLEMENT, building.getType());
    }
}
