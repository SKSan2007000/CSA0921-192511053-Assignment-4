import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FeeWindow extends JFrame {
    private final JTextField regNo = new JTextField(14);
    private final JTextField amount = new JTextField(14);
    private final JLabel status = new JLabel("Enter details and choose an action.");
    private final FeeService service = new FeeService(null);
    private String lastReceipt = null;

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));

        JPanel fields = new JPanel(new GridLayout(2, 2, 6, 6));
        fields.add(new JLabel("Register number:"));
        fields.add(regNo);
        fields.add(new JLabel("Amount (INR):"));
        fields.add(amount);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton pay = new JButton("Pay");
        JButton clear = new JButton("Clear");
        JButton receipt = new JButton("Receipt");
        buttons.add(pay);
        buttons.add(clear);
        buttons.add(receipt);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);

        pay.addActionListener(e -> payFee());
        clear.addActionListener(e -> {
            regNo.setText("");
            amount.setText("");
            status.setText("Fields cleared.");
        });
        receipt.addActionListener(e -> {
            String message = lastReceipt == null
                    ? "No saved payment is available for a receipt."
                    : lastReceipt;
            JOptionPane.showMessageDialog(this, message, "Receipt",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 190);
        setLocationRelativeTo(null);
    }

    private void payFee() {
        String id = regNo.getText();
        String value = amount.getText();
        status.setText("Checking payment...");
        setButtonsEnabled(false);
        new javax.swing.SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {
                return service.pay(id, value);
            }
            @Override protected void done() {
                setButtonsEnabled(true);
                try {
                    String message = get();
                    status.setText(message);
                    if (message.startsWith("Payment recorded")) lastReceipt = message;
                } catch (Exception ex) {
                    status.setText(ex.getCause() == null
                            ? ex.getMessage() : ex.getCause().getMessage());
                }
            }
        }.execute();
    }

    private void setButtonsEnabled(boolean enabled) {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                for (Component child : ((JPanel) component).getComponents()) {
                    if (child instanceof JButton) child.setEnabled(enabled);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
