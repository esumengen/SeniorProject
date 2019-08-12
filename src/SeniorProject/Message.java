package SeniorProject;

import javax.swing.*;
import java.awt.*;

public class Message extends JFrame {
    public Message(String s) {
        super("");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel(s);

        panel.add(label);

        super.add(panel);
        super.setSize(500, 100);
        super.setLocation(500, 350);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setVisible(true);
    }
}