package lab15.gabriel.group2.a2.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class User_Scroll_DB {
    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String DB_PATH = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/Database/User_scroll_list.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    //    -------------------------------Main----------------
    public static void main(String[] args) {
        try {
            init_db();
//            List<String> k = Arrays.asList("hi.txt", "b.txt");
//            insertScrollName("123",k);
//
//            List<String> scrollNames = getScrollNamesByUserID("123");
//            if (scrollNames != null && !scrollNames.isEmpty()) {
//                System.out.println("Scroll names for userID " + "123" + ": " + String.join(", ", scrollNames));
//            } else {
//                System.out.println("No scroll names found for userID: " + "123");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    -------------------------------DB init-------------------------------
    public static void init_db() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS User_scroll_list (\n"
                    + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                    + " userId TEXT NOT NULL UNIQUE,\n"
                    + " scrollName TEXT\n"
                    + ");";
            stmt.execute(createTableSQL);
        }
    }

    public static void insertScrollName(String userID, List<String> newScrollNames) throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Fetch existing ScrollNames
            String fetchSQL = "SELECT scrollName FROM User_scroll_list WHERE userId = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(fetchSQL);
            String existingScrolls = rs.next() ? rs.getString("scrollName") : null;

            // Merge existing ScrollNames with new ones
            List<String> mergedScrollNames;
            if (existingScrolls != null && !existingScrolls.isEmpty()) {
                mergedScrollNames = Arrays.stream(existingScrolls.split(",")).collect(Collectors.toList());
                mergedScrollNames.addAll(newScrollNames);
            } else {
                mergedScrollNames = newScrollNames;
            }
            String updatedScrollNames = String.join(", ", mergedScrollNames);

            // Insert or Update based on UserID
            String insertOrUpdateSQL = existingScrolls != null
                    ? "UPDATE User_scroll_list SET scrollName = '" + updatedScrollNames + "' WHERE userId = '" + userID + "'"
                    : "INSERT INTO User_scroll_list (userId, scrollName) VALUES ('" + userID + "', '" + updatedScrollNames + "')";

            stmt.execute(insertOrUpdateSQL);
        }
    }

    public static List<String> getScrollNamesByUserID(String userID) throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String fetchSQL = "SELECT scrollName FROM User_scroll_list WHERE userId = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(fetchSQL);
            String scrolls = rs.next() ? rs.getString("scrollName") : null;

            return scrolls != null ? Arrays.asList(scrolls.split(", ")) : null;
        }
    }
}

