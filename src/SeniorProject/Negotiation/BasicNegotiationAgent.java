package SeniorProject.Negotiation;

import java.util.Random;

public class BasicNegotiationAgent extends NegotiationAgent {
    private Random randomizer = new Random();

    public BasicNegotiationAgent() {
        super();
    }

    public double getBaseRatio(NegotiationSession session) {
        return (double) session.getTurn(this, session.getOwnerAgent()) / Negotiator.maximumMutualOffers;
    }

    @Override
    public Bid handleOffer(NegotiationSession session, Bid offer) {
        if (offer == null) {
            return getBidRanking().get(0);
        } else {
            return getBidRanking().get((int) (getBaseRatio(session) + randomizer.nextDouble() * 0.1) * getBidRanking().size() / 4);
        }
    }

    @Override
    public Bid handleOffer(NegotiationSession session) {
        return handleOffer(session, null);
    }

    @Override
    public boolean isAccepted(NegotiationSession session, Bid offer) {
        double ratio = getBidRanking().indexOf(offer) / (getBidRanking().size() / 4);

        return ratio <= getBaseRatio(session);
    }
}