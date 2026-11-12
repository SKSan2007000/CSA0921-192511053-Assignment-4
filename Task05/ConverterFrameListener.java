import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class ConverterFrameListener extends JFrame implements ActionListener {
    private final JTextField amount = new JTextField(9);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convert = new JButton("Convert");

    public ConverterFrameListener() {
        super("Converter - Frame Listener");
        setLayout(new FlowLayout());
        add(new JLabel("INR")); add(amount); add(target); add(convert); add(result);
        convert.addActionListener(this);
        amount.addActionListener(this);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    @Override public void actionPerformed(ActionEvent e) { convert(); }

    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (value < 0) throw new NumberFormatException();
            String currency = (String) target.getSelectedItem();
            double converted = value * rate(currency);
            result.setText(String.format("%.2f %s", converted, currency));
            JOptionPane.showMessageDialog(this, result.getText(), "Conversion", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
        }
    }

    private double rate(String currency) { return currency.equals("USD") ? 0.012 : 0.011; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterFrameListener().setVisible(true));
    }
}
