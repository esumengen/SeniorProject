package SeniorProject;

import java.util.ArrayList;

public interface IObservable {
    void addObserver(IObserver observer);

    ArrayList<IObserver> getObservers();
}
