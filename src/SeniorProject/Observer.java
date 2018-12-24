package SeniorProject;

import java.util.ArrayList;

public interface Observer {
    void addSubscriber(Subscriber subscriber);
    ArrayList<Subscriber> getSubscribers();
}
