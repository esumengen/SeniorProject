package SeniorProject.Negotiation;

import SeniorProject.Resource;
import SeniorProject.ResourceType;

import java.io.Serializable;

public class Bid implements Comparable<Bid>, Serializable {
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

    public Bid getReversed() {
        Resource reversedChange = new Resource(change);

        for (ResourceType resourceType : ResourceType.values()) {
            if (reversedChange.get(resourceType) > 0)
                reversedChange.put(resourceType, -reversedChange.get(resourceType));
            else
                reversedChange.put(resourceType, -reversedChange.get(resourceType));
        }

        return new Bid(reversedChange);
    }

    public Bid getPositives() {
        Resource positives = new Resource();

        for (ResourceType resourceType : ResourceType.values()) {
            if (change.get(resourceType) > 0)
                positives.put(resourceType, change.get(resourceType));
        }

        return new Bid(positives);
    }

    public Bid getNegatives() {
        Resource negatives = new Resource();

        for (ResourceType resourceType : ResourceType.values()) {
            if (change.get(resourceType) < 0)
                negatives.put(resourceType, -change.get(resourceType));
        }

        return new Bid(negatives);
    }

    public Resource getChange() {
        return change;
    }

    private void setChange(Resource change) {
        this.change = change;
    }

    // ? problematic ?
    @Override
    public int compareTo(Bid o) {
        double totalResources = change.getSum();

        double myScore = change.getSum() + change.get(bestType);
        double itsScore = o.change.getSum() + o.change.get(bestType);

        if (myScore - itsScore == 0.0)
            return 0;

        return (myScore - itsScore > 0.0) ? -1 : 1;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Bid) {
            Bid bid = (Bid) object;

            for (ResourceType resourceType : ResourceType.values()) {
                if (bid.getChange().get(resourceType) != change.get(resourceType)) {
                    return false;
                }
            }

            return true;
        } else
            return false;
    }

    @Override
    public String toString() {
        return change.toString();
    }
}