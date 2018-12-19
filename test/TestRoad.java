import SeniorProject.Location;
import SeniorProject.Player;
import SeniorProject.Road;
import org.junit.Assert;
import org.junit.Test;

public class TestRoad {

    @Test
    public void testGetStartLocation(){
        Location start = new Location(0);
        Location end = new Location(1);
        Player player = new Player(0);
        Road road = new Road(start, end, player);
        Assert.assertEquals(start, road.getStartLocation());
    }

    @Test
    public void testGetEndLocation(){
        Location start = new Location(0);
        Location end = new Location(1);
        Player player = new Player(0);
        Road road = new Road(start, end, player);
        Assert.assertEquals(end, road.getEndLocation());
    }
}
