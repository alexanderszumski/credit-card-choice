package com.creditcardchoice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditCardRepository {
    public List<CreditCard> findAll(Connection connection)
            throws SQLException {
        List<CreditCard> cards = new ArrayList<>();
            String sql = "SELECT name, dining_multiplier, grocery_multiplier, travel_multiplier, gas_multiplier, other_multiplier FROM credit_cards";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {

                while (results.next()) {
                    String name = results.getString("name");
                    double diningMultiplier = results.getDouble("dining_multiplier");
                    double groceryMultiplier = results.getDouble("grocery_multiplier");
                    double travelMultiplier = results.getDouble("travel_multiplier");
                    double gasMultiplier = results.getDouble("gas_multiplier");
                    double otherMultiplier = results.getDouble("other_multiplier");
                    CreditCard card = new CreditCard(name, diningMultiplier, groceryMultiplier, travelMultiplier, gasMultiplier, otherMultiplier);
                    cards.add(card);
                }
             }
        return cards;
    }

    public List<CreditCard> findByMinimumDiningMultiplier(Connection connection, double minimumMultiplier)
        throws SQLException {
            List<CreditCard> cards = new ArrayList<>();
            String sql = "SELECT name, dining_multiplier, grocery_multiplier, travel_multiplier, gas_multiplier, other_multiplier FROM credit_cards WHERE dining_multiplier >= ? ";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, minimumMultiplier);

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    String name = results.getString("name");
                    double diningMultiplier = results.getDouble("dining_multiplier");
                    double groceryMultiplier = results.getDouble("grocery_multiplier");
                    double travelMultiplier = results.getDouble("travel_multiplier");
                    double gasMultiplier = results.getDouble("gas_multiplier");
                    double otherMultiplier = results.getDouble("other_multiplier");
                    CreditCard card = new CreditCard(name, diningMultiplier, groceryMultiplier, travelMultiplier, gasMultiplier, otherMultiplier);
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    public int updateDiningMultiplier(
        Connection connection, int cardId, double newMultiplier)
        throws SQLException {

    String sql = "UPDATE credit_cards  SET dining_multiplier = ? WHERE id = ? ";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setDouble(1, newMultiplier);
        statement.setInt(2, cardId);
        return statement.executeUpdate();
    }
    }

    public int addCard(Connection connection, CreditCard card)
        throws SQLException {
            String sql = "INSERT INTO credit_cards (name, dining_multiplier, grocery_multiplier, travel_multiplier, gas_multiplier, other_multiplier) VALUES (?, ?, ?, ?, ?, ?) ";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, card.getName());
        statement.setDouble(2, card.getDining());
        statement.setDouble(3, card.getGrocery());
        statement.setDouble(4, card.getTravel());
        statement.setDouble(5, card.getGas());
        statement.setDouble(6, card.getOther());
        return statement.executeUpdate();
    }
    }

    public List<CreditCard> findRankedBySpending(Connection connection, double dining, double grocery, double travel, double gas, double other)
        throws SQLException{
            List<CreditCard> cards = new ArrayList<>();
            String sql = "SELECT name, dining_multiplier, grocery_multiplier, travel_multiplier, gas_multiplier, other_multiplier FROM credit_cards ORDER BY ( dining_multiplier * ? + grocery_multiplier * ? + travel_multiplier * ? + gas_multiplier * ? + other_multiplier * ? ) DESC, name ASC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, dining);
                statement.setDouble(2, grocery);
                statement.setDouble(3, travel);
                statement.setDouble(4, gas);
                statement.setDouble(5, other);
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        String name = results.getString("name");
                        double diningMultiplier = results.getDouble("dining_multiplier");
                        double groceryMultiplier = results.getDouble("grocery_multiplier");
                        double travelMultiplier = results.getDouble("travel_multiplier");
                        double gasMultiplier = results.getDouble("gas_multiplier");
                        double otherMultiplier = results.getDouble("other_multiplier");
                        CreditCard card = new CreditCard(name, diningMultiplier, groceryMultiplier, travelMultiplier, gasMultiplier, otherMultiplier);
                        cards.add(card);
                    }
                }
            }
        return cards;
        }
}