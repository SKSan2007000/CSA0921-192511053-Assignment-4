import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class ConverterAnonymous extends JFrame {
    private final JTextField amount = new JTextField(9);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");

    public ConverterAnonymous() {
        super("Converter - Anonymous Listener");
        setLayout(new FlowLayout());
        JButton button = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target); add(button); add(result);
        ActionListener handler = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { convert(); }
        };
        button.addActionListener(handler);
        amount.addActionListener(handler);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (value < 0) throw new NumberFormatException();
            String c = (String) target.getSelectedItem();
            String text = String.format("%.2f %s", value * (c.equals("USD") ? 0.012 : 0.011), c);
            result.setText(text);
            JOptionPane.showMessageDialog(this, text, "Conversion", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) { result.setText("Invalid amount"); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterAnonymous().setVisible(true));
    }
}
