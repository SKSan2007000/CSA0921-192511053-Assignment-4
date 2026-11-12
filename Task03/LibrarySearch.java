import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibrarySearch extends JFrame {
    private final JTextField searchText = new JTextField(22);
    private final JComboBox<String> category =
            new JComboBox<>(new String[]{"All", "Book", "Author", "Subject"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Title", "Author", "Category", "Availability"}, 0);
    private final JTable results = new JTable(model);
    private final JLabel status = new JLabel("Ready.");

    public LibrarySearch() {
        super("Campus Library Search");
        setLayout(new BorderLayout(6, 6));

        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:"));
        searchRow.add(searchText);
        searchRow.add(category);
        JButton search = new JButton("Search");
        searchRow.add(search);
        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.SOUTH);
        remove(title);
        add(top, BorderLayout.NORTH);

        model.addRow(new Object[]{"Java: The Complete Reference", "Herbert Schildt", "Book", "Available"});
        model.addRow(new Object[]{"Effective Java", "Joshua Bloch", "Book", "Issued"});
        model.addRow(new Object[]{"Database System Concepts", "Silberschatz", "Book", "Available"});
        add(new JScrollPane(results), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        bottom.add(prev); bottom.add(next); bottom.add(reserve);
        JPanel south = new JPanel(new BorderLayout());
        south.add(bottom, BorderLayout.NORTH);
        south.add(status, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        search.addActionListener(e -> doSearch());
        searchText.addActionListener(e -> doSearch());
        prev.addActionListener(e -> status.setText("Previous results page."));
        next.addActionListener(e -> status.setText("Next results page."));
        reserve.addActionListener(e -> status.setText(
                results.getSelectedRow() < 0 ? "Select a book first." : "Reservation request recorded."));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(520, 330));
        setSize(760, 470);
        setLocationRelativeTo(null);
    }

    private void doSearch() {
        String q = searchText.getText().trim().toLowerCase();
        String c = (String) category.getSelectedItem();
        for (int i = model.getRowCount() - 1; i >= 0; i--) model.removeRow(i);
        Object[][] data = {
            {"Java: The Complete Reference", "Herbert Schildt", "Book", "Available"},
            {"Effective Java", "Joshua Bloch", "Book", "Issued"},
            {"Database System Concepts", "Silberschatz", "Book", "Available"}
        };
        int count = 0;
        for (Object[] row : data) {
            boolean categoryMatch = "All".equals(c) || c.equals(row[2]);
            boolean textMatch = q.isEmpty() || row[0].toString().toLowerCase().contains(q)
                    || row[1].toString().toLowerCase().contains(q);
            if (categoryMatch && textMatch) { model.addRow(row); count++; }
        }
        status.setText(count + " result(s).");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true));
    }
}
