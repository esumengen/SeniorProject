package SeniorProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

enum OSType {
    WINDOWS, MAC;

    @Override
    public String toString() {
        return super.toString();
    }
}

class Global {
    static final OSType OS_TYPE = System.getProperty("os.name").toLowerCase().contains("windows") ? OSType.WINDOWS : OSType.MAC;
    static final String WORKING_PATH = OS_TYPE == OSType.WINDOWS ? System.getProperty("user.home") + "\\AppData\\Local\\Catan" : "/Users/emresumengen/Desktop/deneme";
    static final String ENVIRONMENT_FILE = "environment.ini";
    static final String ACTIONS_FILE = "actions.txt";
    static final String LOG_FILE = "log.txt";
    static int MAINPLAYER;

    static String get_working_path(String filename) {
        String file_path = Global.WORKING_PATH;

        if (Global.OS_TYPE == OSType.WINDOWS)
            file_path += "\\";
        else
            file_path += "/";

        file_path += filename;

        return file_path;
    }

    static int fibonacci(int n) {
        if(n == 0 || n == 1)
            return 1;

        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    static String getRidOf_quotationMarks(String string) {
        return String.copyValueOf(string.toCharArray(), 1, string.length() - 2);
    }

    static void addLog(String info) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(Global.get_working_path(Global.LOG_FILE), true);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(info);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();

                if (fileWriter != null)
                    fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Added a new log.");
        }
    }
}