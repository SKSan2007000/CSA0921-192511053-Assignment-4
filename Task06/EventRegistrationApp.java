import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EventRegistrationApp extends JFrame {
    private static final int PAGE_SIZE = 20;
    private final RegistrationService service = new RegistrationService();
    private final JTextField studentId = new JTextField(12);
    private final JTextField studentName = new JTextField(12);
    private final JTextField email = new JTextField(15);
    private final JComboBox<String> event = new JComboBox<>(new String[]{
            "Select event", "Cultural Fest", "Tech Symposium", "Sports Meet", "Workshop"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{
            "All", "Cultural Fest", "Tech Symposium", "Sports Meet", "Workshop"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Student ID", "Name", "Event", "Email"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready.");
    private final JLabel pageLabel = new JLabel("Page 1");
    private int page = 0;
    private int total = 0;

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(makeMenu());

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Student registration"));
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Student name")); form.add(studentName);
        form.add(new JLabel("Event")); form.add(event);
        form.add(new JLabel("Email")); form.add(email);

        JButton register = new JButton("Register");
        register.addActionListener(e -> registerStudent());
        JPanel formArea = new JPanel(new BorderLayout());
        formArea.add(form, BorderLayout.CENTER);
        formArea.add(register, BorderLayout.SOUTH);
        add(formArea, BorderLayout.NORTH);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterBar.add(new JLabel("Filter event:"));
        filterBar.add(filter);
        JButton apply = new JButton("Apply filter");
        apply.addActionListener(e -> { page = 0; loadPage(); });
        filterBar.add(apply);

        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        previous.addActionListener(e -> { if (page > 0) { page--; loadPage(); } });
        next.addActionListener(e -> { if ((page + 1) * PAGE_SIZE < total) { page++; loadPage(); } });
        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.add(filterBar, BorderLayout.NORTH);
        tableArea.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel paging = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paging.add(previous); paging.add(pageLabel); paging.add(next);
        tableArea.add(paging, BorderLayout.SOUTH);
        add(tableArea, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(EventRegistrationApp.this,
                        "Close the registration application?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) dispose();
            }
        });
        setSize(780, 570);
        setLocationRelativeTo(null);
        loadPage();
    }

    private JMenuBar makeMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export current page");
        JMenuItem exit = new JMenuItem("Exit");
        export.addActionListener(e -> exportPage());
        exit.addActionListener(e -> dispatchEvent(
                new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        file.add(export); file.addSeparator(); file.add(exit);
        bar.add(file);
        return bar;
    }

    private void registerStudent() {
        String id = studentId.getText();
        String name = studentName.getText();
        String selectedEvent = (String) event.getSelectedItem();
        String mail = email.getText();
        status.setText("Saving registration...");
        setBusy(true);
        new SwingWorker<Void, Void>() {
            private Exception problem;
            @Override protected Void doInBackground() {
                try { service.register(id, name, selectedEvent, mail); }
                catch (Exception ex) { problem = ex; }
                return null;
            }
            @Override protected void done() {
                setBusy(false);
                if (problem == null) {
                    status.setText("Registration saved.");
                    studentId.setText(""); studentName.setText(""); email.setText("");
                    page = 0; loadPage();
                } else if (problem instanceof SQLException
                        && problem.getMessage().toLowerCase().contains("duplicate")) {
                    status.setText("This student is already registered for this event.");
                } else {
                    status.setText(problem.getMessage());
                }
            }
        }.execute();
    }

    private void loadPage() {
        String selected = (String) filter.getSelectedItem();
        if (selected == null) selected = "All";
        final String selectedFilter = selected;
        status.setText("Loading registrations...");
        setBusy(true);
        new SwingWorker<List<Registration>, Void>() {
            private int rowCount;
            @Override protected List<Registration> doInBackground() throws Exception {
                rowCount = service.count(selectedFilter);
                return service.page(selectedFilter, PAGE_SIZE, page * PAGE_SIZE);
            }
            @Override protected void done() {
                setBusy(false);
                try {
                    List<Registration> rows = get();
                    total = rowCount;
                    model.setRowCount(0);
                    for (Registration r : rows) {
                        model.addRow(new Object[]{r.getId(), r.getStudentId(),
                                r.getStudentName(), r.getEventName(), r.getEmail()});
                    }
                    pageLabel.setText("Page " + (page + 1) + " of "
                            + Math.max(1, (int)Math.ceil(total / (double)PAGE_SIZE)));
                    status.setText(total + " registration(s) found.");
                } catch (Exception ex) {
                    Throwable cause = ex.getCause();
                    status.setText("Database error: "
                            + (cause == null ? ex.getMessage() : cause.getMessage()));
                }
            }
        }.execute();
    }

    private void exportPage() {
        try (FileWriter writer = new FileWriter("registrations_page.csv")) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                writer.write(model.getColumnName(c) + (c + 1 == model.getColumnCount() ? "\n" : ","));
            }
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    String value = String.valueOf(model.getValueAt(r, c)).replace("\"", "\"\"");
                    writer.write("\"" + value + "\"" + (c + 1 == model.getColumnCount() ? "\n" : ","));
                }
            }
            status.setText("Exported current page to registrations_page.csv.");
        } catch (IOException ex) {
            status.setText("Export failed: " + ex.getMessage());
        }
    }

    private void setBusy(boolean busy) {
        setCursor(Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true));
    }
}
