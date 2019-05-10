package SeniorProject;

import SeniorProject.Actions.Action;
import SeniorProject.Actions.DrawDevelopmentCard;
import SeniorProject.Negotiation.Bid;
import SeniorProject.Negotiation.NegotiationAgent;
import SeniorProject.Negotiation.NegotiationSession;
import SeniorProject.Negotiation.Negotiator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BasicAI implements IAI, Serializable {
    private Player owner;
    private Board board;
    private ArrayList<IAction> actionsDone;
    private Board virtualBoard;
    private Random randomGenerator = new Random();
    private ArrayList<Bid> bidRanking;

    public BasicAI(Player player, Board board) {
        actionsDone = new ArrayList<>();
        bidRanking = new ArrayList<>();
        this.owner = player;
        this.board = board;

        updateBidRanking();
    }

    public void clearVirtualBoards() {
        virtualBoard = null;
        System.gc();
    }

    public ArrayList<IAction> createActions(boolean isInitial) {
        clearVirtualBoards();
        this.virtualBoard = Board.deepCopy(board);
        actionsDone.clear();

        ArrayList<NegotiationAgent> otherAgents = new ArrayList<>();
        for (Player player : virtualBoard.getPlayers()) {
            player.getAI().updateBidRanking();

            if (player != owner)
                otherAgents.add(player.getNegotiationAgent());
        }

        NegotiationSession session = new NegotiationSession(owner.getNegotiationAgent(), otherAgents, bidRanking);
        Negotiator.getInstance().setSession(session);
        Negotiator.getInstance().startSession();
        if (session.isCompleted()) {

        }

        ArrayList<IAction> possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());

        while (possibleActions.size() != 0) {
            IAction action = possibleActions.get(randomGenerator.nextInt(possibleActions.size()));

            actionsDone.add(action);
            action.execute();

            possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());
        }

        return actionsDone;
    }

    @Override
    public ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    @Override
    public void updateBidRanking() {
        Resource desiredResource;
        Action desiredAction;
        bidRanking.clear();

        for(int i = 0; i < Action.values().length -1; i++) {
            desiredResource = owner.getResource();
            desiredAction = Action.values()[i];
            if(desiredAction == Action.CreateRoad) {
                desiredResource.disjoin(Road.COST);
                if(desiredResource.sum() >= 0)         // checks if the desired Resource have potential
                    createBids(desiredResource);

            }
            else if (desiredAction == Action.CreateSettlement) {
                desiredResource.disjoin(Settlement.COST);
                if(desiredResource.sum() >= 0)         // checks if the desired Resource have potential
                    createBids(desiredResource);
            }
            else if (desiredAction == Action.UpgradeSettlement) {
                desiredResource.disjoin(City.COST);
                if(desiredResource.sum() >= 0)         // checks if the desired Resource have potential
                    createBids(desiredResource);
            }
            else if (desiredAction == Action.DrawDevCard) {
                desiredResource.disjoin(DrawDevelopmentCard.COST);
                if(desiredResource.sum() >= 0)         // checks if the desired Resource have potential
                    createBids(desiredResource);
            }
            else {
                //generic ranking
                bidRanking.add(new Bid(new Resource(5, -1, 0, 0, 0)));
                bidRanking.add(new Bid(new Resource(5, 0, -1, 0, 0)));
                bidRanking.add(new Bid(new Resource(5, 0, 0, -1, 0)));
                bidRanking.add(new Bid(new Resource(5, 0, 0, 0, -1)));

            }
        }
    }

    private void createBids(Resource desiredResource) {
        Resource wantedResource = new Resource();
        for(ResourceType type : desiredResource.keySet()){
            if(desiredResource.get(type) < 0) {
                wantedResource.add(type, -desiredResource.get(type));
                desiredResource.add(type, -desiredResource.get(type));
            }
        }






    }
}