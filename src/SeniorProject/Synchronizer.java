package SeniorProject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.ini4j.Wini;

class Synchronizer {
    private Wini env_ini;
    private Board board;
    public boolean isWorking = false;

    public Synchronizer(Board board) {
        this.board = board;
        try {
            env_ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));
        } catch (Exception e) {
        }
    }

    public void sync(File file) throws IOException {
        isWorking = true;

        String line;

        int playerIndex;
        ArrayList<Integer> actionParam = new ArrayList<>();
        String actionType;
        String objectType;

        try {

            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                line = scanner.nextLine();


                playerIndex = Integer.parseInt(Character.toString(line.charAt(1))) - 1;
                actionType = String.copyValueOf(line.toCharArray(), 4, 2);

                int value = 7;
                while (value < line.length() - 3) {
                    actionParam.add(Integer.parseInt(String.copyValueOf(line.toCharArray(), value, 2)));
                    value += 3;
                }

                objectType = Character.toString(line.charAt(line.length() - 1));

                switch (actionType){
                    case "CR":
                        if(objectType.equals("S")) { //P1 [CR 16] S
                            board.createSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                        }
                        else if(objectType.equals("R")) { //P1 [CR 10 10] R
                            board.createRoad(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)), board.getLocations().get(actionParam.get(1)));
                        }
                        break;
                    case "UP":
                        if(objectType.equals("S")) { // P1 [UP 10] S
                            board.upgradeSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                        }
                        break;
                    case "MO":
                        if(objectType.equals("T")) { //P1 [MO 11 01 01] T
                            board.moveRobber(board.getPlayers().get(playerIndex), board.getLands().get(actionParam.get(0)), board.getPlayers().get(actionParam.get(1)), ResourceType.values()[actionParam.get(2)]);
                        }
                        break;
                    case "TR":
                        if(objectType.equals("B")) {  //P0 [TR 35 35 35 35 35 35 35 35 35 35] B
                            board.tradeBank(playerIndex, actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
                        }
                        else {  //P0 [TR 35 35 35 35 35 35 35 35 35 35] 1
                            board.tradePlayer(playerIndex, Integer.parseInt(objectType), actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
                        }
                        break;
                    case "RD":  //P0 [RD 06 03] X q
                        board.rollDice(board.getPlayers().get(playerIndex), actionParam.get(0), actionParam.get(1));
                }
                actionParam.clear();
            }

            env_ini.put("General", "isSynchronized[" + 0 +"]", "\"true\"");
            env_ini.put("General", "isSynchronized[" + 1 +"]", "\"true\"");
            env_ini.put("General", "isSynchronized[" + 2 +"]", "\"true\"");
            env_ini.put("General", "isSynchronized[" + 3 +"]", "\"true\"");
            env_ini.store();

        } catch (Exception e) {
        }
        isWorking = false;
    }

    public boolean isSynchronized() {
        String isSynchronized_str = "true";

        try{
            File envFile = new File(Global.get_working_path(Global.ENVIRONMENT_FILE));
            if(envFile.exists()){
                env_ini = new Wini(envFile);
                isSynchronized_str = env_ini.get("General", "isSynchronized[" + 0 + "]", String.class);
                isSynchronized_str = env_ini.get("General", "isSynchronized[" + 1 + "]", String.class);
                isSynchronized_str = env_ini.get("General", "isSynchronized[" + 2 + "]", String.class);
                isSynchronized_str = env_ini.get("General", "isSynchronized[" + 3 + "]", String.class);

                isSynchronized_str = Global.getRidOf_quotationMarks(isSynchronized_str);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return !isSynchronized_str.equals("false");
    }
}