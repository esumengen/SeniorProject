package SeniorProject.Negotiation;

import SeniorProject.Resource;
import SeniorProject.ResourceType;

public class Bid implements Comparable<Bid> {
    static private ResourceType bestType;
    private Resource change;

    public Bid(NegotiationAgent owner, NegotiationAgent target, Resource change) {
        this.change = change;
    }

    public Bid(Resource change) {
        this(null, null, change);
    }

    public static void setBestType(ResourceType bestType) {
        Bid.bestType = bestType;
    }

    public Resource getChange() {
        return change;
    }

    public void setChange(Resource change) {
        this.change = change;
    }

    @Override
    public int compareTo(Bid o) {
        double resourceCount = change.sum();

        double myScore = change.sum() + change.get(bestType);
        double itsScore = o.change.sum() + o.change.get(bestType);

        if (myScore - itsScore == 0.0)
            return 0;

        return (myScore - itsScore > 0.0) ? 1 : -1;
    }
}