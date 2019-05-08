package SeniorProject.Negotiation;

import SeniorProject.Player;

public interface NegotiationAgent {
    public Bid handleOffer(NegotiationSession session, Bid offer);
    public Bid handleOffer(NegotiationSession session);

    public boolean isAccepted(NegotiationSession session, Bid offer);

    public Player getOwner(Bid offer);
}