import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketDatabase {

    private String filePath;

    public TicketDatabase(String filePath) {
        this.filePath = filePath;
    }

    public void addTicket(String ticketName, double ticketPrice, List<Integer> winningNumbers) throws IOException {
        String ticketDetails = String.format("[%s,%.2f,WinningNumbers: %s]%n", ticketName, ticketPrice, winningNumbers.toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(ticketDetails);
        }
    }

    public void viewTickets() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public void removeTicket(int ticketIndex) throws IOException {
        List<String> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tickets.add(line);
            }
        }
        if (ticketIndex >= 0 && ticketIndex < tickets.size()) {
            tickets.remove(ticketIndex);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String ticket : tickets) {
                    writer.write(ticket);
                    writer.newLine();
                }
            }
        } else {
            System.out.println("Invalid ticket index");
        }
    }

    public List<String> getTickets() throws IOException {
        List<String> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tickets.add(line);
            }
        }
        return tickets;
    }

    public void editTicket(int ticketIndex, String newTicketName, double newTicketPrice, List<Integer> newWinningNumbers) throws IOException {
        List<String> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tickets.add(line);
            }
        }
        if (ticketIndex >= 0 && ticketIndex < tickets.size()) {
            String newTicketDetails = String.format("[%s,%.2f,WinningNumbers: %s]", newTicketName, newTicketPrice, newWinningNumbers.toString());
            tickets.set(ticketIndex, newTicketDetails);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String ticket : tickets) {
                    writer.write(ticket);
                    writer.newLine();
                }
            }
        } else {
            System.out.println("Invalid ticket index");
        }
    }

    public void editTicketName(int ticketIndex, String newTicketName) throws IOException {
        List<String> tickets = getTickets();
        if (isValidIndex(ticketIndex, tickets)) {
            String[] parts = tickets.get(ticketIndex).split(",", 3);
            parts[0] = "[" + newTicketName;
            tickets.set(ticketIndex, String.join(",", parts));
            saveTickets(tickets);
        }
    }

    public void editTicketPrice(int ticketIndex, double newTicketPrice) throws IOException {
        List<String> tickets = getTickets();
        if (isValidIndex(ticketIndex, tickets)) {
            String[] parts = tickets.get(ticketIndex).split(",", 3);
            parts[1] = String.format("%.2f", newTicketPrice);
            tickets.set(ticketIndex, String.join(",", parts));
            saveTickets(tickets);
        }
    }

    public void editTicketWinningNumbers(int ticketIndex, List<Integer> newWinningNumbers) throws IOException {
        List<String> tickets = getTickets();
        if (isValidIndex(ticketIndex, tickets)) {
            String[] parts = tickets.get(ticketIndex).split(",", 3);
            parts[2] = "WinningNumbers: " + newWinningNumbers.toString();
            tickets.set(ticketIndex, String.join(",", parts));
            saveTickets(tickets);
        }
    }

    private boolean isValidIndex(int index, List<String> list) {
        return index >= 0 && index < list.size();
    }

    private void saveTickets(List<String> tickets) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String ticket : tickets) {
                writer.write(ticket);
                writer.newLine();
            }
        }
    }
}
