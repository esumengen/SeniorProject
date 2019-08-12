package SeniorProject;

import SeniorProject.Negotiation.Bid;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public abstract class AI implements Serializable {
    Board virtualBoard;
    ArrayList<IAction> possibleActions = new ArrayList<>();
    private Player owner;
    private Board board;
    private ArrayList<Bid> bidRanking = new ArrayList<>();
    private ArrayList<IAction> negotiationActions = new ArrayList<>();
    private ArrayList<IAction> actionsDone = new ArrayList<>();

    static final StructureType SETTLEMENT = StructureType.SETTLEMENT;
    static final ResourceType BRICK = ResourceType.BRICK;
    static final ResourceType LUMBER = ResourceType.LUMBER;
    static final ResourceType WOOL = ResourceType.WOOL;
    static final ResourceType ORE = ResourceType.ORE;

    public abstract ArrayList<IAction> createActions(boolean isInitial);

    public abstract double calculateBidUtility(Bid bid);

    public abstract void updateBidRanking();

    public AI () {
    }

    public final ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    public final void addBidToBidRanking(Bid bid) {
        bidRanking.add(bid.setUtilityFunction_owner(this));
        Collections.sort(bidRanking);
    }

    public final void clearBidRanking() {
        bidRanking.clear();
    }

    public final void resetAI() {
        virtualBoard = Board.deepCopy(getBoard());
        actionsDone.clear();
        possibleActions.clear();
        //System.gc();
    }

    public final void doVirtually(IAction action) {
        actionsDone.add(action);
        action.execute();

        possibleActions = virtualBoard.getState().getPossibleActions(getOwner().getIndex());
    }

    public final void clearNegotiationActions() {
        negotiationActions.clear();
    }

    public final void addNegotiationAction(IAction action) {
        negotiationActions.add(action);
    }

    public final Player getOwner() {
        return owner;
    }

    public final void setOwner(Player owner) {
        this.owner = owner;
    }

    public final Board getBoard() {
        return board;
    }

    public final void setBoard(Board board) {
        this.board = board;
    }

    public final ArrayList<IAction> getActionsDone() {
        return actionsDone;
    }

    public final ArrayList<IAction> getPossibleActions() {
        return possibleActions;
    }

    public final ArrayList<IAction> getNegotiationActions() {
        return negotiationActions;
    }

    public final Board getVirtualBoard() {
        return virtualBoard;
    }

    public final AbstractMap.SimpleEntry<Integer, ArrayList<Road>> getShortestPath(Location locationSource, Location locationTarget, boolean ignoreBuildings) {
        if (locationSource.getIndex() == locationTarget.getIndex())
            return new AbstractMap.SimpleEntry<>(0, null);

        ArrayList<Node> nodes = new ArrayList<>();

        for (Location location : getVirtualBoard().getLocations()) {
            if (location.isActive()) {
                for (Location _location : location.getAdjacentLocations()) {
                    Node node = null;
                    boolean node_exists = false;

                    Node _node = null;
                    boolean _node_exists = false;

                    for (Node nnode : nodes) {
                        if (nnode.getIndex() == location.getIndex()) {
                            node = nnode;
                            node_exists = true;
                            continue;
                        } else if (nnode.getIndex() == _location.getIndex()) {
                            _node = nnode;
                            _node_exists = true;
                            continue;
                        }
                    }

                    if (!node_exists) {
                        node = new Node(location.getIndex());
                        nodes.add(node);
                    }
                    if (!_node_exists) {
                        _node = new Node(_location.getIndex());
                        nodes.add(_node);
                    }

                    if (!node.getAdjacentNodes_manual().contains(_node))
                        node.getAdjacentNodes_manual().add(_node);

                    if (!_node.getAdjacentNodes_manual().contains(node))
                        _node.getAdjacentNodes_manual().add(node);
                }
            }
        }

        for (Structure structure : getVirtualBoard().getStructures()) {
            if (structure instanceof Road && structure.getPlayer().getIndex() != getOwner().getIndex()) {
                Road road = (Road) structure;

                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getIndex() == road.getStartLocation().getIndex()) {
                        Node nodeDeleted = nodes.get(i);

                        for (int j = 0; j < nodeDeleted.getAdjacentNodes_manual().size(); j++) {
                            if (nodeDeleted.getAdjacentNodes_manual().get(j).getIndex() == road.getEndLocation().getIndex()) {
                                Node nodeDeleted2 = nodeDeleted.getAdjacentNodes_manual().get(j);

                                for (Node x : nodeDeleted.getAdjacentNodes_manual()) {
                                    if (x.getIndex() == nodeDeleted2.getIndex()) {
                                        nodeDeleted.getAdjacentNodes_manual().remove(x);
                                        break;
                                    }
                                }
                                for (Node x : nodeDeleted2.getAdjacentNodes_manual()) {
                                    if (x.getIndex() == nodeDeleted.getIndex()) {
                                        nodeDeleted2.getAdjacentNodes_manual().remove(x);
                                        break;
                                    }
                                }

                                /*System.out.println(nodeDeleted2.getIndex() + "is deleted from " + nodeDeleted.getIndex());
                                System.out.println(nodeDeleted.getIndex() + "is deleted from " + nodeDeleted2.getIndex());*/
                            }
                        }
                    }
                }
            } else if (ignoreBuildings && structure instanceof Building && structure.getPlayer().getIndex() != getOwner().getIndex()) {
                Building building = (Building) structure;

                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getIndex() == building.getLocation().getIndex()) {
                        Node nodeDisconnected = nodes.get(i);

                        for (int j = 0; j < nodeDisconnected.getAdjacentNodes_manual().size(); j++) {
                            Node adjacentNode = nodeDisconnected.getAdjacentNodes_manual().get(j);

                            nodeDisconnected.getAdjacentNodes_manual().remove(adjacentNode);
                            j--;

                            adjacentNode.getAdjacentNodes_manual().remove(nodeDisconnected);
                        }
                    }
                }
            }
        }

        Node startNode = null;
        Node targetNode = null;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getIndex() == locationSource.getIndex()) {
                startNode = nodes.get(i);
            } else if (nodes.get(i).getIndex() == locationTarget.getIndex()) {
                targetNode = nodes.get(i);
            }
        }

        ArrayDeque<Node> stack = new ArrayDeque();
        ArrayDeque<ArrayList<Node>> chainStack = new ArrayDeque();

        int minDistance = -1;
        ArrayList<Node> minChain = new ArrayList<>();

        ArrayList<Node> chain = new ArrayList<>();
        chain.add(startNode);

        stack.addLast(startNode);
        chainStack.addLast(chain);

        while (!stack.isEmpty()) {
            Node _node = stack.removeFirst();
            ArrayList<Node> _chain = chainStack.removeFirst();

            for (Node adjNode : _node.getAdjacentNodes_manual()) {
                if (!_chain.contains(adjNode)) {
                    if (adjNode.getIndex() == targetNode.getIndex()) {
                        minDistance = _chain.size();

                        _chain.add(adjNode);
                        minChain = _chain;
                        break;
                    } else {
                        ArrayList<Node> newChain = new ArrayList<>(_chain);
                        newChain.add(adjNode);

                        stack.addLast(adjNode);
                        chainStack.addLast(newChain);
                    }
                }
            }

            if (minDistance != -1)
                break;
        }

        ArrayList<Road> resultRoads = new ArrayList<>();
        for (int i = 0; i < minChain.size() - 1; i++) {
            resultRoads.add(new Road(getVirtualBoard().getLocations().get(minChain.get(i).getIndex()), getVirtualBoard().getLocations().get(minChain.get(i + 1).getIndex()), getOwner()));
        }

        return new AbstractMap.SimpleEntry<>(minDistance, resultRoads);
    }
}