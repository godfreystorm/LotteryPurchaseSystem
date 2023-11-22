import java.util.List;

public class User {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private int age;
    private OrderHistory orderHistory;

    public User(String name, String email, String password, String phoneNumber, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.orderHistory = new OrderHistory();
    }

    public void addTicketToHistory(String ticketDetail) {
        this.orderHistory.addTicket(ticketDetail);
    }

    public List<String> getTicketHistory() {
        return this.orderHistory.getTicketHistory();
    }

    // Getters and setters for other fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }
}
