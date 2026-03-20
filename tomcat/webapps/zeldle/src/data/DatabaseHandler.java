package data;

import java.sql.*;
import java.util.*;

import model.Item;
import model.Guess;

public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost:5432/zeldle";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "8530";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static Item createItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("classification"),
                rs.getString("primary_type"),
                rs.getString("secondary_type"),
                rs.getString("location"),
                rs.getString("game"),
                rs.getObject("value", Integer.class)
        );
    }

    public static Item getRandomItem() throws SQLException {
        String query = "SELECT * FROM items ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return createItem(rs);
            }
        }
        return null;
    }

    public static Item getItem(String name) throws SQLException {
        String query = "SELECT * FROM items WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createItem(rs);
                }
            }
        }
        return null;
    }

    public static void saveGuess(Guess guess) throws SQLException {
        String query = "INSERT INTO guesses (session_id, guessed_name, guessed_classification, guessed_primary_type, guessed_secondary_type, guessed_location, guessed_game, guessed_value, " +
                "name_result, classification_result, primary_type_result, secondary_type_result, location_result, game_result, game_arrow, value_result, value_arrow, correct, guesses_remaining) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, guess.getSessionID());
            pstmt.setString(2, guess.getGuessedName());
            pstmt.setString(3, guess.getGuessedClassification());
            pstmt.setString(4, guess.getGuessedPType());
            pstmt.setString(5, guess.getGuessedSType());
            pstmt.setString(6, guess.getGuessedLocation());
            pstmt.setString(7, guess.getGuessedGame());
            pstmt.setObject(8, guess.getGuessedValue());
            pstmt.setString(9, guess.getNameResult());
            pstmt.setString(10, guess.getClassificationResult());
            pstmt.setString(11, guess.getPTypeResult());
            pstmt.setString(12, guess.getSTypeResult());
            pstmt.setString(13, guess.getLocationResult());
            pstmt.setString(14, guess.getGameResult());
            pstmt.setString(15, guess.getGameArrow());
            pstmt.setString(16, guess.getValueResult());
            pstmt.setString(17, guess.getValueArrow());
            pstmt.setBoolean(18, guess.isCorrect());
            pstmt.setInt(19, guess.getGuessesRemaining());
            pstmt.executeUpdate();
        }
    }

    public static List<Guess> getGuessHistory(String sessionID) throws SQLException {
        String query = "SELECT * FROM guesses WHERE session_id = ? ORDER BY id ASC";
        List<Guess> guesses = new ArrayList<>();

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, sessionID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    guesses.add(new Guess(
                        rs.getString("session_id"),
                        rs.getString("guessed_name"),
                        rs.getString("guessed_classification"),
                        rs.getString("guessed_primary_type"),
                        rs.getString("guessed_secondary_type"),
                        rs.getString("guessed_location"),
                        rs.getString("guessed_game"),
                        rs.getObject("guessed_value", Integer.class),
                        rs.getString("name_result"),
                        rs.getString("classification_result"),
                        rs.getString("primary_type_result"),
                        rs.getString("secondary_type_result"),
                        rs.getString("location_result"),
                        rs.getString("game_result"),
                        rs.getString("game_arrow"),
                        rs.getString("value_result"),
                        rs.getString("value_arrow"),
                        rs.getBoolean("correct"),
                        rs.getInt("guesses_remaining")
                    ));
                }
            }
        }

        return guesses;
    }

    public static void clearGuesses(String sessionID) throws SQLException {
        String query = "DELETE FROM guesses WHERE session_id = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, sessionID);
            pstmt.executeUpdate();
        }
    }
}