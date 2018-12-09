package SeniorProject;

import java.io.*;
import java.util.ArrayList;
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
        ArrayList<Integer> actionParam = new ArrayList<>();
        String actionType;
        String objectType;

        try {
            Scanner scanner = new Scanner(file);
            line = scanner.nextLine();


            playerIndex = Integer.parseInt(Character.toString(line.charAt(1)));
            actionType = String.copyValueOf(line.toCharArray(), 4, 2);

            int value = 7;
            while (value < line.length()-3) {
                actionParam.add(Integer.parseInt(String.copyValueOf(line.toCharArray(), value, 2)));
                value += 3;
            }

            objectType = Character.toString(line.charAt(line.length() - 1));

            switch (actionType){
                case "CR":
                    if(objectType.equals("S")) {
                        board.createSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                    }
                    else if(objectType.equals("R")) {
                        board.createRoad(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)), board.getLocations().get(actionParam.get(1)));
                    }
                    break;
                case "UP":
                    if(objectType.equals("S")) {
                        board.upgradeSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                    }
                    break;
                case "MO":
                    if(objectType.equals("T")) {
                        board.moveRobber(board.getPlayers().get(playerIndex), board.getLands().get(actionParam.get(0)));
                    }
                    break;
                case "TR":
                    if(objectType.equals("B")) {
                        board.tradeBank(playerIndex, actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
                    }
                    else {
                        board.tradePlayer(playerIndex, Integer.parseInt(objectType), actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
                    }
                    break;
                case "RD":
                        board.rollDice(board.getPlayers().get(playerIndex), actionParam.get(0), actionParam.get(1));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}