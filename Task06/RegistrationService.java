import java.sql.SQLException;
import java.util.List;

public class RegistrationService {
    private final RegistrationDao dao = new RegistrationDao();

    public void register(String id, String name, String event, String email)
            throws SQLException {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Enter student ID.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Enter student name.");
        if (event == null || event.equals("Select event")) throw new IllegalArgumentException("Select an event.");
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new IllegalArgumentException("Enter a valid email address.");

        dao.insert(new Registration(0, id.trim(), name.trim(), event, email.trim()));
    }

    public List<Registration> page(String event, int limit, int offset) throws SQLException {
        return dao.page(event, limit, offset);
    }

    public int count(String event) throws SQLException {
        return dao.count(event);
    }
}
