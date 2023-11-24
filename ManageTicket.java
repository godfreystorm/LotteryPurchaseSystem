import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ManageTicket {
    private TicketDatabase ticketDb;
    private Scanner scanner;

    public ManageTicket(String ticketsFilePath) {
        this.ticketDb = new TicketDatabase(ticketsFilePath);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("");
            System.out.println("1. Add a Ticket");
            System.out.println("2. View Tickets");
            System.out.println("3. Remove a Ticket");
            System.out.println("4. Edit a Ticket");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            try {
                switch (choice) {
                    case 1:
                        addTicket(scanner, ticketDb);
                        break;
                    case 2:
                        ticketDb.viewTickets();
                        break;
                    case 3:
                        removeTicket(scanner, ticketDb);
                        break;
                    case 4:
                        editTicket(scanner, ticketDb);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                System.out.println(); // Add a newline for spacing after each operation
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addTicket(Scanner scanner, TicketDatabase ticketDb) throws IOException {
        System.out.print("Enter ticket name: ");
        String name = scanner.nextLine();

        System.out.print("Enter ticket price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        System.out.print("Enter winning numbers separated by commas (e.g., 1,2,3,4,5): ");
        String numbers = scanner.nextLine();
        List<Integer> winningNumbers = new ArrayList<>();
        for (String num : numbers.split(",")) {
            winningNumbers.add(Integer.parseInt(num.trim()));
        }

        ticketDb.addTicket(name, price, winningNumbers);
        System.out.println("Ticket added successfully!");
    }

    private static void editTicket(Scanner scanner, TicketDatabase ticketDb) throws IOException {
        List<String> tickets = ticketDb.getTickets();
    
        System.out.println("");
        // Display all tickets with line numbers
        for (int i = 0; i < tickets.size(); i++) {
            System.out.println((i + 1) + ". " + tickets.get(i));
        }
    
        System.out.print("Enter the number of the ticket to edit: ");
        int ticketNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        System.out.println("\n1. Edit Name");
        System.out.println("2. Edit Price");
        System.out.println("3. Edit Winning Numbers");
        System.out.println("4. Edit Whole Ticket");
        System.out.print("Enter your choice: ");
        int editChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        switch (editChoice) {
            case 1:
                // Edit Name
                System.out.print("Enter new ticket name: ");
                String newName = scanner.nextLine();
                ticketDb.editTicketName(ticketNumber - 1, newName);
                break;
            case 2:
                // Edit Price
                System.out.print("Enter new ticket price: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline
                ticketDb.editTicketPrice(ticketNumber - 1, newPrice);
                break;
            case 3:
                // Edit Winning Numbers
                System.out.print("Enter new winning numbers separated by commas (e.g., 1,2,3,4,5): ");
                String numbers = scanner.nextLine();
                List<Integer> newWinningNumbers = new ArrayList<>();
                for (String num : numbers.split(",")) {
                    newWinningNumbers.add(Integer.parseInt(num.trim()));
                }
                ticketDb.editTicketWinningNumbers(ticketNumber - 1, newWinningNumbers);
                break;
            case 4:
                // Edit Whole Ticket
                System.out.print("Enter new ticket name: ");
                String name = scanner.nextLine();
    
                System.out.print("Enter new ticket price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline
    
                System.out.print("Enter new winning numbers separated by commas (e.g., 1,2,3,4,5): ");
                String allNumbers = scanner.nextLine();
                List<Integer> winningNumbers = new ArrayList<>();
                for (String num : allNumbers.split(",")) {
                    winningNumbers.add(Integer.parseInt(num.trim()));
                }
    
                ticketDb.editTicket(ticketNumber - 1, name, price, winningNumbers);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    
        System.out.println("Ticket edited successfully!");
    }
    

    private static void removeTicket(Scanner scanner, TicketDatabase ticketDb) throws IOException {
        List<String> tickets = ticketDb.getTickets();

        // Display all tickets with line numbers
        for (int i = 0; i < tickets.size(); i++) {
            System.out.println((i + 1) + ". " + tickets.get(i));
        }

        System.out.print("Enter the number of the ticket to remove: ");
        int ticketNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Adjusting for zero-based index
        ticketDb.removeTicket(ticketNumber - 1);
        System.out.println("Ticket removed successfully!");
    }
}
