package SeniorProject;

import java.io.*;
import java.util.Scanner;

class Synchronizer {
    private File actions_file;
    private Board board;

    public Synchronizer(Board board){
        this.board = board;

        this.actions_file = new File(Global.get_working_path(Global.ACTIONS_FILE));
        sync(actions_file);
    }

    private void sync(File file){
        String line;

        int playerIndex;
        int actionParam;
        int actionParam2;
        String actionType;
        String objectType;
        int wheatG = 0, woodG = 0, woolG = 0, stoneG = 0, brickG = 0, wheatT = 0, woodT = 0, woolT = 0, stoneT = 0, brickT = 0;

        try {
            Scanner scanner = new Scanner(file);
            line = scanner.nextLine();

            playerIndex = Integer.parseInt(Character.toString(line.charAt(1)));
            actionType = String.copyValueOf(line.toCharArray(), 4, 2);
            objectType = Character.toString(line.charAt(line.length()-1));
            actionParam = Integer.parseInt(String.copyValueOf(line.toCharArray(), 7, 2));
            actionParam2 = (objectType == "R") ? Integer.parseInt(String.copyValueOf(line.toCharArray(), 10, 2)) :0;
            if(actionType == "TR"){
                wheatG = Integer.parseInt(String.copyValueOf(line.toCharArray(), 10, 2));
                woodG = Integer.parseInt(String.copyValueOf(line.toCharArray(), 13, 2));
                woolG = Integer.parseInt(String.copyValueOf(line.toCharArray(), 16, 2));
                stoneG = Integer.parseInt(String.copyValueOf(line.toCharArray(), 19, 2));
                brickG = Integer.parseInt(String.copyValueOf(line.toCharArray(), 22, 2));
                wheatT = Integer.parseInt(String.copyValueOf(line.toCharArray(), 25, 2));
                woodT = Integer.parseInt(String.copyValueOf(line.toCharArray(), 28, 2));
                woolT = Integer.parseInt(String.copyValueOf(line.toCharArray(), 31, 2));
                stoneT = Integer.parseInt(String.copyValueOf(line.toCharArray(), 34, 2));
                brickT = Integer.parseInt(String.copyValueOf(line.toCharArray(), 37, 2));
            }

            switch (actionType){
                case "CR":
                    if(objectType.equals("S")) {
                        board.createSettlement(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam));
                    }
                    else if(objectType.equals("R")) {
                        board.createRoad(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam), Board.getLocations().get(actionParam2));
                    }
                    else if(objectType.equals("U")) {
                        board.upgradeSettlement(Board.getPlayers().get(playerIndex), Board.getLocations().get(actionParam));
                    }
                    break;
                case "TR":
                    if(objectType.equals("B")) {
                        board.tradeBank(playerIndex, wheatG, woodG, woolG, stoneG, brickG, wheatT, woodT, woolT, stoneT, brickT);
                    }
                    else if(objectType.equals("0") || objectType.equals("1") || objectType.equals("2") || objectType.equals("3")) {
                        board.tradePlayer(playerIndex, objectType.toCharArray()[1], wheatG, woodG, woolG, stoneG, brickG, wheatT, woodT, woolT, stoneT, brickT);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}