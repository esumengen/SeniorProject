package SeniorProject;

import SeniorProject.Actions.*;

import java.util.ArrayList;

public class ActionFactory {
    static public IAction getAction(String actionText, Board board) {
        int playerIndex;
        ArrayList<Integer> actionParam = new ArrayList<>();
        String actionType;
        String objectType;

        playerIndex = Integer.parseInt(Character.toString(actionText.charAt(1))) - 1;

        actionType = String.copyValueOf(actionText.toCharArray(), 4, 2);

        int value = 7;
        while (value < actionText.length() - 3) {
            actionParam.add(Integer.parseInt(String.copyValueOf(actionText.toCharArray(), value, 2)));
            value += 3;
        }

        objectType = Character.toString(actionText.charAt(actionText.length() - 1));

        switch (actionType) {
            case "CR":
                if (objectType.equals("S")) //P1 [CR 16] S
                    return new CreateSettlement(actionParam.get(0), playerIndex, board);
                else if (objectType.equals("R")) { //P1 [CR 10 10] R
                    int[] locations = new int[2];
                    locations[0] = actionParam.get(0);
                    locations[1] = actionParam.get(1);

                    return new CreateRoad(locations, playerIndex, board);
                }
                break;
            case "UP":
                if (objectType.equals("S")) // P1 [UP 10] S
                    return new UpgradeSettlement(actionParam.get(0), playerIndex, board);
                break;
            case "MO":
                if (objectType.equals("T")) //P1 [MO 11 01] T
                    return new MoveRobber(actionParam.get(0), playerIndex, actionParam.get(1), board);
                break;
            case "TR":
                Resource givenResources = new Resource(actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4));
                Resource takenResources = new Resource(actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));

                if (objectType.equals("B")) {  //P0 [TR 35 35 35 35 35 35 35 35 35 35] B
                    return new TradeWithBank(givenResources, takenResources, playerIndex, board);
                } else { //P0 [TR 35 35 35 35 35 35 35 35 35 35] 1
                    return new TradeWithPlayer(givenResources, takenResources, playerIndex, Integer.parseInt(objectType) - 1, board);
                }
            case "RD":  //P0 [RD 06 03] X q
                return new RollDice(playerIndex, actionParam.get(0), actionParam.get(1), board);
        }

        return null;
    }
}