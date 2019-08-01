package SeniorProject.Negotiation;

import SeniorProject.Global;
import SeniorProject.Resource;
import SeniorProject.ResourceType;

import java.util.ArrayList;
import java.util.Collections;

public class Negotiator {
    private static Negotiator instance = new Negotiator();
    public static final int maximumMutualOffers = 100;
    private NegotiationSession session;

    private Negotiator() {
    }

    public static Negotiator getInstance() {
        return instance;
    }

    public void clearSession() {
        if (session != null)
            session.terminate();

        this.session = null;
    }

    public boolean startSession(NegotiationSession session) {
        clearSession();
        this.session = session;

        int countOffers = 0;

        int sequentialPass = 0;
        while (countOffers++ < maximumMutualOffers) {
            boolean isAccepted = false;
            Bid lastBid = null;
            NegotiationAgent targetAgent = session.getBidTarget();
            NegotiationAgent ownerAgent = session.getOwnerAgent();

            Bid offeredBid = ownerAgent.handleOffer(session);
            session.addGivenBid(offeredBid, targetAgent);

            if (targetAgent.getOwner().getAI().getBidRanking().size() > 0 && isAffordable(offeredBid, targetAgent)) {
                if (!targetAgent.getOwner().getAI().getBidRanking().contains(offeredBid.getReversed()))
                    addBid(targetAgent, offeredBid.getReversed());

                    lastBid = offeredBid;

                    if (offeredBid == null) {
                        sequentialPass++;
                        if (sequentialPass == Global.PLAYER_COUNT - 1)
                            break;
                    } else
                        sequentialPass = 0;

                    isAccepted = targetAgent.isAccepted(session, offeredBid.getReversed());

                    if (!isAccepted) {
                        Bid response = targetAgent.handleOffer(session, offeredBid.getReversed());
                        session.addTakenBid(response.getReversed(), targetAgent);

                        if (isAffordable(response, ownerAgent)) {
                            if (!ownerAgent.getOwner().getAI().getBidRanking().contains(response.getReversed()))
                                addBid(ownerAgent, response.getReversed());

                            isAccepted = (response != null) && ownerAgent.isAccepted(session, response.getReversed());
                            if (isAccepted) {
                                lastBid = response.getReversed();
                                System.out.println("    " + targetAgent.getOwner() + ": " + response + "/" + lastBid);
                            }
                        }
                    }
            }

            if (isAccepted) {
                session.complete(lastBid, targetAgent);
                System.out.println("    Negotiation Ended: Agreement between "+ownerAgent.getOwner()+" and "+targetAgent.getOwner());
                System.out.println("    Agreed on " + lastBid.getChange());

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

    public void addBid(NegotiationAgent agent, Bid bid) {
        agent.getOwner().getAI().getBidRanking().add(bid);
        Collections.sort(agent.getOwner().getAI().getBidRanking());

        System.out.println("    " + bid + " is added to " + agent.getOwner() + "'s list. [" + (agent.getOwner().getAI().getBidRanking().indexOf(bid) + 1) + ". order]");
    }

    public boolean isAffordable (Bid offeredBid, NegotiationAgent agent) {
        if (offeredBid != null) {
            Resource _resource = new Resource(agent.getOwner().getResource());
            _resource.disjoin(offeredBid.getChange());
            for (ResourceType resourceType : ResourceType.values()) {
                if (_resource.get(resourceType) < 0) {
                    return false;
                }
            }
        }

        return true;
    }
}