import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;

public class LotteryTicketGenerator {

    public static void generateTicket(int choice, Scanner scanner, CurrentUser currentUser, UserDatabase userDatabase) {
        double ticketPrice = getTicketPrice(choice);
        System.out.println("\nYou have selected " + getLotteryTypeName(choice) + " for $" + ticketPrice);

        int inputOption = getUserInputOption(scanner);
        int[] lotteryNumbers;

        if (inputOption == 1) {
            lotteryNumbers = enterNumbersManually(scanner);
        } else {
            lotteryNumbers = generateLotteryNumbers();
        }

        int ticketId = generateRandomTicketId();
        System.out.println("Your Ticket ID: " + ticketId);
        System.out.println("Your Numbers: " + Arrays.toString(lotteryNumbers));

        User user = currentUser.getUser();
        if (user != null) {
            String ticketDetails = String.format("%s, Ticket ID: %d, Numbers: %s, Price: $%.2f",
                                         getLotteryTypeName(choice), ticketId, Arrays.toString(lotteryNumbers), ticketPrice);
            user.getOrderHistory().addTicket(ticketDetails);

            try {
                userDatabase.updateUser(user); // Update user in the database
            } catch (IOException e) {
                System.out.println("Error updating user data: " + e.getMessage());
            }
        }
    }

    private static int getUserInputOption(Scanner scanner) {
        System.out.println("Choose an option for entering numbers:");
        System.out.println("1. Enter numbers manually");
        System.out.println("2. Generate random numbers");
        return scanner.nextInt();
    }

    private static int[] enterNumbersManually(Scanner scanner) {
        int[] numbers = new int[5];
        System.out.println("Enter 5 numbers (between 1 and 50):");
        for (int i = 0; i < 5; i++) {
            System.out.print("Number " + (i + 1) + ": ");
            numbers[i] = scanner.nextInt();
            if (numbers[i] < 1 || numbers[i] > 50) {
                System.out.println("Invalid input. Numbers must be between 1 and 50. Please try again.");
                i--; // Decrement to re-enter the same index
            }
        }
        return numbers;
    }

    private static int[] generateLotteryNumbers() {
        Random random = new Random();
        int[] numbers = new int[5];
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextInt(50) + 1; // Generate numbers between 1 and 50
        }
        return numbers;
    }

        private static String getLotteryTypeName(int choice) {
        try {
            TicketDatabase ticketDb = new TicketDatabase("tickets.txt");
            List<String> tickets = ticketDb.getTickets();
            if (choice >= 1 && choice <= tickets.size()) {
                String ticket = tickets.get(choice - 1);
                String[] ticketDetails = ticket.substring(1, ticket.indexOf(']')).split(",");
                return ticketDetails[0];
            }
        } catch (IOException e) {
            System.out.println("Error reading tickets: " + e.getMessage());
        }
        return "Unknown Lottery Type";
    }

    private static double getTicketPrice(int choice) {
        try {
            TicketDatabase ticketDb = new TicketDatabase("tickets.txt");
            List<String> tickets = ticketDb.getTickets();
            if (choice >= 1 && choice <= tickets.size()) {
                String ticket = tickets.get(choice - 1);
                String[] ticketDetails = ticket.substring(1, ticket.indexOf(']')).split(",");
                return Double.parseDouble(ticketDetails[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading tickets: " + e.getMessage());
        }
        return 0.0;
    }

    private static int generateRandomTicketId() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generates a 6-digit random number
    }
}
