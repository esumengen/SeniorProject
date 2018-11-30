package SeniorProject;

import java.io.*;
import java.util.Scanner;

class Synchronizer {
    private File actions_file;
    private Board board;

    public Synchronizer(Board board){
        this.board = board;

        this.actions_file = new File(Global.get_working_path(Global.ACTIONS_FILE));
        reader(actions_file);
    }

    private void reader(File file){
        String line;

        int playerIndex;
        int actionParam;
        int actionParam2 = 0;
        String actionType;
        String objectType;

        try {
            Scanner scanner = new Scanner(file);
            line = scanner.nextLine();

            playerIndex = Integer.parseInt(Character.toString(line.charAt(1)));
            actionType = String.copyValueOf(line.toCharArray(), 4, 2);
            actionParam = Integer.parseInt(String.copyValueOf(line.toCharArray(), 7, 2));
            if(actionType == "R")
                actionParam2 = Integer.parseInt(String.copyValueOf(line.toCharArray(), 9, 2));
            objectType = Character.toString(line.charAt(11));

            switch (actionType){
                case "CR":
                    if(objectType.equals("S")) {
                        board.createSettlement(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam));
                    }
                    else if(objectType.equals("R")) {
                        board.createRoad(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam), Board.getLocations().get(actionParam2));
                    }
                    else if(objectType.equals("U")) {
                        board.upgradeSettlementtoCity(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam));
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}