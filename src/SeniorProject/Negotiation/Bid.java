package SeniorProject.Negotiation;

import SeniorProject.Resource;

public class Bid {
    private NegotiationAgent owner;
    private NegotiationAgent target;
    private Resource change;

    public Bid(NegotiationAgent owner, NegotiationAgent target, Resource change) {
        this.owner = owner;
        this.target = target;
        this.change = change;
    }

    public Bid(Resource change) {
        this(null, null, change);
    }

    public NegotiationAgent getOwner() {
        return owner;
    }

    public NegotiationAgent getTarget() {
        return target;
    }

    public Resource getChange() {
        return change;
    }

    public void setChange(Resource change) {
        this.change = change;
    }
}
