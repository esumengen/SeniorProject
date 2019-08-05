package SeniorProject;

import SeniorProject.Negotiation.BasicNegotiationAgent;
import SeniorProject.Negotiation.NegotiationAgent;
import SeniorProject.Negotiation.NegotiationSession;
import SeniorProject.Negotiation.Negotiator;
import org.ini4j.Wini;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static ArrayList<Player> players;
    public static Board board; // public ?
    static Synchronizer synchronizer;

    static File actionsFile = new File(Global.get_working_path(Global.ACTIONS_FILE));
    static File communication_file = new File(Global.get_working_path(Global.COMMUNICATION_FILE));
    static Timer timer = new Timer();

    public static void main(String[] args) {
        initialization();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!synchronizer.isSynchronized() && synchronizer.getState() == SynchronizerState.WAITING) {
                    if (actionsFile.exists())
                        synchronizer.sync(actionsFile);
                }

                if (communication_file.exists()) {
                    Wini communication_ini = null;
                    try {
                        communication_ini = new Wini(communication_file);
                    } catch (Exception e) {
                        new Message(e.getMessage() + " - 12");
                    }

                    boolean isPlayed = false;
                    Player player_lastMoved = null;

                    for (Player player : players) {
                        boolean isInitial = true;

                        if (player.getType() != PlayerType.HUMAN) {
                            String turnMode = communication_ini.get("General", "turnMode[" + player.getIndex() + "]", String.class);
                            isInitial = communication_ini.get("Game State", "isInitial", String.class).equals("\"true\"");
                            turnMode = Global.getRidOf_quotationMarks(turnMode);

                            board.setInitial(isInitial);

                            if (turnMode.equals("waiting") && player.getState() != PlayerState.THINKING) {
                                player.setState(PlayerState.THINKING);

                                if (!isInitial)
                                    negotiationPhase(player, board);

                                player.writeMove(isInitial);

                                isPlayed = true;
                                player_lastMoved = player;

                                communication_ini.put("General", "turnMode[" + player.getIndex() + "]", "\"done\"");

                                try {
                                    communication_ini.store();
                                } catch (Exception e) {
                                    new Message(e.getMessage() + " - 11");
                                }
                            }
                        }

                        // May be problematic.
                        // This part is agent side controlled turn management.
                        if (isPlayed) {
                            if (player_lastMoved.getIndex() == Global.PLAYER_COUNT - 1 && !isInitial
                                    || player_lastMoved.getIndex() == 0 && board.countStructures(StructureType.SETTLEMENT, Global.PLAYER_COUNT - 1) > 0 && isInitial
                                    || player_lastMoved.getIndex() == Global.PLAYER_COUNT - 1 && board.countStructures(StructureType.SETTLEMENT, Global.PLAYER_COUNT - 1) == 0 && isInitial)
                                board.setTurn(board.getTurn() + 1);

                            break;
                        }
                    }
                }

                programTermination();
            }
        };

        timer.schedule(task, 0, 1);
    }

    private static void negotiationPhase(Player player, Board board) {
        ArrayList<NegotiationAgent> otherAgents = new ArrayList<>();

        for (Player _player : board.getPlayers()) {
            if (_player.getType() == PlayerType.HUMAN)
                continue;

            _player.getAI().updateBidRanking();

            if (_player.getIndex() != player.getIndex()) {
                otherAgents.add(_player.getNegotiationAgent());
            }
        }

        if (player.getAI().getBidRanking().size() > 0) {
            NegotiationSession session = new NegotiationSession(player.getNegotiationAgent(), otherAgents, player.getAI().getBidRanking());
            Negotiator.getInstance().startSession(session);
            if (session.isCompleted()) {

            }
        }
    }

    private static void initialization() {
        players = createPlayers();

        board = new Board(players);
        board.setActive(true);
        board.setMain(true);

        String[] negotiationDirectories = new String[Global.PLAYER_COUNT];
        String[] AIDirectories = new String[Global.PLAYER_COUNT];

        for (Player player : players) {
            if (player.getType() == PlayerType.HUMAN)
                continue;

            try {
                Wini ini = new Wini(new File(Global.get_working_path(Global.ENVIRONMENT_FILE)));

                String negotiationDirectory = ini.get("NegotiationSetup", Integer.toString(player.getIndex()), String.class);
                negotiationDirectory = Global.getRidOf_quotationMarks(negotiationDirectory);

                String AIDirectory = ini.get("AISetup", Integer.toString(player.getIndex()), String.class);
                AIDirectory = Global.getRidOf_quotationMarks(AIDirectory);

                negotiationDirectories[player.getIndex()] = negotiationDirectory;
                AIDirectories[player.getIndex()] = AIDirectory;
            } catch (Exception e) {
                new Message(e.getMessage() + " - 23");
            }
        }

        for (Player player : players) {
            if (player.getType() == PlayerType.HUMAN)
                continue;

            player.setPureBoard(board);

            NegotiationAgent negotiationAgent = null;
            AI AI_instance = null;

            if (negotiationDirectories[player.getIndex()].equals("Default")) {
                negotiationAgent = new BasicNegotiationAgent();

                negotiationAgent.setOwner(player);
            }

            if (AIDirectories[player.getIndex()].equals("Default")) {
                AI_instance = new BasicAI();

                AI_instance.setBoard(board);
                AI_instance.setOwner(player);
            }

            try {
                Class clss, clss2;
                Constructor constructor, constructor2;

                if (!negotiationDirectories[player.getIndex()].equals("Default")) {
                    System.out.println("!= Default NEG" + negotiationDirectories[player.getIndex()]);
                    File file = new File(negotiationDirectories[player.getIndex()]);
                    String name = file.getName().substring(0, file.getName().indexOf("."));

                    clss = new URLClassLoader(new URL[]{file.getParentFile().toURI().toURL()})
                            .loadClass(name);
                    constructor = clss.getDeclaredConstructor();

                    negotiationAgent = (NegotiationAgent) constructor.newInstance();
                    System.out.println(player.getIndex());
                    negotiationAgent.setOwner(player);
                }

                if (!AIDirectories[player.getIndex()].equals("Default")) {
                    System.out.println("!= Default AI" + AIDirectories[player.getIndex()]);
                    File file = new File(AIDirectories[player.getIndex()]);
                    String name = file.getName().substring(0, file.getName().indexOf("."));

                    clss2 = new URLClassLoader(new URL[]{file.getParentFile().toURI().toURL()})
                            .loadClass(name);
                    constructor2 = clss2.getDeclaredConstructor();

                    AI_instance = (AI) constructor2.newInstance();
                    AI_instance.setBoard(board);
                    AI_instance.setOwner(player);
                }
            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }

            player.createAI(AI_instance);
            player.createNegotiationAgent(negotiationAgent);
        }

        synchronizer = new Synchronizer(board);
    }

    private static void programTermination() {
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
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}