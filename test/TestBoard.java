import SeniorProject.*;
import SeniorProject.Player;
import SeniorProject.Road;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;


public class TestBoard {
        /*@Test
        public void copyTest() {
            ArrayList<Player> players = Main.createPlayers();

            Board board = new Board(players);
            Board boardCopy = Global.deepCopy(board);

            board.toString();
            boardCopy.toString();

            Assert.assertEquals(board.toString(), boardCopy.toString());
        }*/

        @Test
        public void testRoad(){
            ArrayList<Structure> structures = new ArrayList<>();
            int index = 0;
            Player player = new Player(index);
            Location location_first = new Location(0);
            Location location_second = new Location(1);
            Road road = new Road(location_first, location_second, player);
            structures.add(road);
            Assert.assertEquals(1, structures.size());
        }

        @Test
        public void testRoad2(){
            ArrayList<Structure> structures = new ArrayList<>();
            int index = 0;
            Player player = new Player(index);
            Location location_first = new Location(0);
            Location location_second = new Location(1);
            Road road = new Road(location_first, location_second, player);
            structures.add(road);
            Assert.assertEquals(road, structures.get(structures.size() - 1));
        }

        @Test
        public void testRoad3(){

        }

        @Test
        public void testSettlement(){
            ArrayList<Structure> structures = new ArrayList<>();
            Player player = new Player(0);
            Location location = new Location(1);
            Settlement settlement = new Settlement(location, player);
            structures.add(settlement);
            Assert.assertEquals(1, structures.size());
        }

        @Test
        public void testSettlement2(){
            ArrayList<Structure> structures = new ArrayList<>();
            Player player = new Player(0);
            Location location = new Location(1);
            Settlement settlement = new Settlement(location, player);
            structures.add(settlement);
            Assert.assertEquals(settlement, structures.get(structures.size() - 1));
        }

        @Test
        public void testUpgradeSettlement(){
            ArrayList<Structure> structures = new ArrayList<>();
            Player player = new Player(0);
            Location location = new Location(1);
            for(Structure structure : location.getStructures()){
                if (structure instanceof Settlement) {
                    City city = new City(location, structure.getPlayer());
                    structures.set(structures.indexOf(structure), city);
                    Assert.assertEquals(city, structures.get(structures.size() - 1));
                }
            }

        }

        @Test
        public void testUpgradeSettlement2(){
            ArrayList<Structure> structures = new ArrayList<>();
            Player player = new Player(0);
            Location location = new Location(1);
            for(Structure structure : location.getStructures()){
                if (structure instanceof Settlement) {
                    City city = new City(location, structure.getPlayer());
                    structures.set(structures.indexOf(structure), city);
                    Building building = (Building)structures.get(structures.size() - 1);
                    Assert.assertEquals(city.getLocation(), building.getLocation());
                }
            }
        }

        @Test
        public void testUpgradeSettlement3(){

        }

        @Test
        public void testIsValidForRoad(){
            Player player = new Player(1);
            Location start = new Location(1);
            Location end = new Location(2);
            Structure structure = new Road(start,end,player);
        }

        @Test
        public void testMoveRobber(){

        }
}
