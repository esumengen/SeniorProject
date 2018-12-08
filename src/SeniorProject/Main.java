package SeniorProject;

import org.ini4j.Wini;

import java.io.InputStream;
import java.util.*;
import java.io.File;

class Main {
    public static void main(String[] args) {
        //System.out.println(Global.fibonacci(3));

        Board board = new Board();
        Timer myTimer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

                    String isSynchronized_str = ini.get("General", "isSynchronized", String.class);
                    isSynchronized_str = Global.getRidOf_quotationMarks(isSynchronized_str);

                    boolean isSynchronized = isSynchronized_str.equals("true");

                    if (!isSynchronized) {
                        Synchronizer Synchronizer = new Synchronizer(board);

                        ini.put("General", "isSynchronized", "\"true\"");
                        ini.store();
                    }

                    ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
                    Process process = processBuilder.start();
                    String tasksList = Stream_toString(process.getInputStream());

                    if (!tasksList.contains("Catan.exe")) {
                        System.exit(0);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        };

        myTimer.schedule(task,0,100);
    }

    private static String Stream_toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}