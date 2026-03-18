import java.sql.*;

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
                rs.getString("category"),
                rs.getString("type"),
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
}
