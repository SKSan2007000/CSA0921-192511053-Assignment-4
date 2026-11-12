import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    public void insert(Registration r) throws SQLException {
        String sql = "INSERT INTO registrations(student_id, student_name, event_name, email) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getStudentId());
            ps.setString(2, r.getStudentName());
            ps.setString(3, r.getEventName());
            ps.setString(4, r.getEmail());
            ps.executeUpdate();
        }
    }

    public List<Registration> page(String event, int limit, int offset) throws SQLException {
        String sql = "SELECT id, student_id, student_name, event_name, email "
                + "FROM registrations WHERE (? = 'All' OR event_name = ?) "
                + "ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Registration> rows = new ArrayList<>();
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, event);
            ps.setString(2, event);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Registration(rs.getInt("id"), rs.getString("student_id"),
                            rs.getString("student_name"), rs.getString("event_name"),
                            rs.getString("email")));
                }
            }
        }
        return rows;
    }

    public int count(String event) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE (? = 'All' OR event_name = ?)";
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, event);
            ps.setString(2, event);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
