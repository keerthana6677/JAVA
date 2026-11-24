import javax.swing.SwingUtilities;
public class LibrarySearchApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchFrame().setVisible(true));
    }
}
