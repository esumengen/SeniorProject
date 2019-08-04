package SeniorProject.Negotiation;

import SeniorProject.Actions.TradeWithPlayer;
import SeniorProject.IAction;
import SeniorProject.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class NegotiationSession {
    private NegotiationAgent ownerAgent;
    private ArrayList<NegotiationAgent> otherAgents;
    private NegotiationAgent bidTarget;

    private boolean isCompleted = false;
    private Bid acceptedOffer;

    // Given bids to the owner.
    private HashMap<NegotiationAgent, ArrayList<Bid>> givenBids = new HashMap<>();

    // Taken bids from the owner.
    private HashMap<NegotiationAgent, ArrayList<Bid>> takenBids = new HashMap<>();

    private boolean isDestroyed = false;

    public NegotiationSession(NegotiationAgent owner, ArrayList<NegotiationAgent> otherAgents, ArrayList<Bid> bidRanking) {
        this.ownerAgent = owner;
        this.otherAgents = otherAgents;

        this.bidTarget = otherAgents.get(0);

        givenBids.put(ownerAgent, new ArrayList<>());
        takenBids.put(ownerAgent, new ArrayList<>());
        for (NegotiationAgent agent : this.otherAgents) {
            givenBids.put(agent, new ArrayList<>());
            takenBids.put(agent, new ArrayList<>());
        }
    }

    public void addGivenBid(Bid bid, NegotiationAgent agent) {
        givenBids.get(agent).add(bid);
    }

    public void addTakenBid(Bid bid, NegotiationAgent agent) {
        takenBids.get(agent).add(bid);
    }

    public int getTurn(NegotiationAgent agent1, NegotiationAgent agent2) {
        return Math.max(takenBids.get(agent1).size(), takenBids.get(agent2).size()) + 1;
    }

    public ArrayList<NegotiationAgent> getOtherAgents() {
        return otherAgents;
    }

    public NegotiationAgent getOwnerAgent() {
        return ownerAgent;
    }

    public void complete(Bid bid, NegotiationAgent targetAgent) {
        if (!isCompleted) {
            acceptedOffer = bid;

            if (bid != null) {
                ownerAgent.getOwner().getAI().clearNegotiationActions();
                IAction tradePlayer_action = new TradeWithPlayer(bid.getNegatives().getChange(), bid.getPositives().getChange(), ownerAgent.getOwner().getIndex(), targetAgent.getOwner().getIndex(), Main.board);
                ownerAgent.getOwner().getAI().addNegotiationAction(tradePlayer_action);
            }

            isCompleted = true;
        }
    }

    public void complete() {
        complete(null, bidTarget);
    }

    public void terminate() {
        isDestroyed = true;
    }

    public NegotiationAgent getBidTarget() {
        return bidTarget;
    }

    public void setBidTarget(NegotiationAgent bidTarget) {
        this.bidTarget = bidTarget;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Bid getAcceptedOffer() {
        return acceptedOffer;
    }
}