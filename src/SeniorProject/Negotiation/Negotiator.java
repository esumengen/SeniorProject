package SeniorProject.Negotiation;

public class Negotiator {
    private NegotiationSession session;
    private static Negotiator instance = new Negotiator();

    private Negotiator() {
    }

    public static Negotiator getInstance() {
        return instance;
    }

    public void setSession(NegotiationSession session) {
        session.destroy();
        this.session = session;
    }

    public void clearSession() {
        if (session != null)
            session.destroy();

        this.session = null;
    }

    public boolean startSession() {
        int maximumOffers = 500;
        int countOffers = 0;

        while (countOffers++ < maximumOffers) {
            Bid offeredBid = session.getOwnerAgent().handleOffer(session);
            session.addGivenBid(offeredBid, session.getBidTarget());

            Bid response = session.getBidTarget().handleOffer(session, offeredBid);
            session.addTakenBid(response, session.getBidTarget());

            boolean isAccepted = session.getOwnerAgent().isAccepted(session, response);

            if (isAccepted) {
                session.complete(response);
                return true;
            }
            else {
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