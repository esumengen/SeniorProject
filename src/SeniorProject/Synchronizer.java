package SeniorProject;

import org.ini4j.Wini;

import java.io.File;
import java.util.Scanner;

enum SynchronizerState {
    RUNNING, WAITING
}

class Synchronizer {
    private SynchronizerState state;
    private Board board;

    Synchronizer(Board board) {
        this.state = SynchronizerState.WAITING;
        this.board = board;
    }

    void sync(File file, Wini communication_ini) {
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
        } catch (Exception e) {
            new Message(e.getMessage() + "(Err: 2)");
        }

        setState(SynchronizerState.WAITING);
    }

    boolean isSynchronized(Wini communication_ini) {
        String isSynchronized_str = Global.getRidOf_quotationMarks(communication_ini.get("General", "isSynchronized", String.class));

        return !isSynchronized_str.equals("false");
    }

    SynchronizerState getState() {
        return state;
    }

    void setState(SynchronizerState state) {
        this.state = state;
    }
}