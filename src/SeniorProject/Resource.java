package SeniorProject;

import java.util.HashMap;

public class Resource extends HashMap<ResourceType, Integer> {
    public Resource() {
        this(0, 0, 0, 0, 0);
    }

    public Resource(Resource resource) {
        this.put(ResourceType.GRAIN, resource.get(ResourceType.GRAIN));
        this.put(ResourceType.LUMBER, resource.get(ResourceType.LUMBER));
        this.put(ResourceType.WOOL, resource.get(ResourceType.WOOL));
        this.put(ResourceType.ORE, resource.get(ResourceType.ORE));
        this.put(ResourceType.BRICK, resource.get(ResourceType.BRICK));
    }

    public Resource(int grain, int lumber, int wool, int ore, int brick) {
        this.put(ResourceType.GRAIN, grain);
        this.put(ResourceType.LUMBER, lumber);
        this.put(ResourceType.WOOL, wool);
        this.put(ResourceType.ORE, ore);
        this.put(ResourceType.BRICK, brick);
    }

    public void add(ResourceType resourceType, int value) {
        if (resourceType != null)
            put(resourceType, get(resourceType) + value);
    }

    public void join(Resource resource) {
        for (ResourceType type : ResourceType.values())
            this.add(type, resource.get(type));
    }

    public Resource getPositives() {
        Resource positives = new Resource();

        for (ResourceType resourceType : ResourceType.values()) {
            if (get(resourceType) > 0)
                positives.put(resourceType, get(resourceType));
        }

        return positives;
    }

    public Resource getNegatives() {
        Resource negatives = new Resource();

        for (ResourceType resourceType : ResourceType.values()) {
            if (get(resourceType) < 0)
                negatives.put(resourceType, get(resourceType));
        }

        return negatives;
    }

    public void disjoin(Resource resource) {
        for (ResourceType type : ResourceType.values())
            this.add(type, -resource.get(type));
    }

    public int getSum() {
        int sum = 0;

        for (ResourceType type : ResourceType.values())
            sum += this.get(type);

        return sum;
    }

    @Override
    public String toString() {
        return "{GR(" + get(ResourceType.GRAIN) + ") LU(" + get(ResourceType.LUMBER) + ") WO(" + get(ResourceType.WOOL) + ") OR(" + get(ResourceType.ORE) + ") BR(" + get(ResourceType.BRICK) + ")}";
    }
}