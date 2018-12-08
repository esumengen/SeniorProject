package SeniorProject;

class Global {
    static final OSType OS_TYPE = System.getProperty("os.name").toLowerCase().contains("windows") ? OSType.WINDOWS : OSType.MAC;
    static final String WORKING_PATH = OS_TYPE == OSType.WINDOWS ? System.getProperty("user.home") + "\\AppData\\Local\\Catan" : "/Users/emresumengen/Desktop/deneme";
    static final String ENVIRONMENT_FILE = "environment.ini";
    static final String ACTIONS_FILE = "actions.txt";
    static final String LOG_FILE = "log.txt";

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
}