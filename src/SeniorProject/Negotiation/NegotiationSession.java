package SeniorProject.Negotiation;

import SeniorProject.Actions.TradeWithPlayer;
import SeniorProject.IAction;

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

    void addGivenBid(Bid bid, NegotiationAgent agent) {
        givenBids.get(agent).add(bid);
    }

    void addTakenBid(Bid bid, NegotiationAgent agent) {
        takenBids.get(agent).add(bid);
    }

    int getTurn(NegotiationAgent agent1, NegotiationAgent agent2) {
        return Math.max(takenBids.get(agent1).size(), takenBids.get(agent2).size()) + 1;
    }

    public ArrayList<NegotiationAgent> getOtherAgents() {
        return otherAgents;
    }

    public NegotiationAgent getOwnerAgent() {
        return ownerAgent;
    }

    void complete(Bid bid, NegotiationAgent targetAgent) {
        if (!isCompleted) {
            acceptedOffer = bid;

            if (bid != null) {
                ownerAgent.getOwner().getAI().clearNegotiationActions();
                IAction tradePlayer_action = new TradeWithPlayer(bid.getNegatives().getChange(), bid.getPositives().getChange(), ownerAgent.getOwner().getIndex(), targetAgent.getOwner().getIndex(), null);
                ownerAgent.getOwner().getAI().addNegotiationAction(tradePlayer_action);
            }

            isCompleted = true;
        }
    }

    void complete() {
        complete(null, bidTarget);
    }

    public NegotiationAgent getBidTarget() {
        return bidTarget;
    }

    void setBidTarget(NegotiationAgent bidTarget) {
        this.bidTarget = bidTarget;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Bid getAcceptedOffer() {
        return acceptedOffer;
    }
}