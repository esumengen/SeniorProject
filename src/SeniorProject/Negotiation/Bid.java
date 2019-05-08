package SeniorProject.Negotiation;

import SeniorProject.Resource;

public class Bid {
    private Resource change;

    public Bid(NegotiationAgent owner, NegotiationAgent target, Resource change) {
        this.change = change;
    }

    public Bid(Resource change) {
        this(null, null, change);
    }

    public Resource getChange() {
        return change;
    }

    public void setChange(Resource change) {
        this.change = change;
    }
}
