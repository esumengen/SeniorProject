import javax.swing.*;
import java.awt.*;

public class Warning extends JFrame {
    JPanel panel;
    JLabel label;
    public Warning (String s){
        super("Warning");
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        label = new JLabel(s);
        panel.add(label);
        super.add(panel);
        super.setSize(300, 50);
        super.setLocation(500, 350);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }


}
