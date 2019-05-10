package SeniorProject;

import SeniorProject.Actions.CreateSettlement;
import SeniorProject.Negotiation.Bid;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.*;

public class BasicAI implements IAI, Serializable {
    private Player owner;
    private Board board;

    private ArrayList<IAction> actionsDone;
    private static final StructureType SETTLEMENT = StructureType.SETTLEMENT;
    private Board virtualBoard;

    private ArrayList<Bid> bidRanking;
    private static final ResourceType BRICK = ResourceType.BRICK;
    private static final ResourceType LUMBER = ResourceType.LUMBER;
    private ArrayList<IAction> possibleActions;
    private HashMap<StructureType, Double[]> locScores;
    private Random randomGenerator = new Random();

    public BasicAI(Player player, Board board) {
        actionsDone = new ArrayList<>();
        possibleActions = new ArrayList<>();
        bidRanking = new ArrayList<>();

        locScores = new HashMap<>();
        locScores.put(SETTLEMENT, new Double[board.getLocations().size()]);

        this.owner = player;
        this.board = board;

        updateBidRanking();
        updateLocationScores(SETTLEMENT);
    }

    public ArrayList<IAction> createActions(boolean isInitial) {
        clearSystem();

        negotiationPhase();

        possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());

        ArrayList<CreateSettlement> actions_settlements = getActions_of(possibleActions, CreateSettlement.class);

        AbstractMap.SimpleEntry<Integer, ArrayList<Road>> minRoad_info = getShortestPath(virtualBoard.getLocations().get(17), virtualBoard.getLocations().get(82));
        System.out.println("Length: "+minRoad_info.getKey());
        for (Road road : minRoad_info.getValue()) {
            System.out.println(road.getStartLocation()+", "+road.getEndLocation());
        }

        //region While a settlement can be built, build it.
        Double[] _locScores = locScores.get(SETTLEMENT);
        while (actions_settlements.size() > 0) {
            for (int i = 0; i < _locScores.length; i++) {
                _locScores[i] = Math.abs(_locScores[i]);
                _locScores[i] *= -1;
            }

            for (CreateSettlement createSettlement : actions_settlements)
                _locScores[createSettlement.getLocation().getIndex()] *= -1;

            Location bestLocation = null;
            for (int i = 0; i < _locScores.length; i++) {
                if (bestLocation == null || _locScores[i] > _locScores[bestLocation.getIndex()])
                    bestLocation = virtualBoard.getLocations().get(i);
            }

            for (CreateSettlement createSettlement : actions_settlements) {
                if (createSettlement.getLocation().equals(bestLocation)) {
                    doVirtually(createSettlement);
                    break;
                }
            }

            actions_settlements = getActions_of(possibleActions, CreateSettlement.class);
        }
        ///endregion

        ///region Do actions randomly.
        while (possibleActions.size() != 0) {
            IAction action = possibleActions.get(randomGenerator.nextInt(possibleActions.size()));
            doVirtually(action);
        }
        ///endregion

        if (!isInitial)
            actionsDone.clear();
        return actionsDone;
    }

    private void negotiationPhase() {
        ///region
        /*ArrayList<NegotiationAgent> otherAgents = new ArrayList<>();
        for (Player player : virtualBoard.getPlayers()) {
            player.getAI().updateBidRanking();

            if (player != owner)
                otherAgents.add(player.getNegotiationAgent());
        }

        NegotiationSession session = new NegotiationSession(owner.getNegotiationAgent(), otherAgents, bidRanking);
        Negotiator.getInstance().setSession(session);
        Negotiator.getInstance().startSession();
        if (session.isCompleted()) {

        }*/
        ///endregion
    }

    private void updateLocationScores(StructureType structureType) {
        Double[] _locScores = locScores.get(structureType);

        if (structureType == SETTLEMENT) {
            for (int i = 0; i < _locScores.length; i++) {
                Location location = board.getLocations().get(i);

                _locScores[i] = 0.0;
                for (Land land : location.getAdjacentLands()) {
                    _locScores[i] += location.isActive() ? 0.01 : 0;
                    _locScores[i] += land.getDiceChance() * 36.0;
                    _locScores[i] += (land.getResourceType() == LUMBER ? 3 : 0) + (land.getResourceType() == BRICK ? 3 : 0);
                }
            }
        }
    }

    public void clearSystem() {
        virtualBoard = Board.deepCopy(board);
        actionsDone.clear();
        possibleActions.clear();
        System.gc();
    }

    public <T> ArrayList<T> getActions_of(ArrayList<IAction> actions, Class T) {
        ArrayList<T> selectedActions = new ArrayList<>();

        for (IAction action : actions) {
            if (action.getClass() == T)
                selectedActions.add((T) action);
        }

        return selectedActions;
    }

    public void doVirtually(IAction action) {
        actionsDone.add(action);
        action.execute();

        possibleActions = virtualBoard.getState().getPossibleActions(owner.getIndex());
    }

    @Override
    public ArrayList<Bid> getBidRanking() {
        return bidRanking;
    }

    @Override
    public void updateBidRanking() {
        bidRanking.clear();

        bidRanking.add(new Bid(new Resource(5, -1, 0, 0, 0)));
        bidRanking.add(new Bid(new Resource(5, 0, -1, 0, 0)));
        bidRanking.add(new Bid(new Resource(5, 0, 0, -1, 0)));
        bidRanking.add(new Bid(new Resource(5, 0, 0, 0, -1)));
    }

    public AbstractMap.SimpleEntry<Integer, ArrayList<Road>> getShortestPath (Location locationSource, Location locationTarget) {
        if (locationSource.getIndex() == locationTarget.getIndex())
            return new AbstractMap.SimpleEntry<>(0, null);

        ArrayList<Node> nodes = new ArrayList<>();

        for (Location location : virtualBoard.getLocations()) {
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
                        }
                        else if (nnode.getIndex() == _location.getIndex()) {
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

                    node.getAdjacentNodes_manual().add(_node);
                    _node.getAdjacentNodes_manual().add(node);
                }
            }
        }

        for (Structure structure : virtualBoard.getStructures()) {
            if (structure instanceof Road && structure.getPlayer().getIndex() != owner.getIndex()) {
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
                                //nodeDeleted.getAdjacentNodes_manual().remove(nodeDeleted2);
                                //nodeDeleted2.getAdjacentNodes_manual().remove(nodeDeleted);

                                System.out.println(nodeDeleted2.getIndex() + "is deleted from " + nodeDeleted.getIndex());
                                System.out.println(nodeDeleted.getIndex() + "is deleted from " + nodeDeleted2.getIndex());
                            }
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
            }
            else if (nodes.get(i).getIndex() == locationTarget.getIndex()) {
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
        for (int i = 0; i < minChain.size()-1; i++) {
            resultRoads.add(new Road(virtualBoard.getLocations().get(minChain.get(i).getIndex()), virtualBoard.getLocations().get(minChain.get(i + 1).getIndex()), owner));
        }

        return new AbstractMap.SimpleEntry<>(minDistance, resultRoads);
    }
}