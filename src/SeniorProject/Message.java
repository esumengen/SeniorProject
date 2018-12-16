package SeniorProject;

import javax.swing.*;
import java.awt.*;

class Message extends JFrame {
    private JPanel panel;
    private JLabel label;

    Message(String s) {
        super("");
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        label = new JLabel(s);

        panel.add(label);

        super.add(panel);
        super.setSize(500, 100);
        super.setLocation(500, 350);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setVisible(true);
    }
}