import java.io.IOException;
import java.util.List;
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
                    showUserProfile();
                    break;
                case 4:
                    showOrderHistory();
                    break;
                case 5:
                    searchForTicket(scanner);
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
    try {
        TicketDatabase ticketDb = new TicketDatabase("tickets.txt"); // Replace with the actual path
        List<String> tickets = ticketDb.getTickets();

        System.out.println("");
        System.out.println("These are the lottery tickets we have available, to choose from:");

        for (int i = 0; i < tickets.size(); i++) {
            String ticket = tickets.get(i);
            String[] ticketDetails = ticket.substring(1, ticket.indexOf(']')).split(",");
            String ticketName = ticketDetails[0];
            String ticketPrice = ticketDetails[1];
            System.out.println((i + 1) + ". " + ticketName + " - $" + ticketPrice);
        }

        int lotteryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        if (lotteryChoice >= 1 && lotteryChoice <= tickets.size()) {
            showLotteryOptions(lotteryChoice);
        } else {
            System.out.println("Invalid choice. Please choose a number between 1 and " + tickets.size() + ".");
        }
    } catch (IOException e) {
        System.out.println("Error reading tickets: " + e.getMessage());
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
        try {
            TicketDatabase ticketDb = new TicketDatabase("tickets.txt"); // Ensure this is the correct path
            List<String> tickets = ticketDb.getTickets();
    
            if (choice >= 1 && choice <= tickets.size()) {
                String ticket = tickets.get(choice - 1); // Adjust for zero-based index
                String[] ticketDetails = ticket.substring(1, ticket.indexOf(']')).split(",");
                return ticketDetails[0]; // Return the ticket name
            }
        } catch (IOException e) {
            System.out.println("Error reading tickets: " + e.getMessage());
        }
        return "Unknown Lottery Type";
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

    private static void searchForTicket(Scanner scanner) {
        System.out.print("Enter TicketID (If you do not know ticket ID, check orders history): ");
        int ticketId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        User currentUser = CurrentUser.getUser();
        if (currentUser != null) {
            OrderHistory orderHistory = currentUser.getOrderHistory();
            List<String> ticketHistory = orderHistory.getTicketHistory();
            boolean ticketFound = false;
    
            for (String ticket : ticketHistory) {
                if (ticket.contains("Ticket ID: " + ticketId)) {
                    System.out.println("Ticket Found: " + ticket);
                    ticketFound = true;
                    break;
                }
            }
    
            if (!ticketFound) {
                System.out.println("Ticket ID not found in your order history.");
            }
        } else {
            System.out.println("No user currently logged in.");
        }
    }

    private static void showUserProfile() {
        User user = CurrentUser.getUser();
        if (user != null) {
            System.out.println("Profile Details:");
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("Age: " + user.getAge());
    
            System.out.println("\n1. Edit Profile Info");
            System.out.println("2. Back");
            System.out.print("Enter your choice: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
    
            if (choice == 1) {
                editUserProfile();
            }
            // No need to handle 'Back' as it will just return to the previous menu
        } else {
            System.out.println("No user currently logged in.");
        }
    }

    private static void editUserProfile() {
        User user = CurrentUser.getUser();
        if (user == null) {
            System.out.println("No user currently logged in.");
            return;
        }
    
        System.out.println("\nSelect the information you want to edit:");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Password");
        System.out.println("4. Phone Number");
        System.out.println("5. Age");
        System.out.println("6. Back");
        System.out.print("Enter your choice: ");
    
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        if (choice == 6) return; // Back to profile page
        
    
        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();

        boolean emailChanged = false;
    
        switch (choice) {
            case 1:
                user.setName(newValue);
                break;
            case 2:
                user.setEmail(newValue);
                emailChanged = true;
                break;
            case 3:
                user.setPassword(newValue);
                break;
            case 4:
                user.setPhoneNumber(newValue);
                break;
            case 5:
                user.setAge(Integer.parseInt(newValue));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
    
        try {
            userDatabase.updateUser(user); // Update user in the database
            System.out.println("Profile updated successfully.");
    
            if (emailChanged) {
                CurrentUser.clearUser(); // Clear the current user session
                System.out.println("Email updated successfully.");
                return; // Return to main menu for the user to log in again
            }
        } catch (IOException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }
}
