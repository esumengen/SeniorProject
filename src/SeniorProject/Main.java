package SeniorProject;

import org.ini4j.Wini;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        ArrayList<Player> players = createPlayers();

        Board board = new Board(players);
        board.setActive(true);
        board.setMain(true);

        for (Player player : players) {
            player.setPureBoard(board);
            player.createAI();
        }

        Synchronizer synchronizer = new Synchronizer(board);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!synchronizer.isSynchronized() && synchronizer.getState() == SynchronizerState.WAITING) {
                    File actionsFile = new File(Global.get_working_path(Global.ACTIONS_FILE));

                    if (actionsFile.exists()) {
                        synchronizer.sync(actionsFile);
                    }
                }

                File communication_file = new File(Global.get_working_path(Global.COMMUNICATION_FILE));
                if (communication_file.exists()) {

                    Wini communication_ini = null;
                    try {
                        communication_ini = new Wini(communication_file);
                    }
                    catch (Exception e) {
                        new Message(e.getMessage() + " - 12");
                    }

                    boolean isPlayed = false;
                    Player playerMoved = null;

                    for (Player player : players) {
                        boolean isInitial = true;

                        if (player.getType() != PlayerType.HUMAN) {
                            String turnMode = communication_ini.get("General", "turnMode[" + player.getIndex() + "]", String.class);
                            isInitial = communication_ini.get("Game State", "isInitial", String.class).equals("\"true\"");
                            turnMode = Global.getRidOf_quotationMarks(turnMode);

                            if (turnMode.equals("waiting") && player.getState() != PlayerState.THINKING) {
                                player.setState(PlayerState.THINKING);

                                player.writeMove(isInitial);
                                isPlayed = true;
                                playerMoved = player;

                                communication_ini.put("General", "turnMode[" + player.getIndex() + "]", "\"done\"");

                                try {
                                    communication_ini.store();
                                } catch (Exception e) {
                                    new Message(e.getMessage() + " - 11");
                                }

                                //break;
                            }
                        }

                        if (isPlayed) {
                            if (playerMoved.getIndex() == Global.PLAYER_COUNT - 1 && !isInitial)
                                board.setTurn(board.getTurn() + 1);

                            break;
                        }
                    }
                }

                ///region Automatic Termination
                ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
                Process process = null;
                try {
                    process = processBuilder.start();
                } catch (Exception e) {
                    new Message(e.getMessage() + " - 13");
                }
                String tasksList = stream_toString(process.getInputStream());

                if (!tasksList.contains("Catan.exe") && !tasksList.contains("Runner.exe")) {
                    System.exit(0);
                }
                ///endregion
            }
        };

        timer.schedule(task, 0, 333);

        // Debug Frame
        //DebugFrame debugFrame = new DebugFrame(board);
        //debugFrame.setVisible(true);
    }

    public static ArrayList<Player> createPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < Global.PLAYER_COUNT; i++) {
            Player player = new Player(i);

            if (i == 0) player.setType(PlayerType.HUMAN);
            else player.setType(PlayerType.AI);

            players.add(player);
        }
        return players;
    }

    private static String stream_toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}