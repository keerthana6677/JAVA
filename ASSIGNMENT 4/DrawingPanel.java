import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {
    private final List<Point> points = new ArrayList<>();
    private final JLabel counter;

    public DrawingPanel(JLabel counter) {
        this.counter = counter;
        setPreferredSize(new Dimension(700,450));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent event) {
                points.add(event.getPoint());
                counter.setText("Clicks: " + points.size());
                repaint();
            }
        });
    }
    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g=(Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLUE);
        for (Point point:points) g.fillOval(point.x-5,point.y-5,10,10);
        g.dispose();
    }
}
