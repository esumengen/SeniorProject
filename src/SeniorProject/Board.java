package SeniorProject;

import DevelopmentCards.*;
import org.apache.commons.lang3.SerializationUtils;
import org.ini4j.Wini;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class Board implements Serializable {
    private static final int landCount_horizontal_max = 7;
    private static final int landCount_vertical = 7;
    private static final int landCount_horizontal_min = landCount_horizontal_max - (int) Math.floor(landCount_vertical / 2);
    private ArrayList<Land> lands = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Structure> structures = new ArrayList<>();
    private ArrayList<Player> players;
    private Deck deck;
    private int locationCount = calculateLocationCount();
    private Land robbedLand;
    private boolean isActive;

    public Board(ArrayList<Player> players) {
        this.isActive = false;

        this.players = players;
        this.deck = new Deck();

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

                makeAdjacent(locations.get(result), locations.get(result - 1));
                makeAdjacent(locations.get(result), locations.get(result + 1));
                bind(land, locations.get(result - 1));
                bind(land, locations.get(result));
                bind(land, locations.get(result + 1));

                makeAdjacent(locations.get(oldResult - 1), locations.get(result - 1));
                makeAdjacent(locations.get(oldResult + 1), locations.get(result + 1));

                landIndex++;
            }
        }
        //endregion

        load(lands);

        //region Mark the corner locations
        for (Land land : lands) {
            if (land.getType() == LandType.SEA) {
                for (Location location:land.getAdjacentLocations()) {
                    location.makeCorner();
                }
            }
        }
        //endregion
    }

    private static int calculateLocationCount() {
        int sum = 0;

        for (int i = landCount_horizontal_max; i >= landCount_horizontal_min; i--) {
            sum += 2 * (2 * i + 1);
        }
        return sum;
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
            new Message(e.getMessage() + " - 7");

            Global.addLog("ERROR: The game is not loaded to the AI.");
        }
    }

    private void bind(Land land, Location location) {
        land.getAdjacentLocations().add(location);
        location.getAdjacentLands().add(land);
        if (land.getType() != LandType.SEA)
            location.setActive(true);
    }

    public void createSettlement(Player player, Location location) {
        int settlementCount = countStructures(StructureType.SETTLEMENT, player);

        Settlement settlement = new Settlement(location, player);
        structures.add(settlement);

        location.setOwner(player);
        location.addStructures(settlement);

        addLog("ACTION: A settlement has been added on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        // Immediate Harvesting
        if (settlementCount == 1) {
            for (Land land : location.getAdjacentLands())
                player.changeResource(land.getResourceType(), 1);
        }

        syncPlayer(player);
    }

    public static boolean isAffordable(BasicAI.MoveType type, Player player) {
        int brick = player.getResources().get(ResourceType.BRICK);
        int grain = player.getResources().get(ResourceType.GRAIN);
        int wool = player.getResources().get(ResourceType.WOOL);
        int ore = player.getResources().get(ResourceType.ORE);
        int lumber = player.getResources().get(ResourceType.LUMBER);

        switch (type) {
            case CreateSettlement:
                if (brick >= 1 && grain >= 1 && wool >= 1 && lumber >= 1)
                    return true;
                break;
            case CreateRoad:
                if (brick >= 1 && lumber >= 1)
                    return true;
                break;
            case UpgradeSettlement:
                if (ore >= 3 && grain >= 2)
                    return true;
                break;
            case DevelopmentCard:
                if (grain >= 1 && wool >= 1 && ore >= 1)
                    return true;
                break;
            case KnightCard:
                if (player.getKnight() > 0)
                    return true;
                break;
            case Trade:
                return true;
            case MonopolyCard:
                for (DevelopmentCard card : player.getDevelopmentCards()) {
                    if (card instanceof Monopoly) {
                        return true;
                    }
                }
                break;
            case RoadBuildingCard: {
                for (DevelopmentCard card : player.getDevelopmentCards()) {
                    if (card instanceof RoadBuilding) {
                        return true;
                    }
                }
                break;
            }
            case YearOfPlentyCard: {
                for (DevelopmentCard card : player.getDevelopmentCards()) {
                    if (card instanceof YearOfPlenty) {
                        return true;
                    }
                }
                break;
            }
        }

        return false;
    }

    void createRoad(Player player, Location location_first, Location location_second) {
        Road road = new Road(location_first, location_second, player);
        location_first.addConnectedRoad(road);
        location_second.addConnectedRoad(road);

        structures.add(road);

        location_first.addStructures(road);
        location_second.addStructures(road);

        addLog("ACTION: A road has been added between [Location " + location_first.getIndex() + " and Location " + location_second.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        syncPlayer(player);
    }

    void upgradeSettlement(Player player, Location location) {
        for (Structure structure : location.getStructures()) {
            if (structure instanceof Settlement) {
                City city = new City(location, structure.getPlayer());
                structures.set(structures.indexOf(structure), city);
                location.getStructures().set(location.getStructures().indexOf(structure), city);
                break;
            }
        }

        addLog("ACTION: A settlement has been upgraded on [Location " + location.getIndex() + "] by [Player " + (player.getIndex() + 1) + "]");

        syncPlayer(player);
    }

    boolean isValid(Structure structure) {
        return isValid(structure, false);
    }

    boolean isValid(Structure structure, boolean isInitial) {
        if (structure instanceof Road) {
            Road road = (Road) structure;
            Location end = road.getEndLocation();
            Location start = road.getStartLocation();
            boolean isBothLocationsActive = start.isActive() && end.isActive();

            // İki ucu aktif olmak zorunda.
            if (!isBothLocationsActive)
                return false;

            // İki ucu komşu olmak zorunda.
            if (!start.getAdjacentLocations().contains(end))
                return false;

            // Aynı yoldan başka olmamalı.
            if (roadExits(road))
                return false;

            // Herhangi bir ucunda rakip bina olmamalı. (?)
            if ((start.hasOwner() && start.getOwner().getIndex() != road.getPlayer().getIndex())
                    || (end.hasOwner() && end.getOwner().getIndex() != road.getPlayer().getIndex()))
                return false;

            // İki ucunda en az bir tane kendi yapısı olmalı.
            if (countStructures(StructureType.ROAD, start, road.getPlayer()) + countStructures(StructureType.ROAD, end, road.getPlayer())
                    + countStructures(StructureType.SETTLEMENT, start, road.getPlayer()) + countStructures(StructureType.SETTLEMENT, end, road.getPlayer()) == 0)
                return false;
        } else if (structure instanceof Building) {
            Building settlement = (Building) structure;

            // Bu location aktif olmalı.
            if (!settlement.getLocation().isActive())
                return false;

            // Etrafında bina olmamalı.
            if (!isAdjacentFree(settlement.getLocation()))
                return false;

            // Başlangıç durumu değilse
            if (!isInitial) {
                // Tam o location'a bağlı en az bir yolu bulunmalı.
                if (countStructures(StructureType.ROAD, settlement.getLocation(), settlement.getPlayer()) == 0)
                    return false;
            }
            else {
                // Köşede olmamalı.
                if (((Building) structure).getLocation().isCorner())
                    return false;
            }
        }

        return true;
    }

    boolean roadExits(Road newRoad) {
        for (Structure structure : structures) {
            if (structure.getType() == StructureType.ROAD) {
                Road road = (Road) structure;

                if ((road.getStartLocation() == newRoad.getStartLocation() && (road.getEndLocation() == newRoad.getEndLocation()))
                        || ((road.getStartLocation() == newRoad.getEndLocation()) && (road.getEndLocation() == newRoad.getStartLocation()))) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean isAdjacentFree(Location location) {
        /*for (Player player:players) {
            for (Structure structure:player.getStructures()) {
                if (structure instanceof Building)
                    if (((Building) structure).getLocation().getIndex() == location.getIndex())
                        return false;
            }
        }*/
        if (location.hasOwner()) {
            return false;
        }

        for (Location adjacentLocation : location.getAdjacentLocations()) {
            if (adjacentLocation.hasOwner())
                return false;
        }

        return true;
    }

    int countStructures(StructureType type, Player player) {
        return countStructures(type, null, player);
    }

    int countStructures(StructureType type, Location location) {
        return countStructures(type, location, null);
    }

    int countStructures(StructureType type, Location location, Player player) {
        int count = 0;

        if (location == null) {
            for (Structure structure:player.getStructures()) {
                count += structure.getType() == type ? 1 : 0;
            }
        }
        else {
            if (type == StructureType.ROAD) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof Road && (player == null || structure.getPlayer() == player))
                        count++;
                }
            } else if (type == StructureType.SETTLEMENT) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof Settlement && (player == null || structure.getPlayer() == player))
                        count++;
                }
            } else if (type == StructureType.CITY) {
                for (Structure structure : location.getStructures()) {
                    if (structure instanceof City && (player == null || structure.getPlayer() == player))
                        count++;
                }
            }
        }

        return count;
    }

    void moveRobber(Player robber, Land land, Player victim, ResourceType resourceType) {
        robbedLand = land;
        stealRandomResource(robber, victim, resourceType);

        addLog("ACTION: The robber has been moved to [Land " + land.getIndex() + "] by [Player " + (robber.getIndex() + 1) + "]");
    }

    private void stealRandomResource(Player robber, Player robbed, ResourceType randomType) {
        robbed.changeResource(randomType, robbed.getResources().get(randomType) - 1);
        robber.changeResource(randomType, 1);

        addLog("ACTION: [Player " + (robbed.getIndex() + 1) + "] is robbed by [Player " + (robber.getIndex() + 1) + "]");
    }

    void tradeBank(int playerIndex, int wheat, int wood, int wool, int stone, int brick, int wheatB, int woodB, int woolB, int stoneB, int brickB) {
        if (((wheatB + woodB + woolB + stoneB + brickB) * 4) == wheat + wood + wool + stone + brick) {
            players.get(playerIndex).setGrain(players.get(playerIndex).getGrain() - wheat);
            players.get(playerIndex).setLumber(players.get(playerIndex).getLumber() - wood);
            players.get(playerIndex).setWool(players.get(playerIndex).getWool() - wool);
            players.get(playerIndex).setOre(players.get(playerIndex).getOre() - stone);
            players.get(playerIndex).setBrick(players.get(playerIndex).getBrick() - brick);
            addLog("ACTION: A trade with bank has been done by [Player " + playerIndex + "]");
        } else {
            addLog("ACTION: A trade with bank has been failed by [Player " + playerIndex + "]");
        }
    }

    void tradePlayer(int playerIndex1, int playerIndex2, int wheat, int wood, int wool, int stone, int brick, int wheatB, int woodB, int woolB, int stoneB, int brickB) {
        addLog("TODOACTION: A trade with [Player " + playerIndex2 + 1 + " has been done by [Player " + (playerIndex1 + 1) + "]");
        // TODO: 30-Nov-18
    }

    void rollDice(Player player, int dice1, int dice2) {
        generateResource(dice1 + dice2);
        addLog("ACTION: Dice are rolled " + dice1 + " " + dice2 + " by [Player " + (player.getIndex() + 1) + "]");
    }

    private void generateResource(int diceNo) {
        for (Land land : lands) {
            if (land.getDiceNo() == diceNo && land != robbedLand) {
                for (Location location : land.getAdjacentLocations()) {
                    if (location.hasOwner()) {
                        Player rewardedPlayer = location.getOwner();

                        rewardedPlayer.changeResource(land.getResourceType(), location.hasCity() ? 2 : 1);
                    }
                }
            }
        }

        addLog("New resources are generated.");
    }

    void addLog(String log) {
        if (isActive)
            Global.addLog(log);
    }

    public static Board deepCopy(Serializable object) {
        Board copy = (Board) SerializationUtils.clone(object);
        return copy;

        /*try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);

            outputStrm.writeObject(object);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);

            return (Board) objInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public ArrayList<Land> getLands() {
        return lands;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Structure> getStructures() {
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