package SeniorProject;

import SeniorProject.Negotiation.Bid;

import java.util.ArrayList;

public interface IAI {
    ArrayList<IAction> createActions(boolean isInitial);

    ArrayList<Bid> getBidRanking();

    void updateBidRanking();

    void clearNegotiationActions();

    void addNegotiationAction(IAction action);
}