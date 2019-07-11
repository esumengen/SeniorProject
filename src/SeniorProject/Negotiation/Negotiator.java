package SeniorProject.Negotiation;

import SeniorProject.Global;

public class Negotiator {
    private static Negotiator instance = new Negotiator();
    private NegotiationSession session;

    private Negotiator() {
    }

    public static Negotiator getInstance() {
        return instance;
    }

    public void setSession(NegotiationSession session) {
        clearSession();
        this.session = session;
    }

    public void clearSession() {
        if (session != null)
            session.terminate();

        this.session = null;
    }

    public boolean startSession() {
        int maximumOffers = 100;
        int countOffers = 0;

        int sequentialPass = 0;
        while (countOffers++ < maximumOffers) {
            Bid offeredBid = session.getOwnerAgent().handleOffer(session);
            session.addGivenBid(offeredBid, session.getBidTarget());

            if (offeredBid == null) {
                sequentialPass++;
                if (sequentialPass == Global.PLAYER_COUNT - 1)
                    break;
            } else
                sequentialPass = 0;

            Bid response = session.getBidTarget().handleOffer(session, offeredBid);
            session.addTakenBid(response, session.getBidTarget());

            boolean isAccepted = (response != null) && session.getOwnerAgent().isAccepted(session, response);

            if (isAccepted) {
                session.complete(response);
                return true;
            } else {
                int bidTarget_index = session.getOtherAgents().indexOf(session.getBidTarget());
                if (bidTarget_index + 1 < session.getOtherAgents().size())
                    session.setBidTarget(session.getOtherAgents().get(bidTarget_index + 1));
                else
                    session.setBidTarget(session.getOtherAgents().get(0));
            }
        }

        session.complete();

        return false;
    }
}