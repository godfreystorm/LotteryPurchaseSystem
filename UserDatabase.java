import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDatabase {
    private final String filePath = "users.txt";

    public UserDatabase() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addUser(User user) throws IOException {
        Map<String, User> users = getAllUsers();
        users.put(user.getEmail(), user);
        saveAllUsers(users);
    }

    public User getUser(String email) throws IOException {
        Map<String, User> users = getAllUsers();
        return users.getOrDefault(email, null);
    }

    public void updateUser(User updatedUser) throws IOException {
        Map<String, User> users = getAllUsers();
        User existingUser = users.get(updatedUser.getEmail());
    
        if (existingUser != null) {
            // Ensure that only new tickets are added to the existing history
            List<String> existingHistory = existingUser.getOrderHistory().getTicketHistory();
            List<String> newHistory = updatedUser.getOrderHistory().getTicketHistory();
    
            // Remove any duplicate entries from the new history
            newHistory.removeAll(existingHistory);
            existingHistory.addAll(newHistory); // Append only new tickets to existing history
    
            updatedUser.getOrderHistory().setTicketHistory(existingHistory);
    
            users.put(updatedUser.getEmail(), updatedUser); // Replace the user in the map
        }
    
        saveAllUsers(users);
    }
    

    private String serializeTicketHistory(List<String> ticketHistory) {
        return ticketHistory.stream().collect(Collectors.joining(";;"));
    }
    
    private List<String> deserializeTicketHistory(String data) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(data.split(";;")));
    }
    
    private void saveAllUsers(Map<String, User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                String ticketData = serializeTicketHistory(user.getOrderHistory().getTicketHistory());
                writer.write(user.getName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getPhoneNumber() + "," + user.getAge() + "#|#" + ticketData);
                writer.newLine();
            }
        }
    }
    
    public Map<String, User> getAllUsers() throws IOException {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("#\\|#", 2); // Split into user info and ticket history
                String[] userData = parts[0].split(",");
                if (userData.length >= 5) {
                    User user = new User(userData[0], userData[1], userData[2], userData[3], Integer.parseInt(userData[4]));
                    if (parts.length == 2) {
                        OrderHistory orderHistory = new OrderHistory();
                        orderHistory.setTicketHistory(deserializeTicketHistory(parts[1]));
                        user.setOrderHistory(orderHistory);
                    }
                    users.put(user.getEmail(), user);
                }
            }
        }
        return users;
    }
}
