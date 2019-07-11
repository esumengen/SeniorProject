package SeniorProject.Negotiation;

import SeniorProject.Player;

public interface NegotiationAgent {
    Bid handleOffer(NegotiationSession session, Bid offer);

    Bid handleOffer(NegotiationSession session);

    boolean isAccepted(NegotiationSession session, Bid offer);

    Player getOwner(Bid offer);
}