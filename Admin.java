import java.util.Scanner;
import java.util.List;
import java.io.IOException;

public class Admin {
    private Scanner scanner;
    private UserDatabase userDatabase;

    public Admin(Scanner scanner, UserDatabase userDatabase) {
        this.scanner = scanner;
        this.userDatabase = userDatabase;
    }

    public boolean adminMenu() {
        System.out.println("Choose an option: \n1. Select 1 to proceed to admin options \n2. Back(Select this if not admin)");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (choice == 1) {
            System.out.println("Enter admin password: ");
            String password = scanner.nextLine();
            if ("admin".equals(password)) {
                return showAdminOptions();
            } else {
                System.out.println("Incorrect password. Returning to main menu.");
            }
        }
        return false; // Stay in the main menu if 'Back' is selected or password is incorrect
    }

    private boolean showAdminOptions() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Current System Status");
            System.out.println("2. Manage Tickets");
            System.out.println("3. Back");

            int adminChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (adminChoice) {
                case 1:
                    if (showSystemStatus()) {
                        return true; // Admin logged out
                    }
                    break;
                case 2:
                    // Placeholder for Manage Tickets functionality
                    System.out.println("Manage Tickets option selected.");
                    break;
                case 3:
                    return false; // Go back to the main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private boolean showSystemStatus() {
        try {
            int totalTicketsSold = 0;
            double totalPrice = 0.0;
            for (User user : userDatabase.getAllUsers().values()) {
                List<String> ticketHistory = user.getOrderHistory().getTicketHistory();
                for (String ticket : ticketHistory) {
                    totalTicketsSold++;
                    totalPrice += extractPriceFromTicket(ticket);
                }
            }

            System.out.println("\n1. Back");
            System.out.println("2. Logout of Admin");
            System.out.println("System status: All systems up and running effectively.");
            System.out.println("Total amount of tickets sold: Till date we have sold " + totalTicketsSold + " for a total price of $" + String.format("%.2f", totalPrice));

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (choice == 1) {
                return false; // Go back to the admin options menu
            } else if (choice == 2) {
                System.out.println("Logging out of admin...");
                return true; // Logout admin
            }
            return false; // Default action is to stay in the system status menu
        } catch (IOException e) {
            System.out.println("Error accessing user data: " + e.getMessage());
            return false;
        }
    }

    private double extractPriceFromTicket(String ticket) {
        try {
            // Split the ticket string by ", " and then take the last part ("Price: $X.XX")
            String[] parts = ticket.split(", ");
            String pricePart = parts[parts.length - 1]; // Get the last part
    
            // Now, split the price part by " " and take the last part ("$X.XX")
            String[] priceParts = pricePart.split(" ");
            String priceStr = priceParts[priceParts.length - 1].substring(1); // Remove the '$'
    
            return Double.parseDouble(priceStr);
        } catch (Exception e) {
            System.out.println("Error parsing ticket price: " + e.getMessage());
            return 0.0;
        }
    }
    
}
