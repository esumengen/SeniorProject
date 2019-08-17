package SeniorProject;

import SeniorProject.Actions.*;
import SeniorProject.Negotiation.Bid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BasicAI extends AI {
    private Random randomMachine = new Random();
    private HashMap<StructureType, Double[]> locScores;
    private HashMap<ResourceType, Double> resourceWeights = new HashMap<>();
    private ActionType resourceWeights_target;
    private int sequentialSkips = 0;
    private Player me;

    // Initialization of action categories
    private ArrayList<MoveRobber> actions_robber = new ArrayList<>();
    private ArrayList<CreateSettlement> actions_settlement = new ArrayList<>();
    private ArrayList<CreateRoad> actions_road = new ArrayList<>();
    private ArrayList<UpgradeSettlement> actions_upgrade = new ArrayList<>();
    private ArrayList<TradeWithBank> tradeBank_actions = new ArrayList<>();
    private ArrayList<DrawDevelopmentCard> actions_drawCard = new ArrayList<>();

    @Override
    public ArrayList<IAction> createActions(boolean isInitial) {
        initialization();

        /// region If robber must be moved, move the robber.
        if (actions_robber.size() > 0) {
            MoveRobber bestRobberAction = null;
            int bestRobberAction_score = Integer.MIN_VALUE;
            for (MoveRobber robberAction : actions_robber) {
                ArrayList<Building> allBuildings = getVirtualBoard().getLands().get(robberAction.getLandIndex()).getAllBuildings();
                ArrayList<Building> myBuildings = new ArrayList<>();
                for (Building building : allBuildings)
                    if (building.getPlayer().getIndex() == getOwner().getIndex())
                        myBuildings.add(building);

                int score = (int) (robberAction.getVictimIndex() != -1 ? Math.pow(getVirtualBoard().getPlayers().get(robberAction.getVictimIndex()).getVictoryPoint(), 2) : 0);
                score -= (int) Math.pow(myBuildings.size(), 2);
                score += (int) Math.pow(allBuildings.size() - myBuildings.size(), 2);

                if (score > bestRobberAction_score) {
                    bestRobberAction_score = score;
                    bestRobberAction = robberAction;
                }
            }

            doAction(bestRobberAction);
        }
        /// endregion

        if (tradeBank_actions.size() > 0) {
            //System.out.println("I can do TradeWithBank(s).");

            ArrayList<ArrayList<? extends IAction>> action_categories = new ArrayList<>();
            action_categories.add(actions_settlement);
            action_categories.add(actions_upgrade);
            action_categories.add(actions_drawCard);
            action_categories.add(actions_road);

            ArrayList<Resource> action_category_costs = new ArrayList<>();
            action_category_costs.add(CreateSettlement.COST);
            action_category_costs.add(UpgradeSettlement.COST);
            action_category_costs.add(DrawDevelopmentCard.COST);
            action_category_costs.add(CreateRoad.COST);

            ArrayList<ActionType> actions_category_enums = new ArrayList<>();
            actions_category_enums.add(ActionType.CreateSettlement);
            actions_category_enums.add(ActionType.UpgradeSettlement);
            actions_category_enums.add(ActionType.DrawDevCard);
            actions_category_enums.add(ActionType.CreateRoad);

            ArrayList<ArrayList<TradeWithBank>> allSolvingActions = new ArrayList<>();

            for (int i = 0; i < action_categories.size(); i++) {
                ArrayList actions_group = action_categories.get(i);
                Resource actions_cost = action_category_costs.get(i);

                if (actions_group.size() == 0) {
                    //System.out.println("I cannot do any " + actions_category_enums.get(i) + "(s).");

                    allSolvingActions.add(new ArrayList<>());

                    State.StateBuilder stateBuilder = new State.StateBuilder(getVirtualBoard());
                    State allAffordableState = stateBuilder.setResource(getOwner().getIndex(), new Resource(4, 4, 4, 4, 4)).build();
                    ArrayList<IAction> allAffordableState_possibleActions = allAffordableState.getPossibleActions(getOwner().getIndex());
                    int validActions_size = 0;
                    for (IAction _action : allAffordableState_possibleActions) {
                        if (_action.getClass().equals(action_categories.get(i).getClass())) ;
                        validActions_size++;
                    }

                    if (validActions_size > 0) {
                        Resource remainingResources = new Resource(getOwner().getResource());
                        remainingResources.disjoin(actions_cost);

                        if (-remainingResources.getNegatives().getSum() <= remainingResources.getPositives().getSum()) {
                            for (TradeWithBank tradeWithBank : tradeBank_actions) {
                                Resource missingResources_checkPoint = new Resource(remainingResources);

                                remainingResources.join(tradeWithBank.getTakenResources());
                                remainingResources.disjoin(tradeWithBank.getGivenResources());

                                if (remainingResources.getNegatives().getSum() == 0) {
                                    allSolvingActions.get(i).add(tradeWithBank);
                                    //System.out.println("I can fix this problem with " + tradeWithBank);
                                }

                                remainingResources = missingResources_checkPoint;
                            }
                        }
                    }
                } else {
                    allSolvingActions.add(new ArrayList<>());
                }
            }

            int affordableAction_count = 0;
            for (ActionType actionType : ActionType.values()) {
                if (Board.isAffordable(actionType, me.getResource())) // ?
                    affordableAction_count++;
            }

            double maxScore = Integer.MIN_VALUE;
            TradeWithBank maxScore_owner = null;
            for (int i = 0; i < allSolvingActions.size(); i++) {
                ArrayList<TradeWithBank> solving_tradeBanks = allSolvingActions.get(i);
                ActionType action = actions_category_enums.get(i);

                /*if (solving_tradeBanks.size() != 0)
                    System.out.println(action + " solver's scores: ");*/

                for (int j = 0; j < solving_tradeBanks.size(); j++) {
                    TradeWithBank tradeWithBank = solving_tradeBanks.get(j);

                    double score = 0;

                    score += (action == ActionType.CreateSettlement) ? 1 : 0;
                    score += (action == ActionType.UpgradeSettlement) ? 1 : 0;

                    for (ResourceType resourceType : ResourceType.values()) {
                        if (resourceWeights_target != action)
                            score -= tradeWithBank.getGivenResources().get(resourceType) * resourceWeights.get(resourceType);
                        else
                            score -= tradeWithBank.getGivenResources().get(resourceType) * 0.2;
                    }

                    for (int k = 0; k < allSolvingActions.size(); k++) {
                        if (actions_category_enums.get(k) == action)
                            score += 0.3;
                    }

                    int afterAffordableAction_count = 0;
                    Resource afterResource = new Resource(me.getResource()); // ?
                    afterResource.disjoin(tradeWithBank.getGivenResources());
                    afterResource.join(tradeWithBank.getTakenResources());

                    for (ActionType actionType : ActionType.values()) {
                        if (Board.isAffordable(actionType, afterResource))
                            afterAffordableAction_count++;
                    }

                    score -= (affordableAction_count - afterAffordableAction_count) * 0.3;

                    if (score > maxScore) {
                        maxScore = score;
                        maxScore_owner = tradeWithBank;
                    }

                    //System.out.println(tradeWithBank + " Score: " + score);
                }
            }

            if (maxScore_owner != null && maxScore > -1) {
                //System.out.println("I chose " + maxScore_owner);

                doAction(maxScore_owner);
            }
        }

        while (true) {
            boolean roadAdded = false;
            boolean settlementAdded = false;

            //region While a settlement can be built, build it.
            Double[] _locScores = locScores.get(SETTLEMENT);
            while (actions_settlement.size() > 0) {
                // Mark all of them as impossible
                for (int i = 0; i < _locScores.length; i++)
                    _locScores[i] *= -1;

                // Make the possible ones active again
                for (CreateSettlement createSettlement : actions_settlement)
                    _locScores[createSettlement.getLocation().getIndex()] *= -1;

                // Choose the location has the maximum score
                Location bestLocation = null;
                for (int i = 0; i < _locScores.length; i++) {
                    if (bestLocation == null || _locScores[i] > _locScores[bestLocation.getIndex()])
                        bestLocation = getVirtualBoard().getLocations().get(i);
                }

                // Create the settlement on this location
                for (CreateSettlement createSettlement : actions_settlement) {
                    if (createSettlement.getLocation().equals(bestLocation)) {
                        doAction(createSettlement);
                        settlementAdded = true;
                        break;
                    }
                }
            }
            ///endregion

            //region If a good road can be built, build it.
            _locScores = locScores.get(SETTLEMENT);
            if (actions_road.size() > 0) {
                // Mark the adjacent locations of settlements as impossible.
                for (Location location : getVirtualBoard().getLocations()) {
                    if (location.getOwner() != null) {
                        _locScores[location.getIndex()] *= -1;

                        for (Location adjacentLocation : location.getAdjacentLocations())
                            _locScores[adjacentLocation.getIndex()] *= -1;
                    }
                }

                // Find the best three locations.
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

                            best3Locations[j] = getVirtualBoard().getLocations().get(i);
                            best3Locations_score[j] = _locScores[i];
                            break;
                        }
                    }
                }

                // Set the reachable locations via possible roads
                ArrayList<Location> actionRoad_locations = new ArrayList<>();
                for (CreateRoad createRoad : actions_road) {
                    if (!actionRoad_locations.contains(createRoad.getLocations()[0]))
                        actionRoad_locations.add(createRoad.getLocations()[0]);

                    if (!actionRoad_locations.contains(createRoad.getLocations()[1]))
                        actionRoad_locations.add(createRoad.getLocations()[1]);
                }

                // Find the best three paths and their discounted lengths (according to the best three locations) using the shortest path.
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
                                for (CreateRoad createRoad : actions_road) {
                                    if (path.contains(createRoad.getRoad())) {
                                        ignorePath = false;
                                        break;
                                    }
                                }

                                if (ignorePath)
                                    continue;

                                int path_discounted_len = path.size();

                                for (Road _road : path)
                                    path_discounted_len -= getOwner().getStructures().contains(_road) ? 1 : 0;

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

                /// region DEBUG PRINT
                /*for (int i = 0; i < threeBestPaths.size(); i++) {
                    System.out.println("    Path(" + i + "): " + threeBestPaths.get(i));
                    System.out.println("    Path(" + i + ") Discounted Length: " + threeBestPaths_discounted_len.get(i));
                }*/
                /// endregion

                // Set the scores of the best three paths.
                ArrayList<Double> threeBestPaths_scores = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    if (threeBestPaths.get(i) != null && threeBestPaths.get(i).size() > 0) {
                        /// region DEBUG PRINT
                        /*System.out.println(-threeBestPaths_discounted_len.get(0));
                        System.out.println(_locScores[threeBestPaths.get(0).get(threeBestPaths.get(0).size() - 1).getEndLocation().getIndex()]);*/
                        /// endregion

                        threeBestPaths_scores.add((double) -threeBestPaths_discounted_len.get(i)
                                + _locScores[threeBestPaths.get(i).get(threeBestPaths.get(i).size() - 1).getEndLocation().getIndex()]);
                    } else
                        threeBestPaths_scores.add(-Double.MAX_VALUE);
                }

                /// region DEBUG PRINT
                /*for (int i = 0; i < threeBestPaths.size(); i++)
                    System.out.println("    Path(" + i + ") Score: " + threeBestPaths_scores.get(i));*/
                /// endregion

                // Choose the best path
                ArrayList<Road> chosenPath = new ArrayList<>();
                Double chosenPath_score = -Double.MAX_VALUE;
                for (int i = 0; i < threeBestPaths.size(); i++) {
                    ArrayList<Road> path = threeBestPaths.get(i);

                    if (path != null && path.size() != 0 && threeBestPaths_scores.get(i) > chosenPath_score) {
                        chosenPath_score = threeBestPaths_scores.get(i);
                        chosenPath = path;
                    }
                }

                /// region DEBUG PRINT
                /*System.out.println("Ch. Path: " + chosenPath);
                System.out.println("Ch. Path Sc.: " + chosenPath_score);*/
                /// endregion

                // Create roads in order to complete the best path.
                for (Road road : chosenPath) {
                    int[] locationIndexes = new int[2];
                    locationIndexes[0] = road.getStartLocation().getIndex();
                    locationIndexes[1] = road.getEndLocation().getIndex();

                    int index = actions_road.indexOf(new CreateRoad(locationIndexes, getOwner().getIndex(), getVirtualBoard()));
                    if (index != -1) {
                        doAction(actions_road.get(index));
                        roadAdded = true;

                        continue;
                    }
                }
            }
            ///endregion

            if (!roadAdded && !settlementAdded)
                break;
        }

        ///region Do UpgradeSettlement(s) randomly.
        while (actions_upgrade.size() > 0) {
            IAction action = actions_upgrade.get(randomMachine.nextInt(actions_upgrade.size()));
            doAction(action);
        }
        ///endregion

        if (actions_drawCard.size() > 0) {
            if (getVirtualBoard().getLastMaxKnight_owner() != null && getVirtualBoard().getLastMaxKnight_owner().getIndex() != me.getIndex() && getVirtualBoard().getKnights_max() - me.getKnights() <= 1
                    || randomMachine.nextInt(3) == 0 || getVirtualBoard().getLastMaxKnight_owner() == null && randomMachine.nextInt(4) == 0
                    || me.getVictoryPoint() + 1 >= Global.MAX_VICTORY_POINTS) {
                doAction(actions_drawCard.get(0));
            }
        }

        if (getActionsDone().size() == 0) {
            // If the AI has skipped too many turns,
            if (skippedTooMuch()) {
                System.out.println(getOwner() + "'s " + sequentialSkips + ". pass.");

                // Do actions that are not TradeWithBank(s) randomly.
                while (getPossibleActions().size() > tradeBank_actions.size()) {
                    IAction action = getPossibleActions().get(randomMachine.nextInt(getPossibleActions().size()));

                    if (action instanceof TradeWithBank)
                        continue;

                    doAction(action);
                }
            }

            sequentialSkips++;
        } else
            sequentialSkips = 0;

        return getActionsDone();
    }

    @Override
    public double calculateBidUtility(Bid bid) {
        double utility = 0;

        for (ResourceType resourceType : ResourceType.values())
            utility += resourceWeights.get(resourceType) * bid.getChange().get(resourceType);

        return utility;
    }

    @Override
    public void updateBidRanking() {
        updateResourceWeights(getBoard());
        //System.out.println("    " + getOwner() + "'s Resource Weights: " + resourceWeights);

        Resource afterResource;
        clearBidRanking();

        for (ActionType desiredAction : ActionType.values()) {
            afterResource = new Resource(getOwner().getResource());
            afterResource.disjoin(Global.getCost(desiredAction));

            if (afterResource.getPositives().getSum() > 0 && afterResource.getNegatives().getSum() < 0)
                resourceAnalyze(afterResource);
        }
    }

    private void resourceAnalyze(Resource afterResource) {
        Resource wantedResource = new Resource();
        Resource freeResource = new Resource(afterResource);

        for (ResourceType type : ResourceType.values()) {
            if (afterResource.get(type) < 0) {
                wantedResource.replace(type, -freeResource.get(type));
                freeResource.replace(type, 0);
            }
        }

        for (ResourceType type : wantedResource.keySet()) {
            if (wantedResource.get(type) > 0)
                createBids(type, wantedResource.get(type), freeResource);
        }
    }

    private void createBids(ResourceType type, int need, Resource freeResource) {
        if (need > 1)
            createBids(type, need - 1, freeResource);

        Resource change = new Resource();
        change.add(type, need);

        ArrayList<ResourceType> freeTypeList = new ArrayList<>();
        for (int i = 0; i < ResourceType.values().length; i++) {
            if (ResourceType.values()[i] != type)
                freeTypeList.add(ResourceType.values()[i]);
        }

        int[] max = new int[4];
        for (int i = 0; i < max.length; i++)
            max[i] = Math.min(freeResource.get(freeTypeList.get(i)), need + 2);

        int[] givenCount = new int[4];
        for (givenCount[0] = max[0]; givenCount[0] >= 0; givenCount[0]--) {
            for (givenCount[1] = Math.max(max[1] - givenCount[0], 0); givenCount[1] >= 0; givenCount[1]--) {
                for (givenCount[2] = Math.max(max[2] - givenCount[0] - givenCount[1], 0); givenCount[2] >= 0; givenCount[2]--) {
                    for (givenCount[3] = Math.max(max[3] - givenCount[0] - givenCount[1] - givenCount[2], 0); givenCount[3] >= 0; givenCount[3]--) {
                        Resource copyChange = new Resource(change);

                        if (givenCount[0] + givenCount[1] + givenCount[2] + givenCount[3] == 0)
                            continue;

                        for (int i = 0; i < 4; i++)
                            copyChange.put(freeTypeList.get(i), -givenCount[i]);

                        // ?
                        Bid addedBid = new Bid(copyChange);
                        if (!getBidRanking().contains(addedBid))
                            addBidToBidRanking(addedBid);
                    }
                }
            }
        }
    }

    private void initialization() {
        locScores = new HashMap<>();
        locScores.put(SETTLEMENT, new Double[getVirtualBoard().getLocations().size()]);

        me = getVirtualBoard().getPlayers().get(getOwner().getIndex());

        updateLocationScores_for(SETTLEMENT);
        updatePossibleActions();
        updateResourceWeights(getVirtualBoard()); // ?
    }

    private void doAction(IAction action) {
        doVirtually(action);

        updateLocationScores_for(SETTLEMENT);
        updatePossibleActions();
        updateResourceWeights(getVirtualBoard()); // ?
    }

    private void updateResourceWeights(Board board) {
        State.StateBuilder stateBuilder = new State.StateBuilder(Board.deepCopy(board));
        State state = stateBuilder.build();
        //ArrayList<IAction> possibleActions = state.getPossibleActions(getOwner().getIndex());

        /*int createSettlement_size = Global.getActions_of(possibleActions, CreateSettlement.class).size();
        int createRoad_size = Global.getActions_of(possibleActions, CreateRoad.class).size();
        int upgradeSettlement_size = Global.getActions_of(possibleActions, UpgradeSettlement.class).size();
        int drawDevelopmentCard_size = Global.getActions_of(possibleActions, DrawDevelopmentCard.class).size();*/

        for (ResourceType resourceType : ResourceType.values())
            resourceWeights.put(resourceType, 0.0);

        HashMap<ActionType, Integer> missingResources = new HashMap<>();

        int settlement_missing = 0;
        Resource missingResourceFor_settlement = new Resource(Settlement.COST);
        missingResourceFor_settlement.disjoin(getOwner().getResource());
        for (ResourceType resourceType : ResourceType.values()) {
            if ((resourceType.equals(ResourceType.BRICK) || resourceType.equals(ResourceType.LUMBER)
                    || resourceType.equals(ResourceType.WOOL) || resourceType.equals(ResourceType.GRAIN))
                    && missingResourceFor_settlement.get(resourceType) > 0)
                settlement_missing += missingResourceFor_settlement.get(resourceType);
        }
        missingResources.put(ActionType.CreateSettlement, settlement_missing);

        int road_missing = 0;
        Resource missingResourceFor_road = new Resource(Road.COST);
        missingResourceFor_road.disjoin(getOwner().getResource());
        for (ResourceType resourceType : ResourceType.values()) {
            if ((resourceType.equals(ResourceType.BRICK) || resourceType.equals(ResourceType.LUMBER)) && missingResourceFor_road.get(resourceType) > 0)
                road_missing -= missingResourceFor_road.get(resourceType);
        }
        missingResources.put(ActionType.CreateRoad, road_missing);

        int city_missing = 0;
        Resource missingResourceFor_city = new Resource(City.COST);
        missingResourceFor_city.disjoin(getOwner().getResource());
        for (ResourceType resourceType : ResourceType.values()) {
            if ((resourceType.equals(ResourceType.GRAIN) || resourceType.equals(ResourceType.ORE)) && missingResourceFor_city.get(resourceType) > 0)
                city_missing -= missingResourceFor_city.get(resourceType);
        }
        missingResources.put(ActionType.UpgradeSettlement, city_missing);

        int draw_missing = 0;
        Resource missingResourceFor_draw = new Resource(DrawDevelopmentCard.COST);
        missingResourceFor_draw.disjoin(getOwner().getResource());
        for (ResourceType resourceType : ResourceType.values()) {
            if ((resourceType.equals(ResourceType.ORE) || resourceType.equals(ResourceType.GRAIN) || resourceType.equals(ResourceType.WOOL)) && missingResourceFor_draw.get(resourceType) > 0)
                draw_missing -= missingResourceFor_draw.get(resourceType);
        }
        missingResources.put(ActionType.DrawDevCard, draw_missing);

        //System.out.print("    " + getOwner() + "'s Resource Weight Calculations:");

        ActionType chosenAction = null;
        int chosenAction_score = Integer.MIN_VALUE;
        for (ActionType actionType : missingResources.keySet()) {
            int score = -missingResources.get(actionType) * 10;
            score += actionType.equals(ActionType.CreateSettlement) ? 15 : 0;
            score -= missingResources.get(actionType) == 0 ? 75 : 0;

            if (actionType.equals(ActionType.CreateSettlement)) {
                if (state.getVictoryPoints(getOwner().getIndex()) == Global.MAX_VICTORY_POINTS - 1)
                    score += 50;
            } else if (actionType.equals(ActionType.UpgradeSettlement)) {
                if (state.getVictoryPoints(getOwner().getIndex()) == Global.MAX_VICTORY_POINTS - 1)
                    score += 50;
            } else if (actionType.equals(ActionType.DrawDevCard)) {
                if (state.getVictoryPoints(getOwner().getIndex()) == Global.MAX_VICTORY_POINTS - 1) {
                    score += 35;

                    int knightsMax = 0;
                    int myKnights = board.getPlayers().get(getOwner().getIndex()).getKnights();
                    int knightsMax_repeat = 0;
                    for (Player player : board.getPlayers()) {
                        int knights = player.getKnights();
                        if (knights > knightsMax) {
                            knightsMax = knights;
                            knightsMax_repeat = 1;
                        } else if (knights == knightsMax)
                            knightsMax_repeat++;
                    }

                    if (myKnights == knightsMax && knightsMax > 1)
                        score += (knightsMax_repeat - 1) * 30;
                }
            } else if (actionType.equals(ActionType.CreateRoad)) {
                int longestRoad_length = 0;
                int myLongestRoad = 0;
                int longestRoad_repeat = 0;
                for (Player player : board.getPlayers()) {
                    int length = state.getLongestRoad_lengths().get(player.getIndex());
                    if (length > longestRoad_length) {
                        longestRoad_length = length;
                        longestRoad_repeat = 1;
                    } else if (length == longestRoad_length)
                        longestRoad_repeat++;

                    if (player.getIndex() == getOwner().getIndex())
                        myLongestRoad = length;
                }

                if (myLongestRoad == longestRoad_length && longestRoad_length > 3)
                    score += (longestRoad_repeat - 1) * 60;
            }

            if (score > chosenAction_score) {
                chosenAction_score = score;
                chosenAction = actionType;
                resourceWeights_target = chosenAction;
            }

            //System.out.print("  " + actionType + "'s score: " + score);
        }

        if (resourceWeights_target == ActionType.CreateRoad) {
            resourceWeights_target = chosenAction;
            resourceWeights.put(BRICK, 0.3);
            resourceWeights.put(LUMBER, 0.3);
            resourceWeights.put(ORE, 0.08);
            resourceWeights.put(GRAIN, 0.16);
            resourceWeights.put(WOOL, 0.16);
        } else if (resourceWeights_target == ActionType.CreateSettlement) {
            resourceWeights.put(BRICK, 0.22);
            resourceWeights.put(LUMBER, 0.22);
            resourceWeights.put(ORE, 0.12);
            resourceWeights.put(GRAIN, 0.22);
            resourceWeights.put(WOOL, 0.22);
        } else if (resourceWeights_target == ActionType.UpgradeSettlement) {
            resourceWeights.put(BRICK, 0.25 / 3);
            resourceWeights.put(LUMBER, 0.25 / 3);
            resourceWeights.put(ORE, 0.3);
            resourceWeights.put(GRAIN, 0.45);
            resourceWeights.put(WOOL, 0.25 / 3);
        } else if (resourceWeights_target == ActionType.DrawDevCard) {
            resourceWeights.put(BRICK, 0.13 / 2);
            resourceWeights.put(LUMBER, 0.13 / 2);
            resourceWeights.put(ORE, 0.29);
            resourceWeights.put(GRAIN, 0.29);
            resourceWeights.put(WOOL, 0.29);
        }

        //System.out.println("   [" + getOwner() + " wants to " + chosenAction + "]");
    }

    private void updateLocationScores_for(StructureType structureType) {
        Double[] _locScores = locScores.get(structureType);
        HashMap<ResourceType, Double> _resourceWeights;

        // For the first turns
        if (getVirtualBoard().getTurn() < 5) {
            _resourceWeights = new HashMap<>();
            _resourceWeights.put(BRICK, 1.0 / 3);
            _resourceWeights.put(LUMBER, 1.0 / 3);
            _resourceWeights.put(ORE, 1.0 / 9);
            _resourceWeights.put(GRAIN, 1.0 / 9);
            _resourceWeights.put(WOOL, 1.0 / 9);
        } else
            _resourceWeights = resourceWeights;

        if (structureType == SETTLEMENT) {
            for (int i = 0; i < _locScores.length; i++) {
                Location location = getVirtualBoard().getLocations().get(i);

                _locScores[i] = 0.0;
                for (Land land : location.getAdjacentLands()) {
                    _locScores[i] += location.isActive() ? 0.01 : 0;
                    _locScores[i] += land.getDiceChance() * 36.0;

                    for (ResourceType resourceType : ResourceType.values())
                        _locScores[i] += land.getResourceType() == resourceType ? _resourceWeights.get(resourceType) * 10 : 0.0;
                }

                for (Road connectedRoads : location.getConnectedRoads()) {
                    if (connectedRoads.getPlayer().getIndex() != getOwner().getIndex()) {
                        _locScores[i] += getVirtualBoard().isValid(new Settlement(location, getOwner()), getVirtualBoard().isInitial()) ? 10 : 0;
                    }
                }
            }
        }
    }

    private void updatePossibleActions() {
        actions_robber = Global.getActions_of(getPossibleActions(), MoveRobber.class);
        actions_settlement = Global.getActions_of(getPossibleActions(), CreateSettlement.class);
        actions_road = Global.getActions_of(getPossibleActions(), CreateRoad.class);
        actions_upgrade = Global.getActions_of(getPossibleActions(), UpgradeSettlement.class);
        tradeBank_actions = Global.getActions_of(getPossibleActions(), TradeWithBank.class);
        actions_drawCard = Global.getActions_of(getPossibleActions(), DrawDevelopmentCard.class);
    }

    private boolean skippedTooMuch() {
        return sequentialSkips > 4;
    }
}