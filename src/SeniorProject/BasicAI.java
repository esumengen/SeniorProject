
package SeniorProject;

import SeniorProject.Actions.CreateRoad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BasicAI implements IAI, Serializable {
    private Player owner;
    private Board board;
    private ArrayList<IAction> actionsDone;
    private Board virtualBoard;
    private Random randomGenerator = new Random();

    public BasicAI(Player player, Board board) {
        actionsDone = new ArrayList<>();
        this.owner = player;
        this.board = board;
    }

    public void clearVirtualBoards() {
        virtualBoard = null;
        System.gc();
    }

    public ArrayList<IAction> createActions(boolean isInitial) {
        clearVirtualBoards();
        if (isInitial)
            this.virtualBoard = Board.deepCopy(board);

        actionsDone.clear();

        if (isInitial) {
            ArrayList<IAction> possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());

            while (possibleActions.size() != 0) {
                IAction action = possibleActions.get(randomGenerator.nextInt(possibleActions.size()));

                actionsDone.add(action);
                action.execute();

                possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());
            }
        }

        return actionsDone;
    }
}