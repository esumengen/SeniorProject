package SeniorProject.Negotiation;

import SeniorProject.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BasicNegotiationAgent implements NegotiationAgent, Serializable {
    private Player owner;
    private ArrayList<Bid> bidRanking;
    private Random randomizer = new Random();

    public BasicNegotiationAgent(Player owner) {
        this.owner = owner;
        this.bidRanking = owner.getAI().getBidRanking();
    }

    public double getBaseRatio(NegotiationSession session) {
        return (double) session.getTurn(this, session.getOwnerAgent()) / Negotiator.maximumMutualOffers;
    }

    @Override
    public Bid handleOffer(NegotiationSession session, Bid offer) {
        if (offer == null) {
            return bidRanking.get(0);
        }
        else {
            return bidRanking.get((int) (getBaseRatio(session) + randomizer.nextDouble() * 0.1) * bidRanking.size() / 4);
        }
    }

    @Override
    public Bid handleOffer(NegotiationSession session) {
        return handleOffer(session, null);
    }

    @Override
    public boolean isAccepted(NegotiationSession session, Bid offer) {
        double ratio = bidRanking.indexOf(offer) / (bidRanking.size() / 4);

        return ratio <= getBaseRatio(session);
    }

    @Override
    public Player getOwner() {
        return owner;
    }
}