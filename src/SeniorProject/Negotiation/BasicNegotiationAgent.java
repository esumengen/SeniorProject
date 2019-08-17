package SeniorProject.Negotiation;

import java.util.Random;

public class BasicNegotiationAgent extends NegotiationAgent {
    private Random randomMachine = new Random();

    public double getBaseRatio(NegotiationSession session) {
        return (double) session.getTurn(this, session.getOwnerAgent()) / Negotiator.maximumMutualOffers;
    }

    public double getOfferRatio(NegotiationSession session) {
        return (getBaseRatio(session) + randomMachine.nextDouble() * 0.1) * (getBidRanking().size() / 2.0);
    }

    @Override
    public Bid handleOffer(NegotiationSession negotiationSession, Bid bid) {
        if (bid == null) {
            return getBidRanking().get(0);
        } else {
            return getBidRanking().get((int) getOfferRatio(negotiationSession));
        }
    }

    @Override
    public Bid handleOffer(NegotiationSession negotiationSession) {
        return handleOffer(negotiationSession, null);
    }

    @Override
    public boolean isAccepted(NegotiationSession negotiationSession, Bid bid) {
        double ratio = (double) getBidRanking().indexOf(bid) / getBidRanking().size();

        return ratio <= getOfferRatio(negotiationSession);

    }
}