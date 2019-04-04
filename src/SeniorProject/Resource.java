package SeniorProject;
import java.util.HashMap;

public class Resource extends HashMap<ResourceType, Integer> {
    public Resource () {
        this(0, 0, 0, 0, 0);
    }

    public Resource (Resource resource) {
        this.put(ResourceType.GRAIN, resource.get(ResourceType.GRAIN));
        this.put(ResourceType.LUMBER, resource.get(ResourceType.LUMBER));
        this.put(ResourceType.WOOL, resource.get(ResourceType.WOOL));
        this.put(ResourceType.ORE, resource.get(ResourceType.ORE));
        this.put(ResourceType.BRICK, resource.get(ResourceType.BRICK));
    }

    public Resource (int grain, int lumber, int wool, int ore, int brick) {
        this.put(ResourceType.GRAIN, grain);
        this.put(ResourceType.LUMBER, lumber);
        this.put(ResourceType.WOOL, wool);
        this.put(ResourceType.ORE, ore);
        this.put(ResourceType.BRICK, brick);
    }

    public void add (ResourceType resourceType, int value ) {
        this.put(resourceType, this.get(resourceType) + value);
    }
}