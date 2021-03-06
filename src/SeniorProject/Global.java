package SeniorProject;

import SeniorProject.Actions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

enum OSType {
    WINDOWS, MAC;

    @Override
    public String toString() {
        return super.toString();
    }
}

public class Global {
    public static final int PLAYER_COUNT = 4;
    public static final boolean FULL_BOT_MODE = true;
    public static final OSType OS_TYPE = System.getProperty("os.name").toLowerCase().contains("windows") ? OSType.WINDOWS : OSType.MAC;
    public static final String WORKING_PATH = OS_TYPE == OSType.WINDOWS ? System.getProperty("user.home") + "\\AppData\\Local\\Catan" : "/Users/emresumengen/Desktop/deneme";
    public static final String ENVIRONMENT_FILE = "environment.ini";
    public static final String GAME_PLATFORM_ACTIONS = "actions_gamePlatform.txt";
    public static final String LOG_FILE = "log.txt";
    public static final String COMMUNICATION_FILE = "communication.ini";
    public static final String LONGEST_ROADS_FILE = "longest_roads.ini";
    public static final int MAX_VICTORY_POINTS = 8;

    public static String get_working_path(String filename) {
        String file_path = Global.WORKING_PATH;

        if (Global.OS_TYPE == OSType.WINDOWS)
            file_path += "\\";
        else
            file_path += "/";

        file_path += filename;

        return file_path;
    }

    public static String getRidOf_quotationMarks(String string) {
        return String.copyValueOf(string.toCharArray(), 1, string.length() - 2);
    }

    public static <T> ArrayList<T> getActions_of(ArrayList<IAction> actions, Class T) {
        ArrayList<T> selectedActions = new ArrayList<>();

        for (IAction action : actions) {
            if (action.getClass() == T)
                selectedActions.add((T) action);
        }

        return selectedActions;
    }

    public static Resource getCost(ActionType actionType) {
        switch (actionType) {
            case CreateRoad:
                return CreateRoad.COST;
            case CreateSettlement:
                return CreateSettlement.COST;
            case UpgradeSettlement:
                return UpgradeSettlement.COST;
            case DrawDevCard:
                return DrawDevelopmentCard.COST;
        }

        return new Resource(0, 0, 0, 0, 0);
    }

    public static void addLog(String text) {
        createTextFile(LOG_FILE, text);
    }

    public static void createTextFile(String filename, String text) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(Global.get_working_path(filename), true);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(text);
            bufferedWriter.newLine();
        } catch (IOException e) {
            new Message(e.getMessage() + " (Err: 5)");
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();

                if (fileWriter != null)
                    fileWriter.close();

            } catch (IOException e) {
                new Message(e.getMessage() + " (Err: 6)");
            }
        }
    }
}