import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.*;

/*
---First Priority Decisions
buildSettlement()
buildRoad()
upgradeSettlement()
pickCard()
---Second Priority Decisions
tradeNegotiation()
tradeBank()

* */

public class Main {

    static int sayac = 0;

    public static void main(String[] args) {

        Board board = new Board();

        Timer myTimer = new Timer();
        TimerTask gorev = new TimerTask() {

            @Override
            public void run() {
                String osType = System.getProperty("os.name").toLowerCase();
                try {
                    Wini ini ;
                    if(osType.contains("windows")){
                        ini = new Wini(new File(Board.path+"\\environment.ini"));
                    }else {
                        ini = new Wini(new File(Board.path + "/environment.ini"));
                    }

                    String isSynchronized_str;
                    boolean isSynchronized;

                    isSynchronized_str = ini.get("General", "isSynchronized", String.class);
                    isSynchronized_str = String.copyValueOf(isSynchronized_str.toCharArray(), 1, isSynchronized_str.length()-2);
                    isSynchronized = isSynchronized_str.equals("true");

                    if (!isSynchronized) {
                        ActionReader actionReader = new ActionReader(board);

                        ini.put("General", "isSynchronized", "\"true\"");
                        ini.store();
                    }

                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        myTimer.schedule(gorev,0,100);





    }


}

