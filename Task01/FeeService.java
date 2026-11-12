import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* Keeps payment rules outside the window. */
public class FeeService {
    private final Connection connection;

    public FeeService(Connection connection) {
        this.connection = connection;
    }

    public String pay(String regNo, String amountText) throws Exception {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter the register number.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Enter a valid amount.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (connection == null) {
            return "Payment checked for " + regNo.trim()
                    + ". Connect a fee database to save the payment.";
        }

        String sql = "INSERT INTO fees(reg_no, amount) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, regNo.trim());
            ps.setDouble(2, amount);
            ps.executeUpdate();
        }
        return "Payment recorded for " + regNo.trim();
    }
}
