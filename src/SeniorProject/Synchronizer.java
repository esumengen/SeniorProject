package SeniorProject;

import org.ini4j.Wini;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

enum SynchronizerState {
    RUNNING, WAITING
}

class Synchronizer {
    private SynchronizerState state;
    private Wini communication_ini;
    private Board board;

    Synchronizer(Board board) {
        this.state = SynchronizerState.WAITING;
        this.board = board;

        try {
            communication_ini = new Wini(new File(Global.get_working_path(Global.COMMUNICATION_FILE)));
        } catch (Exception e) {
            new Message(e.getMessage() + " - 4");
        }
    }

    void sync(File file) {
        State.StateBuilder stateBuilder = new State.StateBuilder();
        State currentState = stateBuilder.setPureBoard(Board.deepCopy(board)).initial(false)
                .setResources(board.getPlayers().get(0).getResource(),0)
                .setResources(board.getPlayers().get(1).getResource(),1)
                .setResources(board.getPlayers().get(2).getResource(),2)
                .setResources(board.getPlayers().get(3).getResource(),3)
                .build();

        System.out.println(currentState);
        System.out.println("[Player 2's Affordable Moves]: "+currentState.getAffordableMoves(1));
        System.out.println("[Player 2's Possible Actions]: "+currentState.getPossibleActions(1));

        setState(SynchronizerState.RUNNING);

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

                switch (actionType) {
                    case "CR":
                        if (objectType.equals("S")) { //P1 [CR 16] S
                            board.createSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                        } else if (objectType.equals("R")) { //P1 [CR 10 10] R
                            board.createRoad(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)), board.getLocations().get(actionParam.get(1)));
                        }
                        break;
                    case "UP":
                        if (objectType.equals("S")) { // P1 [UP 10] S
                            board.upgradeSettlement(board.getPlayers().get(playerIndex), board.getLocations().get(actionParam.get(0)));
                        }
                        break;
                    case "MO":
                        if (objectType.equals("T")) { //P1 [MO 11 01 01] T
                            board.moveRobber(board.getPlayers().get(playerIndex), board.getLands().get(actionParam.get(0)), board.getPlayers().get(actionParam.get(1)), ResourceType.values()[actionParam.get(2)]);
                        }
                        break;
                    case "TR":
                        Resource givenResources = new Resource(actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4));
                        Resource takenResources = new Resource(actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));

                        if (objectType.equals("B"))  //P0 [TR 35 35 35 35 35 35 35 35 35 35] B
                            board.tradeBank(playerIndex, givenResources, takenResources);
                        else  //P0 [TR 35 35 35 35 35 35 35 35 35 35] 1
                            board.tradePlayer(playerIndex, Integer.parseInt(objectType), givenResources, takenResources);
                        break;
                    case "RD":  //P0 [RD 06 03] X q
                        board.rollDice(board.getPlayers().get(playerIndex), actionParam.get(0), actionParam.get(1));
                }
                actionParam.clear();
            }

            communication_ini.put("General", "isSynchronized", "\"true\"");
            communication_ini.store();

        } catch (Exception e) {
            new Message(e.getMessage() + " - 2");
        }

        setState(SynchronizerState.WAITING);
    }

    boolean isSynchronized() {
        String isSynchronized_str = "true";

        try {
            File communicationFile = new File(Global.get_working_path(Global.COMMUNICATION_FILE));
            if (communicationFile.exists()) {
                communication_ini = new Wini(communicationFile);
                isSynchronized_str = communication_ini.get("General", "isSynchronized", String.class);

                isSynchronized_str = Global.getRidOf_quotationMarks(isSynchronized_str);
            }
        } catch (Exception e) {
            new Message(e.getMessage() + " - 3");
        }

        return !isSynchronized_str.equals("false");
    }

    SynchronizerState getState() {
        return state;
    }

    void setState(SynchronizerState state) {
        this.state = state;
    }

}