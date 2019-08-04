package SeniorProject.Negotiation;

import SeniorProject.Player;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class NegotiationAgent implements Serializable {
    private Player owner = null;
    private ArrayList<Bid> bidRanking = null;

    public NegotiationAgent() {

    }

    public Bid handleOffer(NegotiationSession session, Bid offer) {
        return null;
    }

    public Bid handleOffer(NegotiationSession session) {
        return null;
    }

    public boolean isAccepted(NegotiationSession session, Bid offer) {
        return false;
    }

    public final Player getOwner() {
        return owner;
    }

    public final void setOwner(Player owner) {
        this.owner = owner;
    }

    public final ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    public final void setBidRanking(ArrayList<Bid> bidRanking) {
        this.bidRanking = bidRanking;
    }
}