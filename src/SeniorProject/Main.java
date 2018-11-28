package SeniorProject;

import org.ini4j.Wini;

import java.util.*;
import java.io.File;

class Main {
    public static void main(String[] args) {

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

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        myTimer.schedule(task,0,100);
    }
}