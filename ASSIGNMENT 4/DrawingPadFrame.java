import javax.swing.*;
import java.awt.*;

public class DrawingPadFrame extends JFrame {
    public DrawingPadFrame() {
        super("Drawing Pad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760,550); setLocationRelativeTo(null);
        JLabel counter=new JLabel("Clicks: 0");
        counter.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        add(counter,BorderLayout.NORTH);
        add(new JScrollPane(new DrawingPanel(counter)),BorderLayout.CENTER);
    }
}
