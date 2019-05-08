package SeniorProject.Negotiation;

import SeniorProject.Player;

public class BasicNegotiationAgent implements NegotiationAgent {
    private Player owner;

    public BasicNegotiationAgent (Player owner) {
        this.owner = owner;
    }

    @Override
    public Bid handleOffer(NegotiationSession session, Bid offer) {
        return null;
    }

    @Override
    public Bid handleOffer(NegotiationSession session) {
        return handleOffer(session, null);
    }

    @Override
    public boolean isAccepted(NegotiationSession session, Bid offer) {
        return false;
    }

    @Override
    public Player getOwner(Bid offer) {
        return owner;
    }
}