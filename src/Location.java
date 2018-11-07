import java.util.ArrayList;

public class Location {
    int index;
    ArrayList<Land> adjentLands;

    public Location(int index) {
        this.adjentLands = new ArrayList<>();
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Land> getAdjentLands() {
        return adjentLands;
    }
}
