public class Registration {
    private final int id;
    private final String studentId;
    private final String studentName;
    private final String eventName;
    private final String email;

    public Registration(int id, String studentId, String studentName,
                        String eventName, String email) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.eventName = eventName;
        this.email = email;
    }

    public int getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getEventName() { return eventName; }
    public String getEmail() { return email; }
}
