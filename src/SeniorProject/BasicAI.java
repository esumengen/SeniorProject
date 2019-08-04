package SeniorProject;

import SeniorProject.Actions.Action;
import SeniorProject.Actions.CreateRoad;
import SeniorProject.Actions.CreateSettlement;
import SeniorProject.Actions.DrawDevelopmentCard;
import SeniorProject.Negotiation.Bid;

import java.util.*;

public class BasicAI extends AI {
    private static final StructureType SETTLEMENT = StructureType.SETTLEMENT;
    private static final ResourceType BRICK = ResourceType.BRICK;
    private static final ResourceType LUMBER = ResourceType.LUMBER;

    private HashMap<StructureType, Double[]> locScores;
    private Random randomGenerator = new Random();

    @Override
    public ArrayList<IAction> createActions(boolean isInitial) {
        locScores = new HashMap<>();
        locScores.put(SETTLEMENT, new Double[getBoard().getLocations().size()]);

        updateLocationScores(SETTLEMENT);

        while (true) {
            ArrayList<CreateSettlement> actions_settlements = getActions_of(getPossibleActions(), CreateSettlement.class);
            ArrayList<CreateRoad> actions_roads = getActions_of(getPossibleActions(), CreateRoad.class);

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
                        bestLocation = getVirtualBoard().getLocations().get(i);
                }

                for (CreateSettlement createSettlement : actions_settlements) {
                    if (createSettlement.getLocation().equals(bestLocation)) {
                        doVirtually(createSettlement);
                        updateLocationScores(SETTLEMENT);
                        break;
                    }
                }

                actions_settlements = getActions_of(getPossibleActions(), CreateSettlement.class);
                actions_roads = getActions_of(getPossibleActions(), CreateRoad.class);
            }
            ///endregion

            updateLocationScores(SETTLEMENT);

            //region If a road can be built, build it else break the loop.
            _locScores = locScores.get(SETTLEMENT);

            for (Location location : getVirtualBoard().getLocations()) {
                if (location.getOwner() != null) {
                    _locScores[location.getIndex()] = Math.abs(_locScores[location.getIndex()]);
                    _locScores[location.getIndex()] *= -1;

                    for (Location adjacentLocation : location.getAdjacentLocations()) {
                        _locScores[adjacentLocation.getIndex()] = Math.abs(_locScores[adjacentLocation.getIndex()]);
                        _locScores[adjacentLocation.getIndex()] *= -1;
                    }
                }
            }

