package SeniorProject.Negotiation;

import java.util.ArrayList;
import java.util.HashMap;

public class NegotiationSession {
    private NegotiationAgent ownerAgent;
    private ArrayList<NegotiationAgent> otherAgents;
    private ArrayList<Bid> bidRanking;
    private NegotiationAgent bidTarget;

    private boolean isCompleted = false;
    private NegotiationAgent acceptedOffer_owner;
    private Bid acceptedOffer;

    private HashMap<NegotiationAgent, ArrayList<Bid>> givenBids = new HashMap<>();
    private HashMap<NegotiationAgent, ArrayList<Bid>> takenBids = new HashMap<>();

    private boolean isDestroyed = false;

    public NegotiationSession(NegotiationAgent owner, ArrayList<NegotiationAgent> otherAgents, ArrayList<Bid> bidRanking) {
        for (NegotiationAgent agent : this.otherAgents) {
            givenBids.put(agent, new ArrayList<>());
            takenBids.put(agent, new ArrayList<>());
        }

        this.ownerAgent = ownerAgent;
        this.otherAgents = this.otherAgents;
        this.bidRanking = bidRanking;
    }

    public void addGivenBid (Bid bid, NegotiationAgent agent) {
        givenBids.get(agent).add(bid);
    }

    public void addTakenBid (Bid bid, NegotiationAgent agent) {
        takenBids.get(agent).add(bid);
    }

    public ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    public ArrayList<NegotiationAgent> getOtherAgents() {
        return otherAgents;
    }

    public NegotiationAgent getOwnerAgent() {
        return ownerAgent;
    }

    public void complete(Bid bid) {
        acceptedOffer = bid;

        if (bid != null)
            acceptedOffer_owner = bidTarget;
        else
            acceptedOffer_owner = null;

        isCompleted = true;
    }

    public void complete() {
        complete(null);
    }

    public void destroy() {
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
}
