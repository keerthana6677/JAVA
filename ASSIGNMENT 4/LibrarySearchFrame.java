import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibrarySearchFrame extends JFrame {
    private final JTextField searchField = new JTextField(25);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All Categories","Programming","Database","Systems","Networks","Mathematics"});
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Title","Author","Category","Available"},0);
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    private final LibrarySearchService service = new LibrarySearchService();
    private List<LibraryBook> results = List.of();
    private int page = 0;
    private static final int PAGE_SIZE = 3;

    public LibrarySearchFrame() {
        super("Campus Library Search");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100,750); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Campus Library Search", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD,20f));
        top.add(title, BorderLayout.NORTH);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.CENTER));
        search.add(new JLabel("Search")); search.add(searchField); search.add(category);
        JButton searchButton = new JButton("Search"); search.add(searchButton);
        top.add(search, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel navigation = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton previous = new JButton("Previous"); JButton next = new JButton("Next"); JButton reserve = new JButton("Reserve");
        navigation.add(previous); navigation.add(next); navigation.add(reserve);
        bottom.add(navigation, BorderLayout.NORTH); bottom.add(status, BorderLayout.SOUTH);
        bottom.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        add(bottom, BorderLayout.SOUTH);
        searchButton.addActionListener(e -> runSearch());
        searchField.addActionListener(e -> runSearch());
        previous.addActionListener(e -> { if (page>0) { page--; showPage(); } });
        next.addActionListener(e -> { if ((page+1)*PAGE_SIZE < results.size()) { page++; showPage(); } });
        reserve.addActionListener(e -> reserveSelected());
    }
    private void runSearch() {
        if (searchField.getText().trim().isEmpty()) {
            status.setText("Enter a title or author to search."); return;
        }
        results = service.search(searchField.getText(), (String)category.getSelectedItem());
        page = 0; showPage();
        status.setText(results.isEmpty() ? "No matching books found." : results.size()+" matching book(s).");
    }
    private void showPage() {
        model.setRowCount(0);
        int end = Math.min((page+1)*PAGE_SIZE, results.size());
        for (int i=page*PAGE_SIZE;i<end;i++) {
            LibraryBook b=results.get(i); model.addRow(new Object[]{b.title(),b.author(),b.category(),b.available()?"Yes":"No"});
        }
        status.setText(results.isEmpty() ? "No results." : "Showing "+(page*PAGE_SIZE+1)+"-"+end+" of "+results.size());
    }
    private void reserveSelected() {
        int row=table.getSelectedRow();
        if (row<0) { status.setText("Select a book to reserve."); return; }
        String title=(String)model.getValueAt(row,0);
        String available=(String)model.getValueAt(row,3);
        status.setText("No".equals(available) ? "Cannot reserve: "+title+" is unavailable." : "Reservation confirmed for "+title+".");
    }
}
