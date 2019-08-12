package SeniorProject.Negotiation;

import SeniorProject.AI;
import SeniorProject.Message;
import SeniorProject.Resource;
import SeniorProject.ResourceType;

import java.io.Serializable;
import java.util.Objects;

public class Bid implements Comparable<Bid>, Serializable {
    private Resource change;
    private AI utilityFunction_owner = null;

    public Bid(Resource change) {
        this.change = change;
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

    @Override
    public int compareTo(Bid o) {
        double myUtility = getUtilityFunction_owner().calculateBidUtility(this);
        double itsUtility = o.getUtilityFunction_owner().calculateBidUtility(o);

        if (myUtility - itsUtility == 0.0)
            return 0;

        return (myUtility - itsUtility > 0.0) ? -1 : 1;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Bid) {
            Bid bid = (Bid) object;

            for (ResourceType resourceType : ResourceType.values()) {
                if (!Objects.equals(bid.getChange().get(resourceType), change.get(resourceType))) {
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

    Bid setUtilityFunction_owner_protected(AI utilityFunction_owner) {
        this.utilityFunction_owner = utilityFunction_owner;

        return this;
    }

    AI getUtilityFunction_owner() {
        return utilityFunction_owner;
    }

    public Bid setUtilityFunction_owner(AI utilityFunction_owner) {
        if (this.utilityFunction_owner == null || utilityFunction_owner == this.utilityFunction_owner)
            this.utilityFunction_owner = utilityFunction_owner;
        else
            new Message("Access to setUtilityFunction_owner is denied. (Err: 884)");

        return this;
    }
}