            if (actions_roads.size() > 0) {
                Location[] best3Locations = new Location[3];
                double[] best3Locations_score = new double[3];
                for (int i = 0; i < _locScores.length; i++) {
                    for (int j = 2; j >= 0; j--) {
                        if (_locScores[i] > 0 && (best3Locations[j] == null || _locScores[i] > best3Locations_score[j])) {
                            for (int k = best3Locations.length - 1; k >= j; k--) {
                                if (k == 2) {
                                    best3Locations[k] = null;
                                    best3Locations_score[k] = -Double.MAX_VALUE;
                                } else {
                                    best3Locations[k + 1] = best3Locations[k];
                                    best3Locations_score[k + 1] = best3Locations_score[k];
                                }
                            }

                            best3Locations[j] = virtualBoard.getLocations().get(i);
                            best3Locations_score[j] = _locScores[i];
                            break;
                        }
                    }
                }

                ArrayList<Location> actionRoad_locations = new ArrayList();
                for (CreateRoad createRoad : actions_roads) {
                    if (!actionRoad_locations.contains(createRoad.getLocations()[0]))
                        actionRoad_locations.add(createRoad.getLocations()[0]);

                    if (!actionRoad_locations.contains(createRoad.getLocations()[1]))
                        actionRoad_locations.add(createRoad.getLocations()[1]);
                }

                ArrayList<ArrayList<Road>> threeBestPaths = new ArrayList<>();
                ArrayList<Integer> threeBestPaths_discounted_len = new ArrayList<>();
                for (int i = 0; i < best3Locations.length; i++) {
                    if (best3Locations[i] != null) {
                        ArrayList<Road> bestPath = new ArrayList<>();
                        int bestPath_discounted_len = Integer.MAX_VALUE;

                        for (Location location : actionRoad_locations) {
                            if (location.getOwner() != null && location.getOwner().getIndex() == getOwner().getIndex()) {
                                ArrayList<Road> path = getShortestPath(location, best3Locations[i], false).getValue();

                                boolean ignorePath = true;
                                for (CreateRoad createRoad : actions_roads) {
                                    if (path.contains(createRoad.getRoad())) {
                                        ignorePath = false;
                                        break;
                                    }
                                }

                                if (ignorePath)
                                    continue;

                                int path_discounted_len = path.size();

                                for (Road _road : path) {
                                    path_discounted_len -= getOwner().getStructures().contains(_road) ? 1 : 0;
                                }

                                if (path_discounted_len < bestPath_discounted_len) {
                                    bestPath = path;
                                    bestPath_discounted_len = path_discounted_len;
                                }
                            }
                        }

                        threeBestPaths.add(bestPath);
                        threeBestPaths_discounted_len.add(bestPath_discounted_len);
                    } else {
                        threeBestPaths.add(null);
                        threeBestPaths_discounted_len.add(null);
                    }
                }

                /*for (int i = 0; i < threeBestPaths.size(); i++) {
                    System.out.println("    Path(" + i + "): " + threeBestPaths.get(i));
                    System.out.println("    Path(" + i + ") Discounted Length: " + threeBestPaths_discounted_len.get(i));
                }*/

                ArrayList<Double> threeBestPaths_scores = new ArrayList<>();

                if (threeBestPaths.get(0) != null && threeBestPaths.get(0).size() > 0) {
                    /*System.out.println(-threeBestPaths_discounted_len.get(0));
                    System.out.println(_locScores[threeBestPaths.get(0).get(threeBestPaths.get(0).size() - 1).getEndLocation().getIndex()]);*/

                    threeBestPaths_scores.add((double) -threeBestPaths_discounted_len.get(0)
                            + _locScores[threeBestPaths.get(0).get(threeBestPaths.get(0).size() - 1).getEndLocation().getIndex()]);
                } else
                    threeBestPaths_scores.add(-Double.MAX_VALUE);

                if (threeBestPaths.get(1) != null && threeBestPaths.get(1).size() > 0) {
                    /*System.out.println(-threeBestPaths_discounted_len.get(1));
                    System.out.println(_locScores[threeBestPaths.get(1).get(threeBestPaths.get(1).size() - 1).getEndLocation().getIndex()]);*/

                    threeBestPaths_scores.add((double) -threeBestPaths_discounted_len.get(1)
                            + _locScores[threeBestPaths.get(1).get(threeBestPaths.get(1).size() - 1).getEndLocation().getIndex()]);
                } else
                    threeBestPaths_scores.add(-Double.MAX_VALUE);

                if (threeBestPaths.get(2) != null && threeBestPaths.get(2).size() > 0) {
                    /*System.out.println(-threeBestPaths_discounted_len.get(2));
                    System.out.println(_locScores[threeBestPaths.get(2).get(threeBestPaths.get(2).size() - 1).getEndLocation().getIndex()]);*/

                    threeBestPaths_scores.add((double) -threeBestPaths_discounted_len.get(2)
                            + _locScores[threeBestPaths.get(2).get(threeBestPaths.get(2).size() - 1).getEndLocation().getIndex()]);
                } else
                    threeBestPaths_scores.add(-Double.MAX_VALUE);

                /*for (int i = 0; i < threeBestPaths.size(); i++)
                    System.out.println("    Path(" + i + ") Score: " + threeBestPaths_scores.get(i));*/

                ArrayList<Road> chosenPath = new ArrayList<>();
                Double chosenPath_score = -Double.MAX_VALUE;
                for (int i = 0; i < threeBestPaths.size(); i++) {
                    ArrayList<Road> path = threeBestPaths.get(i);

                    if (path != null && path.size() != 0 && threeBestPaths_scores.get(i) > chosenPath_score) {
                        chosenPath_score = threeBestPaths_scores.get(i);
                        chosenPath = path;
                    }
                }

                /*System.out.println("Ch. Path: " + chosenPath);
                System.out.println("Ch. Path Sc.: " + chosenPath_score);*/

                for (Road road : chosenPath) {
                    int[] locationIndexes = new int[2];
                    locationIndexes[0] = road.getStartLocation().getIndex();
                    locationIndexes[1] = road.getEndLocation().getIndex();

                    int index = actions_roads.indexOf(new CreateRoad(locationIndexes, getOwner().getIndex(), virtualBoard));
                    if (index != -1) {
                        doVirtually(actions_roads.get(index));
                        updateLocationScores(SETTLEMENT);
                        break;
                    }
                }

                break;
            } else
                break;
            ///endregion
        }

        ///region Do actions randomly.
        while (getPossibleActions().size() != 0) {
            IAction action = getPossibleActions().get(randomGenerator.nextInt(getPossibleActions().size()));
            doVirtually(action);
            updateLocationScores(SETTLEMENT);
        }
        ///endregion

        for (Player player : getVirtualBoard().getPlayers())
            System.out.println("   " + player + "'s Last Resource: " + player.getResource());

        return getActionsDone();
    }

    private void updateLocationScores(StructureType structureType) {
        Double[] _locScores = locScores.get(structureType);

        if (structureType == SETTLEMENT) {
            for (int i = 0; i < _locScores.length; i++) {
                Location location = getBoard().getLocations().get(i);

                _locScores[i] = 0.0;
                for (Land land : location.getAdjacentLands()) {
                    _locScores[i] += location.isActive() ? 0.01 : 0;
                    _locScores[i] += land.getDiceChance() * 36.0;
                    _locScores[i] += (land.getResourceType() == LUMBER ? 3.0 : 0.0) + (land.getResourceType() == BRICK ? 3.0 : 0.0);
                }
            }
        }
    }

    public <T> ArrayList<T> getActions_of(ArrayList<IAction> actions, Class T) {
        ArrayList<T> selectedActions = new ArrayList<>();

        for (IAction action : actions) {
            if (action.getClass() == T)
                selectedActions.add((T) action);
        }

        return selectedActions;
    }

    @Override
    public void updateBidRanking() {
        Resource desiredResource;
        Action desiredAction;
        clearBidRanking();

        Bid.setBestType(ResourceType.BRICK);

        for (int i = 0; i < Action.values().length; i++) {
            desiredResource = new Resource(getOwner().getResource());
            desiredAction = Action.values()[i];

            // Ignore the ineffective action
            if (desiredAction == Action.ZeroCost)
                continue;

            if (desiredAction == Action.CreateRoad) {
                desiredResource.disjoin(Road.COST);
                if (desiredResource.getSum() >= 0)         // checks if the desired Resource have potential
                    resourceAnalyze(desiredResource);
            } else if (desiredAction == Action.CreateSettlement) {
                desiredResource.disjoin(Settlement.COST);
                if (desiredResource.getSum() >= 0) {       // checks if the desired Resource have potential
                    resourceAnalyze(desiredResource);
                }
            } else if (desiredAction == Action.UpgradeSettlement) {
                desiredResource.disjoin(City.COST);
                if (desiredResource.getSum() >= 0)         // checks if the desired Resource have potential
                    resourceAnalyze(desiredResource);
            } else if (desiredAction == Action.DrawDevCard) {
                desiredResource.disjoin(DrawDevelopmentCard.COST);
                if (desiredResource.getSum() >= 0)         // checks if the desired Resource have potential
                    resourceAnalyze(desiredResource);
            }
        }

        if (getOwner().getState() == PlayerState.THINKING) {
            System.out.println("\n    " + getOwner() + "'s Negotiation Session has been started.");
            System.out.println("    " + getOwner() + "'s Bid Ranking:");
            for (Bid bid : getBidRanking())
                System.out.println("    " + bid);
        }
    }

    private void resourceAnalyze(Resource desiredResource) {
        Resource wantedResource = new Resource();
        Resource freeResource = new Resource(desiredResource);

        for (ResourceType type : freeResource.keySet()) {
            if (freeResource.get(type) < 0) {
                wantedResource.add(type, -freeResource.get(type));
                freeResource.replace(type, 0);
            }
        }

        for (ResourceType type : wantedResource.keySet()) {
            if (wantedResource.get(type) > 0) {
                createBids(type, wantedResource.get(type), freeResource);
            }
        }
    }

    private void createBids(ResourceType type, int need, Resource freeResource) {
        if (need > 1)
            createBids(type, need - 1, freeResource);

        Resource bid;
        for (ResourceType givenType : freeResource.keySet()) {
            if (freeResource.get(givenType) > 0) {
                bid = new Resource();
                bid.add(type, need);

                ArrayList<ResourceType> freeTypeList = new ArrayList<>();
                for (int i = 0; i < ResourceType.values().length; i++) {
                    if (ResourceType.values()[i] != type)
                        freeTypeList.add(ResourceType.values()[i]);
                }

                int[] max = new int[4];
                max[0] = freeTypeList.size() > 0 ? Math.min(freeResource.get(freeTypeList.get(0)), need + 2) : 0;
                max[1] = freeTypeList.size() > 1 ? Math.min(freeResource.get(freeTypeList.get(1)), need + 2) : 0;
                max[2] = freeTypeList.size() > 2 ? Math.min(freeResource.get(freeTypeList.get(2)), need + 2) : 0;
                max[3] = freeTypeList.size() > 3 ? Math.min(freeResource.get(freeTypeList.get(3)), need + 2) : 0;

                int[] givenCount = new int[4];
                for (givenCount[0] = max[0]; givenCount[0] >= 0; givenCount[0]--) {
                    for (givenCount[1] = Math.min(max[1], need + 2 - givenCount[0]); givenCount[1] >= 0; givenCount[1]--) {
                        for (givenCount[2] = Math.min(max[2], need + 2 - givenCount[0] - givenCount[1]); givenCount[2] >= 0; givenCount[2]--) {
                            for (givenCount[3] = Math.min(max[3], need + 2 - givenCount[0] - givenCount[1] - givenCount[2]); givenCount[3] >= 0; givenCount[3]--) {
                                Resource bidBefore = new Resource(bid);

                                for (int i = 0; i < freeTypeList.size() - 1; i++)
                                    bid.add(freeTypeList.get(i), -givenCount[i]);

                                // ?
                                // At least one resourceType must be negative.
                                boolean isValid = false;
                                for (ResourceType resourceType : ResourceType.values()) {
                                    if (bid.get(resourceType) < 0) {
                                        isValid = true;
                                        break;
                                    }
                                }

                                // ?
                                Bid addedBid = new Bid(bid);
                                if (isValid && !getBidRanking().contains(addedBid))
                                    addBidToBidRanking(addedBid);

                                bid = bidBefore;
                            }
                        }
                    }
                }
            }
        }
    }

    public AbstractMap.SimpleEntry<Integer, ArrayList<Road>> getShortestPath(Location locationSource, Location locationTarget, boolean ignoreBuildings) {
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

        for (Structure structure : virtualBoard.getStructures()) {
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
            resultRoads.add(new Road(virtualBoard.getLocations().get(minChain.get(i).getIndex()), virtualBoard.getLocations().get(minChain.get(i + 1).getIndex()), getOwner()));
        }

        return new AbstractMap.SimpleEntry<>(minDistance, resultRoads);
    }
}