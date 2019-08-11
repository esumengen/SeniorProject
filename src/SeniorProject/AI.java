package SeniorProject;

import SeniorProject.Negotiation.Bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public abstract class AI implements Serializable {
    Board virtualBoard;
    ArrayList<IAction> possibleActions = new ArrayList<>();
    private Player owner;
    private Board board;
    private ArrayList<Bid> bidRanking = new ArrayList<>();
    private ArrayList<IAction> negotiationActions = new ArrayList<>();
    private ArrayList<IAction> actionsDone = new ArrayList<>();

    public AI () {
        //updateBidRanking();
    }

    /*public AI (Player player, Board board) {
        this.owner = player;
        this.board = board;

        negotiationActions = new ArrayList<>();
        bidRanking = new ArrayList<>();

        updateBidRanking();
    }*/

    public ArrayList<IAction> createActions(boolean isInitial) {
        return null;
    }

    /*public final MoveRobber moveRobber (int landIndex, int victimIndex, ResourceType c, int key) {
        if (key == 23)
            return new MoveRobber(landIndex, getOwner().getIndex(), victimIndex, c, board);
        else
            return null;
    }*/

    public final ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    public final void addBidToBidRanking(Bid bid) {
        bidRanking.add(bid);
        Collections.sort(bidRanking);
    }

    public final void clearBidRanking() {
        bidRanking.clear();
    }

    public final void resetAI() {
        virtualBoard = Board.deepCopy(getBoard());
        actionsDone.clear();
        possibleActions.clear();
        //System.gc();
    }

    public void updateBidRanking() {

    }

    public final void doVirtually(IAction action) {
        actionsDone.add(action);
        action.execute();

        possibleActions = virtualBoard.getState().getPossibleActions(getOwner().getIndex());
    }

    public final void clearNegotiationActions() {
        negotiationActions.clear();
    }

    public final void addNegotiationAction(IAction action) {
        negotiationActions.add(action);
    }

    public final Player getOwner() {
        return owner;
    }

    public final void setOwner(Player owner) {
        this.owner = owner;
    }

    public final Board getBoard() {
        return board;
    }

    public final void setBoard(Board board) {
        this.board = board;
    }

    public final ArrayList<IAction> getActionsDone() {
        return actionsDone;
    }

    public final ArrayList<IAction> getPossibleActions() {
        return possibleActions;
    }

    public final ArrayList<IAction> getNegotiationActions() {
        return negotiationActions;
    }

    public final Board getVirtualBoard() {
        return virtualBoard;
    }
}