import SeniorProject.Building;
import SeniorProject.StructureType;
import SeniorProject.Location;
import SeniorProject.Player;
import org.junit.Assert;
import org.junit.Test;

public class TestBuilding {
    @Test
    public void testGetLocation(){
        Player player = new Player(1);
        Location location = new Location(2);
        Building building = new Building(location, player, StructureType.SETTLEMENT) {
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
        Building building = new Building(location, player, StructureType.SETTLEMENT) {
            public StructureType getType() {
                return super.getType();
            }
        };
        Assert.assertEquals(StructureType.SETTLEMENT, building.getType());
    }
}
