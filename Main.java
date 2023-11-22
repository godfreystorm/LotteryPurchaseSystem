import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static UserDatabase userDatabase = new UserDatabase();
    private static Scanner scanner = new Scanner(System.in);
    private static CurrentUser currentUser = new CurrentUser();
    private static Admin admin = new Admin(scanner, userDatabase);

    public static void main(String[] args) {
        System.out.println("Welcome to the Lottery Purchase System");

        while (true) {
            System.out.println("Choose an option: \n1. Register \n2. Login \n3. Admin \n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                if (admin.adminMenu()) {
                    // If admin logs out, break from the switch to show the main menu again
                    break;
                }
                continue; // If admin did not log out, continue showing the admin menu

                case 4:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        System.out.println("Enter your phone number:");
        String phone = scanner.nextLine();

        System.out.println("Enter your state:");
        String state = scanner.nextLine();
        if (!"Texas".equalsIgnoreCase(state)) {
            System.out.println("Registration failed: State must be Texas.");
            return;
        }

        System.out.println("Enter your age:");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        try {
            User newUser = new User(name, email, password, phone, age);
            userDatabase.addUser(newUser);
            System.out.println("Registration successful.");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An error occurred during registration.");
            e.printStackTrace();
        }
    }

    private static void loginUser() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        try {
            User user = userDatabase.getUser(email);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("\nLogin successful.\n");
                currentUser.setUser(user);  // Set the current user
                boolean isLoggedOut = showUserMenu();
                if (isLoggedOut) {
                    currentUser.clearUser();  // Clear the user after logout
                    return; // Exit this method to avoid showing the main menu immediately
                }
            } else {
                System.out.println("\nLogin failed.\n");
            }
        } catch (IOException e) {
            System.out.println("\nAn error occurred during login.\n");
            e.printStackTrace();
        }
    }

    private static boolean showUserMenu() {
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Browse Lottery Tickets");
            System.out.println("2. Browse Previous Winning Numbers");
            System.out.println("3. Profile Page");
            System.out.println("4. Order History Page");
            System.out.println("5. Search for a Specific Ticket");
            System.out.println("6. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    browseLotteryTickets();
                    break;
                case 2:
                    // Browse Previous Winning Numbers logic
                    break;
                case 3:
                    // Profile Page logic
                    break;
                case 4:
                    showOrderHistory();
                    break;
                case 5:
                    // Search for a Specific Ticket logic
                    break;
                case 6:
                    System.out.println("\nLogging out...\n");
                    return true; // User chooses to logout
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void browseLotteryTickets() {
        System.out.println("");
        System.out.println("These are the lottery tickets we have available, to choose from:");
        System.out.println("1. Mega Millions - $2");
        System.out.println("2. Power Ball - $2");
        System.out.println("3. Lotto Texas - $1");
        System.out.println("4. Texas Two Step - $1.50");

        int lotteryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (lotteryChoice >= 1 && lotteryChoice <= 4) {
            showLotteryOptions(lotteryChoice);
        } else {
            System.out.println("Invalid choice. Please choose a number between 1 and 4.");
        }
    }

    private static void showLotteryOptions(int lotteryChoice) {
        String lotteryName = getLotteryTypeName(lotteryChoice);
        System.out.println("");
        System.out.println("Select an option for " + lotteryName + ":");
        System.out.println("1. Buy Ticket");
        System.out.println("2. More Details about " + lotteryName);

        int option = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        switch (option) {
            case 1:
                LotteryTicketGenerator.generateTicket(lotteryChoice, scanner, currentUser, userDatabase);
                break;
            case 2:
                System.out.println("Details for " + lotteryName + " are not available yet.");
                break;
            default:
                System.out.println("Invalid option. Please choose either 1 or 2.");
        }
    }

    private static String getLotteryTypeName(int choice) {
        switch (choice) {
            case 1:
                return "Mega Millions";
            case 2:
                return "Power Ball";
            case 3:
                return "Lotto Texas";
            case 4:
                return "Texas Two Step";
            default:
                return "Unknown Lottery Type";
        }
    }

    private static void showOrderHistory() {
        System.out.println("\n1. Back to menu");
        System.out.println("These are your past orders:");

        User user = currentUser.getUser();
        if (user != null) {
            for (String ticketDetail : user.getOrderHistory().getTicketHistory()) {
                System.out.println(ticketDetail);
            }
        } else {
            System.out.println("No past orders available.");
        }
    }
}
