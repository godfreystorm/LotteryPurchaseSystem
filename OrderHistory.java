import java.util.ArrayList;
import java.util.List;

public class OrderHistory {
    private List<String> ticketHistory;

    public OrderHistory() {
        this.ticketHistory = new ArrayList<>();
    }

    public void addTicket(String ticketDetail) {
        this.ticketHistory.add(ticketDetail);
    }

    public List<String> getTicketHistory() {
        return ticketHistory;
    }

    public void setTicketHistory(List<String> history) {
        this.ticketHistory = new ArrayList<>(history); // Create a new list from the provided history
    }
}
