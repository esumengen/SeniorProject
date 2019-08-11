package SeniorProject;

import org.ini4j.Wini;

import java.io.File;
import java.util.Scanner;

enum SynchronizerState {
    RUNNING, WAITING
}

class Synchronizer {
    private static File communicationFile = new File(Global.get_working_path(Global.COMMUNICATION_FILE));
    private SynchronizerState state;
    private Wini communication_ini;
    private Board board;

    Synchronizer(Board board) {
        this.state = SynchronizerState.WAITING;
        this.board = board;

        try {
            communication_ini = new Wini(communicationFile);
        } catch (Exception e) {
            new Message(e.getMessage() + " (Err: 4)");
        }
    }

    void sync(File file) {
        setState(SynchronizerState.RUNNING);

        try {
            String actionText;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                actionText = scanner.nextLine();

                IAction action = ActionFactory.getAction(actionText, board);

                if (action != null)
                    action.execute();
            }

            communication_ini.put("General", "isSynchronized", "\"true\"");
            communication_ini.store();
        } catch (Exception e) {
            new Message(e.getMessage() + "(Err: 2)");
        }

        setState(SynchronizerState.WAITING);
    }

    boolean isSynchronized() {
        String isSynchronized_str = "true";

        try {
            File communicationFile = new File(Global.get_working_path(Global.COMMUNICATION_FILE));
            if (communicationFile.exists()) {
                communication_ini = new Wini(communicationFile);
                isSynchronized_str = communication_ini.get("General", "isSynchronized", String.class);

                isSynchronized_str = Global.getRidOf_quotationMarks(isSynchronized_str);
            } else
                new Message("communication.ini does not exists. (Err: 95)");
        } catch (Exception e) {
            // ? BUG?
            // !
            isSynchronized_str = "true";
            new Message(e.getMessage() + " (Err: 115)");
        }

        return !isSynchronized_str.equals("false");
    }

    SynchronizerState getState() {
        return state;
    }

    void setState(SynchronizerState state) {
        this.state = state;
    }
}