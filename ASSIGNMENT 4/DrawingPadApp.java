import javax.swing.SwingUtilities;
public class DrawingPadApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DrawingPadFrame().setVisible(true));
    }
}
