public class FeeServiceTest {
    public static void main(String[] args) throws Exception {
        FeeService service = new FeeService(null);
        System.out.println(service.pay("192511053", "1500"));
        try {
            service.pay("", "1500");
        } catch (IllegalArgumentException ex) {
            System.out.println("Empty ID: " + ex.getMessage());
        }
        try {
            service.pay("192511053", "abc");
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid amount: " + ex.getMessage());
        }
        try {
            service.pay("192511053", "-5");
        } catch (IllegalArgumentException ex) {
            System.out.println("Negative amount: " + ex.getMessage());
        }
    }
}
