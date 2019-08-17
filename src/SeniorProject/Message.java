package SeniorProject;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

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

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("bug_report.txt", StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(s);
        writer.close();
    }
}