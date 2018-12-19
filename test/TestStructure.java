import SeniorProject.Player;
import SeniorProject.Structure;
import org.junit.Assert;
import org.junit.Test;

public class TestStructure {

    @Test
    public void testGetPlayer(){
        Player player = new Player(0);
        Structure structure = new Structure(player) {
            @Override
            public Player getPlayer() {
                return super.getPlayer();
            }
        };
        Assert.assertEquals(player, structure.getPlayer());
    }
}
