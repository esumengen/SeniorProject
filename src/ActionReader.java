import java.io.*;
import java.util.Scanner;

public class ActionReader {
    String path;
    File file;
    Board board;
    int playerIndex;
    String actionType;
    int actionParam;
    String objectType;

    public ActionReader(Board board){
        this.board = board;
        this.path = Board.path + "/actions.txt";
//        this.path = Board.path + "\\actions.txt";
        this.file = new File(path);
        reader(file);
    }

    private void reader(File file){
        String line = "";
        try{
            Scanner scanner = new Scanner(file);
            line = scanner.nextLine();

            playerIndex = Integer.parseInt(Character.toString(line.charAt(1)));
            actionType = String.copyValueOf(line.toCharArray(), 4, 2);
            actionParam = Integer.parseInt(String.copyValueOf(line.toCharArray(), 7, 2));
            objectType = Character.toString(line.charAt(11));

            switch (actionType){
                case "CR":
                    if(objectType.equals("S"))
                    board.createSettlement(board.players.get(playerIndex), board.locations.get(actionParam));
                    break;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(board.structures.get(0).getPlayer().index);
    }
}
