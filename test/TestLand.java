import SeniorProject.Land;
import SeniorProject.Location;
import org.junit.Test;

import java.util.ArrayList;

public class TestLand {

    @Test
    public void testGetAdjacentLocations(){
        Location location = new Location(0);
        Location location2 = new Location(1);
        Location location3 = new Location(2);
        Location location4 = new Location(7);
        Location location5 = new Location(8);
        Location location6 = new Location(9);

        ArrayList<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location2);
        locations.add(location3);
        locations.add(location4);
        locations.add(location5);
        locations.add(location6);

        Land land = new Land(0);  // devam et

    }
}
