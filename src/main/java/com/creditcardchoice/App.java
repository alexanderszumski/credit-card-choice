package com.creditcardchoice;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (url == null || url.isBlank() || user == null || user.isBlank() || password == null) {
            System.out.println("Set DB_URL, DB_USER, and DB_PASSWORD in this terminal before running.");
            return;
        }

        try (Scanner input = new Scanner(System.in);
             Connection connection = DriverManager.getConnection(url, user, password)) {
            CreditCardRepository repository = new CreditCardRepository();
            while (true) {
                System.out.println("\n1. Compare cards\n2. Add a card\n3. Exit");
                System.out.print("Choose an option: ");
                String choice = input.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> compareCards(input, connection, repository);
                        case "2" -> addCard(input, connection, repository);
                        case "3" -> { return; }
                        default -> System.out.println("Enter 1, 2, or 3.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database operation failed: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nInput closed. Exiting.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    private static void compareCards(Scanner input, Connection connection,
                                     CreditCardRepository repository) throws SQLException {
        double dining = readNumber(input, "Monthly dining spending: ", Double.MAX_VALUE);
        double grocery = readNumber(input, "Monthly grocery spending: ", Double.MAX_VALUE);
        double travel = readNumber(input, "Monthly travel spending: ", Double.MAX_VALUE);
        double gas = readNumber(input, "Monthly gas spending: ", Double.MAX_VALUE);
        double other = readNumber(input, "Monthly other spending: ", Double.MAX_VALUE);
        List<CreditCard> cards = repository.findRankedBySpending(
                connection, dining, grocery, travel, gas, other);
        if (cards.isEmpty()) {
            System.out.println("No cards saved yet. Choose Add a card first.");
            return;
        }
        System.out.println("\nCards ordered by monthly points (not dollar value):");
        for (CreditCard card : cards) {
            System.out.printf("%s: %,.2f points%n", card.getName(),
                    card.calculateMonthlyPoints(dining, grocery, travel, gas, other));
        }
    }

    private static void addCard(Scanner input, Connection connection,
                                CreditCardRepository repository) throws SQLException {
        String name;
        do {
            System.out.print("Card name: ");
            name = input.nextLine().trim();
            if (name.isEmpty()) System.out.println("Enter a nonempty name.");
        } while (name.isEmpty());
        double dining = readNumber(input, "Dining points per dollar: ", 999.99);
        double grocery = readNumber(input, "Grocery points per dollar: ", 999.99);
        double travel = readNumber(input, "Travel points per dollar: ", 999.99);
        double gas = readNumber(input, "Gas points per dollar: ", 999.99);
        double other = readNumber(input, "Other points per dollar: ", 999.99);
        CreditCard card = new CreditCard(name, dining, grocery, travel, gas, other);
        int rowsInserted = repository.addCard(connection, card);
        System.out.println("Cards saved: " + rowsInserted);
    }

    private static double readNumber(Scanner input, String prompt, double maximum) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(input.nextLine().trim());
                if (Double.isFinite(value) && value >= 0 && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Enter a finite, nonnegative number without $ or commas"
                    + (maximum == 999.99 ? " (maximum 999.99)." : "."));
        }
    }
}
