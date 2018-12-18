package SeniorProject;

import org.ini4j.Wini;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

class Board {
    private static final int landCount_horizontal_max = 7;
    private static final int landCount_vertical = 7;
    private static final int landCount_horizontal_min = landCount_horizontal_max - (int) Math.floor(landCount_vertical / 2);
    private ArrayList<Land> lands = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Structure> structures = new ArrayList<>();
    private ArrayList<Player> players;
    private int locationCount = calculateLocationCount();
    private Land robbedLand;

    Board(ArrayList<Player> players) {
        this.players = players;

        //region Initialization of Lands&Locations
        int landIndex;

        // Create locations
        for (int i = 0; i < locationCount; i++) {
            Location location = new Location(i);
            locations.add(location);
        }

        // Create lands
        landIndex = 0;
        for (int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int) Math.floor(landCount_vertical / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = new Land(landIndex++);
                lands.add(land);
            }
        }

        // Bind lands&locations
        ArrayList<Integer> topLocationIndexes = new ArrayList<>();
        topLocationIndexes.add(1);

        landIndex = 0;
        for (int i = 0; i < landCount_vertical; i++) {
            int landCount_horizontal = landCount_horizontal_max - Math.abs(i - (int) Math.floor(landCount_vertical / 2));

            for (int j = 0; j < landCount_horizontal; j++) {
                Land land = lands.get(landIndex);
                int result = topLocationIndexes.get(0);

                if (landIndex != 0) {
                    result = topLocationIndexes.get(landIndex - 1) + 2;

                    if (j == 0) {
                        if (i == landCount_vertical / 2 + 1)
                            result += 2;
                        else if (i < landCount_vertical / 2 + 1)
                            result += 1;
                        else
                            result += 3;
                    }
                    topLocationIndexes.add(result);
                }

                makeAdjacent(locations.get(result - 1), locations.get(result));
                makeAdjacent(locations.get(result + 1), locations.get(result));
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));

                int oldResult = result;
                result += 2 * landCount_horizontal + 2 - ((landCount_horizontal == landCount_horizontal_max) ? 1 : 0);

