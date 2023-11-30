import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LotteryChecker {

    private static final Map<String, List<Integer>> winningNumbers = new HashMap<>();
    private static final Map<String, Double> prizeAmounts = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        readTicketInfo("ticket.txt");

        Scanner scanner = new Scanner(new File("user.txt"));
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your User ID: ");
        String userID = input.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(userID)) {
                processUserTickets(line);
            }
        }
    }

    private static void readTicketInfo(String filename) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(filename));
        while (fileScanner.hasNextLine()) {
            String[] parts = fileScanner.nextLine().split(":");
            String lotteryType = parts[0];
            List<Integer> numbers = Arrays.stream(parts[1].split(","))
                                          .map(Integer::parseInt)
                                          .collect(Collectors.toList());
            double prizeAmount = Double.parseDouble(parts[2]);

            winningNumbers.put(lotteryType, numbers);
            prizeAmounts.put(lotteryType, prizeAmount);
        }
    }

    private static void processUserTickets(String userData) {
        String[] parts = userData.split("#\\|#");
        String[] tickets = parts[1].split(";;");

        double totalWinning = 0;
        for (String ticket : tickets) {
            totalWinning += checkTicket(ticket);
        }

        System.out.println("Total winning amount: $" + totalWinning);
    }

    private static double checkTicket(String ticket) {
        String[] parts = ticket.split(", Ticket ID: ");
        String lotteryType = parts[0];
        String numbersPart = parts[1].split(", Price: ")[0];

        List<Integer> userNumbers = extractNumbers(numbersPart);
        List<Integer> winningNums = winningNumbers.get(lotteryType);
        double fixedPrizeAmount = prizeAmounts.getOrDefault(lotteryType, 0.0);

        List<Integer> matchedNumbers = userNumbers.stream()
                                                  .filter(winningNums::contains)
                                                  .collect(Collectors.toList());
        int matchCount = matchedNumbers.size();
        double prizePercentage = calculatePrizePercentage(matchCount);
        double winningAmount = fixedPrizeAmount * prizePercentage;

        if (winningAmount > 0) {
            System.out.println("Winning ticket: " + Arrays.toString(userNumbers.toArray()) 
                               + " in " + lotteryType 
                               + " - Matched Numbers: " + matchedNumbers
                               + " - Prize: $" + winningAmount);
        }

        return winningAmount;
    }

    private static List<Integer> extractNumbers(String numberPart) {
        int startIndex = numberPart.indexOf('[');
        int endIndex = numberPart.indexOf(']');

        if (startIndex == -1 || endIndex == -1) {
            throw new IllegalArgumentException("Invalid number format: " + numberPart);
        }

        String[] nums = numberPart.substring(startIndex + 1, endIndex).split(", ");
        return Arrays.stream(nums).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static double calculatePrizePercentage(int matchCount) {
        switch (matchCount) {
            case 5:
                return 1.0; // 100%
            case 4:
                return 0.2; // 20%
            case 3:
                return 0.05; // 5%
            case 2:
                return 0.01; // 1%
            default:
                return 0.0; // No prize
        }
    }
}
