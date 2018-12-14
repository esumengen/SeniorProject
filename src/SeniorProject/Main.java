package SeniorProject;

import org.ini4j.Wini;

import java.util.*;
import java.io.File;
import java.io.InputStream;

class Main {


    public static void main(String[] args) {
        String a = "working";
        System.out.println(a == "false");

        try {
            Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

            String mainPlayer_str = ini.get("General", "MainPlayer", String.class);
            mainPlayer_str = Global.getRidOf_quotationMarks(mainPlayer_str);

            Global.MAINPLAYER = Integer.parseInt(mainPlayer_str);
            new Message(mainPlayer_str);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        Board board = new Board();
        Synchronizer synchronizer = new Synchronizer(board);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                try {

                    if (!synchronizer.isSynchronized() && !synchronizer.isWorking) {
                        synchronizer.isWorking = true;
                        File actionFile = new File(Global.get_working_path(Global.ACTIONS_FILE));
                        if(actionFile.exists()){
                            synchronizer.sync(actionFile);
                        }

                        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
                        Process process = processBuilder.start();
                        String tasksList = Stream_toString(process.getInputStream());

                        if (!tasksList.contains("Catan.exe")) {
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                }

                try {

                }
                catch (Exception e) {
                }
            }
        };
        timer.schedule(task, 0, 250);

    }
    private static String Stream_toString (InputStream inputStream){
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}