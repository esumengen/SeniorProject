import java.util.ArrayList;

public class Land {
    int index;
    LandType type;
    ArrayList<Location> adjentLocations;
    int diceNo;

    public Land(int index){
        this.adjentLocations = new ArrayList<>();
        this.index = index;
        this.type = LandType.NONE;
    }

    public Land(int index, LandType type){
        this.adjentLocations = new ArrayList<>();
        this.index = index;
        this.type = type;
    }

    public ArrayList<Location> getAdjentLocations() {
        return adjentLocations;
    }
}
