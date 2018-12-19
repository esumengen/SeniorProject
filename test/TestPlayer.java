import SeniorProject.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TestPlayer {

    @Test
    public void testGetIndex(){
        int index = 1;
        Player player = new Player(index);
        Assert.assertEquals(index, player.getIndex());
    }

    @Test
    public void testWriteMove(){

    }

    @Test
    public void testGetName(){
        Player player = new Player(0);
        String name = "Player 1";
        player.setName(name);
        Assert.assertEquals(name, player.getName());
    }

    @Test
    public void testSetName(){
        Player player = new Player(0);
        String name = "Player 1";
        player.setName(name);
        Assert.assertEquals(player.getName(), name);
    }

    @Test
    public void testGetPlayerType(){

    }

    @Test
    public void testGetStructures(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        Location location = new Location(1);
        Settlement settlement = new Settlement(location, board.getPlayers().get(0));
        board.createSettlement(board.getPlayers().get(0),location);
        int index = board.getPlayers().get(0).getStructures().size() - 1;
        Assert.assertEquals(board.getStructures().get(board.getStructures().size() - 1), board.getPlayers().get(0).getStructures().get(index));
    }

    @Test
    public void testGetGrain(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        players.get(players.size() - 1).setGrain(2);
        Assert.assertEquals(players.get(players.size() - 1).getGrain(), board.getPlayers().get(board.getPlayers().size() - 1).getGrain());
    }

    @Test
    public void testGetLumber(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        players.get(players.size() - 1).setLumber(2);
        Assert.assertEquals(players.get(players.size() - 1).getLumber(), board.getPlayers().get(players.size() - 1).getLumber());
    }

    @Test
    public void testGetOre(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        players.get(players.size() - 1).setOre(3);
        Assert.assertEquals(players.get(players.size() - 1).getOre(), board.getPlayers().get(players.size() - 1).getOre());
    }

    @Test
    public void testGetWool(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        players.get(players.size() - 1).setWool(2);
        Assert.assertEquals(players.get(players.size() - 1).getWool(), board.getPlayers().get(players.size() - 1).getWool());
    }

    @Test
    public void testGetBrick(){
        ArrayList<Player> players = Main.createPlayers();
        Board board = new Board(players);
        players.get(players.size() - 1).setBrick(3);
        Assert.assertEquals(players.get(players.size() - 1).getBrick(), board.getPlayers().get(players.size() - 1).getBrick());
    }
}