                makeAdjacent(locations.get(result), locations.get(result-1));
                makeAdjacent(locations.get(result), locations.get(result+1));
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));

                makeAdjacent(locations.get(oldResult-1), locations.get(result-1));
                makeAdjacent(locations.get(oldResult+1), locations.get(result+1));

                landIndex++;
            }
        }
        //endregion

        load(lands);
    }

    private void makeAdjacent(Location location1, Location location2) {
        if (!location1.getAdjacentLocations().contains(location2))
            location1.addAdjacentLocations(location2);

        if (!location2.getAdjacentLocations().contains(location1))
            location2.addAdjacentLocations(location1);
    }

    private void load(ArrayList<Land> lands) {
        try {
            Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

            String type_str;
            String dice_str;
            int diceNo;

            for (Land land : lands) {
                type_str = ini.get("LandTypes", Integer.toString(land.getIndex()), String.class);
                type_str = Global.getRidOf_quotationMarks(type_str);

                dice_str = ini.get("Dice", Integer.toString(land.getIndex()), String.class);
                dice_str = Global.getRidOf_quotationMarks(dice_str);
                diceNo = Integer.parseInt(dice_str);

                land.setType(LandType.valueOf(type_str.toUpperCase(Locale.ENGLISH)));
                land.setDiceNo(diceNo);

                if (land.getType() == LandType.DESERT) robbedLand = land;
            }

            Global.addLog("SUCCESS: The game is loaded to the AI.");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            Global.addLog("ERROR: The game is not loaded to the AI.");
        }
    }

    private void bind(Land land, Location location) {
        land.getAdjacentLocations().add(location);
        location.getAdjacentLands().add(land);
        if(land.getType() != LandType.SEA)
            location.setActive(true);
    }

    void createSettlement(Player player, Location location) {
        Settlement settlement = new Settlement(location, player);
        structures.add(settlement);

        location.setOwner(player);

        Global.addLog("ACTION: A settlement has been added on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        syncPlayer(player);
    }

    void createRoad(Player player, Location location_first, Location location_second) {
        Road road = new Road(location_first, location_second, player);
        structures.add(road);

        Global.addLog("ACTION: A road has been added between [Location " + location_first.getIndex() + " and Location " + location_second.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        syncPlayer(player);
    }

    void upgradeSettlement(Player player, Location location) {
        for (Structure structure : location.getStructures()) {
            if (structure instanceof Settlement) {
                City city = new City(location, structure.getPlayer());
                structures.set(structures.indexOf(structure), city);
                break;
            }
        }

        Global.addLog("ACTION: A settlement has been upgraded on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        syncPlayer(player);
    }

    boolean isValid(Structure structure) {
        return isValid(structure, false);
    }

    boolean isValid(Structure structure, boolean isInitial) {
        boolean isValid = true;

        if(structure instanceof Road) {
            if (!((Road) structure).getStartLocation().isActive() || !((Road) structure).getEndLocation().isActive())
                return false;

            Road road = (Road) structure;
            Location end = road.getEndLocation();
            Location start = road.getStartLocation();
            if (end.isActive() && start.isActive() &&  start.getAdjacentLocations().contains(end)) {
                if (countStructures("Road", start, structure.getPlayer()) + countStructures("Road", end, structure.getPlayer()) > 0) {
                    return true;
                }
            }
        }
        else if (structure instanceof Settlement) {
            if (!((Settlement) structure).getLocation().isActive())
                return false;

            for (Location location:((Settlement) structure).getLocation().getAdjacentLocations()) {
                if (location.getOwner() != null) {
                    isValid = false;
                    break;
                }
            }

            if (((Settlement) structure).getLocation().getOwner() != null)
                isValid = false;

            if (!isInitial) {

            }
        }

        return isValid;
    }

    int countStructures(String type, Location location, Player player) {
        int count = 0;
        if(type.equals("Road")) {
            for (Structure structure: location.getStructures()) {
                if(structure.getPlayer() == player)
                    count++;
            }
        }
        return count;
    }

    void moveRobber(Player robber, Land land, Player victim, ResourceType resourceType) {
        robbedLand = land;
        stealRandomResource(robber, victim, resourceType);

        Global.addLog("ACTION: The robber has been moved to [Land " + land.getIndex() + "] by [Player " + (robber.getIndex() + 1) + "]");
    }

    private void stealRandomResource(Player robber, Player robbed, ResourceType randomType) {
        robbed.addResource(randomType, robbed.getResources().get(randomType) - 1);
        robber.addResource(randomType, 1);

        Global.addLog("ACTION: [Player " + (robbed.getIndex() + 1) + "] is robbed by [Player " + (robber.getIndex() + 1) + "]");
    }

    void tradeBank(int playerIndex, int wheat, int wood, int wool, int stone, int brick, int wheatB, int woodB, int woolB, int stoneB, int brickB) {
        if (((wheatB + woodB + woolB + stoneB + brickB) * 4) == wheat + wood + wool + stone + brick) {
            players.get(playerIndex).setGrain(players.get(playerIndex).getGrain() - wheat);
            players.get(playerIndex).setLumber(players.get(playerIndex).getLumber() - wood);
            players.get(playerIndex).setWool(players.get(playerIndex).getWool() - wool);
            players.get(playerIndex).setOre(players.get(playerIndex).getOre() - stone);
            players.get(playerIndex).setBrick(players.get(playerIndex).getBrick() - brick);
            Global.addLog("ACTION: A trade with bank has been done by [Player " + playerIndex + "]");
        } else {
            Global.addLog("ACTION: A trade with bank has been failed by [Player " + playerIndex + "]");
        }
    }

    void tradePlayer(int playerIndex1, int playerIndex2, int wheat, int wood, int wool, int stone, int brick, int wheatB, int woodB, int woolB, int stoneB, int brickB) {
        Global.addLog("TODOACTION: A trade with [Player " + playerIndex2 + 1 + " has been done by [Player " + (playerIndex1 + 1) + "]");
        // TODO: 30-Nov-18
    }

    void rollDice(Player player, int dice1, int dice2) {
        generateResource(dice1 + dice2);
        Global.addLog("ACTION: Dice are rolled " + dice1 + " " + dice2 + " by [Player " + (player.getIndex() + 1) + "]");
    }

    private void generateResource(int diceNo) {
        for (Land land : lands) {
            if (land.getDiceNo() == diceNo && land != robbedLand) {
                for (Location location : land.getAdjacentLocations()) {
                    if (location.hasOwner()) {
                        Player rewardedPlayer = location.getOwner();

                        rewardedPlayer.addResource(land.getResourceType(), location.hasCity() ? 2 : 0);
                    }
                }
            }
        }
        Global.addLog("New resources are generated.");
    }

    void syncPlayer(Player player) {
        player.getStructures().clear();

        // Structure assignment
        for (Structure structure : structures) {
            if (player == structure.getPlayer()) {
                player.getStructures().add(structure);
            }
        }
    }

    private static int calculateLocationCount() {
        int sum = 0;

        for (int i = landCount_horizontal_max; i >= landCount_horizontal_min; i--) {
            sum += 2 * (2 * i + 1);
        }
        return sum;
    }

    ArrayList<Land> getLands() {
        return lands;
    }

    ArrayList<Location> getLocations() {
        return locations;
    }

    ArrayList<Player> getPlayers() {
        return players;
    }

    ArrayList<Structure> getStructures() {
        return structures;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < lands.size(); i++) {
            string += "[LAND " + lands.get(i).getIndex() + "]\nScore: " + lands.get(i).getDiceChance() + "\nType: " + lands.get(i).getType().toString() + "\nLocations: {";
            int j_max = lands.get(i).getAdjacentLocations().size() - 1;
            for (int j = 0; j < j_max + 1; j++) {
                string += lands.get(i).getAdjacentLocations().get(j).getIndex() + (j == j_max ? "" : ", ");
            }
            string += "}\n\n";
        }

        for (Player player : players) {
            string += "[PLAYER " + player.getIndex() + "]";
        }
        return string;
    }

    public Land getRobbedLand() {
        return robbedLand;
    }
}