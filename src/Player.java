import java.util.ArrayList;

public class Player {
    String name;
    int index;
    int aiType;
    ArrayList<Road> roads = new ArrayList<>();
    ArrayList<Settlement> settlements = new ArrayList<>();
    ArrayList<City> cities = new ArrayList<>();

    public Player (int index){
        this.index = index;
    }

}
