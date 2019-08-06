package SeniorProject.Negotiation;

import java.util.Random;

public class BasicNegotiationAgent extends NegotiationAgent {
    private Random randomizer = new Random();

    public double getBaseRatio(NegotiationSession session) {
        return (double) session.getTurn(this, session.getOwnerAgent()) / Negotiator.maximumMutualOffers;
    }

    @Override
    public Bid handleOffer(NegotiationSession negotiationSession, Bid bid) {
        if (bid == null) {
            return getBidRanking().get(0);
        } else {
            return getBidRanking().get((int) (getBaseRatio(negotiationSession) + randomizer.nextDouble() * 0.1) * getBidRanking().size() / 4);
        }
    }

    @Override
    public Bid handleOffer(NegotiationSession negotiationSession) {
        return handleOffer(negotiationSession, null);
    }

    @Override
    public boolean isAccepted(NegotiationSession negotiationSession, Bid bid) {
        double ratio = (double) getBidRanking().indexOf(bid) / (getBidRanking().size() / 4);

        return ratio <= getBaseRatio(negotiationSession);
    }
}
