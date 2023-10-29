package lab15.gabriel.group2.a2.Database;

import lab15.gabriel.group2.a2.User;
import lab15.gabriel.group2.a2.Scroll;

import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ScrollDB {
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final String DB_PATH = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static final String CURRENT_DIR_user = System.getProperty("user.dir");
    public static final String DB_PATH_user = CURRENT_DIR_user + "/src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db";
    public static final String DB_URL_user = "jdbc:sqlite:" + DB_PATH_user;

//    ------------------Main--------------------
    public static void main(String[] args) {
        try {
            init_db();


            Boolean re = Scroll.addNewScroll("1","first one","123", "private","123","1010101010", "2");
            Boolean re2 = Scroll.addNewScroll("2","second one","1", "private","123","1010101010","2");
            Boolean re3 = Scroll.addNewScroll("3","third one","20", "private","123","1010101010","2");
//            deleteScrollByScrollId("1","123");


        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    ------------------init Db--------------------
        public static void init_db () throws Exception {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {

                String createTableSQL = "CREATE TABLE IF NOT EXISTS Scroll (\n"
                        + " scrollName TEXT PRIMARY KEY ,\n"
                        + " scrollId TEXT NOT NULL,\n"
                        + " scrollFilePath TEXT NOT NULL,\n"
                        + " userId TEXT NOT NULL,\n"
                        + " type TEXT NOT NULL,\n"
                        + " scrollPassword TEXT NOT NULL,\n"
                        + " uploadDate TEXT NOT NULL,\n"
                        + " content TEXT, \n"
                        + " downloadNum TEXT NOT NULL\n"
                        + ");";

                stmt.execute(createTableSQL);
            }
        }

        public static Scroll return_scroll_by_ID(String ID){
            try(Connection conn = DriverManager.getConnection(DB_URL)){
                String sql_command = "SELECT * FROM Scroll WHERE scrollId = ?";
                try(PreparedStatement selectStmt = conn.prepareStatement(sql_command)){
                    selectStmt.setString(1,ID);
                    ResultSet rs = selectStmt.executeQuery();

                    while (rs.next()) {
                        Scroll scroll = new Scroll(
                                rs.getString("scrollId"),
                                rs.getString("scrollName"),
                                rs.getString("scrollFilePath"),
                                rs.getString("userId"),
                                rs.getString("type"),
                                rs.getString("scrollPassword"),
                                rs.getString("uploadDate"),
                                rs.getString("content"),  // 新增的content参数downloadNum
                                rs.getString("downloadNum")
                        );
                        return (scroll);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        public static String insert_data_Scroll (Scroll NewOne){
            try (Connection conn = DriverManager.getConnection(DB_URL)) {

                // Check if record with the same scrollName already exists
                String selectSQL = "SELECT scrollName FROM Scroll WHERE scrollName = ?";
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                    selectStmt.setString(1, NewOne.getScrollName());
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        return "Already exist file named: " + NewOne.getScrollName();
                    }
                }

                // Check if record with the same scrollName already exists
                String sl = "SELECT scrollId FROM Scroll WHERE scrollId = ?";
                try (PreparedStatement selectStmt = conn.prepareStatement(sl)) {
                    selectStmt.setString(1, NewOne.getScrollId());
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        return "Already exist scrollId : " + NewOne.getScrollId();
                    }
                }

                // Insert new record
                String insertSQL = "INSERT INTO Scroll (scrollId, scrollName, scrollFilePath, userId, type, scrollPassword, uploadDate, content, downloadNum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    insertStmt.setString(1, NewOne.getScrollId());
                    insertStmt.setString(2, NewOne.getScrollName());
                    insertStmt.setString(3, NewOne.getScrollFilePath());
                    insertStmt.setString(4, NewOne.getUserId());
                    insertStmt.setString(5, NewOne.getType());
                    insertStmt.setString(6, NewOne.getScrollPassword());
                    insertStmt.setString(7, NewOne.getUploadDate());
                    insertStmt.setString(8, NewOne.getContent());  // 新增的content参数
                    insertStmt.setString(9, NewOne.getDownloadNum());  // 新增的content参数
                    insertStmt.executeUpdate();
                }
                return "Insertion successful for Scroll with Scroll ID: " + NewOne.getScrollId();

            } catch (SQLException e) {
                e.printStackTrace();
                return "Insertion failed due to an error: " + e.getMessage();
            }
        }



        public static List<Scroll> get_Scrolls_By_UserId (String userId){

            List<Scroll> scrolls = new ArrayList<>();
            String query = "SELECT * FROM Scroll WHERE userId = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Scroll scroll = new Scroll(
                            rs.getString("scrollId"),
                            rs.getString("scrollName"),
                            rs.getString("scrollFilePath"),
                            rs.getString("userId"),
                            rs.getString("type"),
                            rs.getString("scrollPassword"),
                            rs.getString("uploadDate"),
                            rs.getString("content"),  // 新增的content参数downloadNum
                            rs.getString("downloadNum")
                    );
                    scrolls.add(scroll);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return scrolls;
        }

    public static List<String> get_ScrollNameAndContent_By_UserId() {
        List<String> results = new ArrayList<>();

        String query = "SELECT scrollName, content FROM Scroll";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("scrollName"));
                results.add(rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static Boolean synchronization() {

        List<String> results = new ArrayList<>();
        String scrollFilePath = "src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/";

        File directory = new File(scrollFilePath);
        if (!directory.exists()) {
            directory.mkdirs(); // This will create the directory if it doesn't exist
        }


        String query = "SELECT scrollName, content FROM Scroll";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                while (rs.next()) {
                    String scrollName = rs.getString("scrollName");
                    String scrollContent = rs.getString("content");

                    File file = new File(scrollFilePath + scrollName);
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write(scrollContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false; // Return false if there's an issue writing the file
                    }

//                    System.out.print("Scroll added successfully!\n");
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there's an SQL exception
        }
    }

    public static void updateRow(String scrollId, String content) throws Exception {
        String updateSQL = "UPDATE Scroll SET content = ? WHERE scrollId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);

             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {


            pstmt.setString(1, content);
            pstmt.setString(2, scrollId);
            pstmt.executeUpdate();
        }
    }

    public static Map<String, Integer> getAllScrollsDownloadCount() {
        Map<String, Integer> result = new HashMap<>();

        String query = "SELECT scrollName, downloadNum FROM Scroll";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String scrollName = rs.getString("scrollName");
                int downloadNum = rs.getInt("downloadNum");  //
                result.put(scrollName, downloadNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map<String, List<String>> getAllScrollsByUser() {
        Map<String, List<String>> userScrolls = new HashMap<>();

        String query = "SELECT userId, scrollName FROM Scroll";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String userId = rs.getString("userId");
                String scrollName = rs.getString("scrollName");

                userScrolls
                        .computeIfAbsent(userId, k -> new ArrayList<>())
                        .add(scrollName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userScrolls;
    }

    public static void updateScrollUserId(String oldUserId, String newUserId) throws SQLException {
        List<Scroll> scrolls = get_Scrolls_By_UserId(oldUserId);
        String updateSQL = "UPDATE Scroll SET userId = ? WHERE scrollId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            for (Scroll scroll : scrolls) {
                pstmt.setString(1, newUserId);
                pstmt.setString(2, scroll.getScrollId());  // Assuming Scroll class has a getScrollId method
                pstmt.executeUpdate();
            }
        }
    }

    public static void updateDownloadCount(String scrollId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Step 1: Fetch the current download count for the provided scrollId
            String selectSQL = "SELECT downloadNum FROM Scroll WHERE scrollId = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setString(1, scrollId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int currentDownloadCount = rs.getInt("downloadNum");

                    // Step 2: Update the download count
                    String updateSQL = "UPDATE Scroll SET downloadNum = ? WHERE scrollId = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setInt(1, currentDownloadCount + 1);  // Incrementing the download count
                        updateStmt.setString(2, scrollId);

                        int updatedRows = updateStmt.executeUpdate();
                        if (updatedRows > 0) {
                            System.out.println("Download count updated successfully.");
                        } else {
                            System.out.println("Failed to update download count.");
                        }
                    }
                } else {
                    System.out.println("Scroll with ID '" + scrollId + "' not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating download count: " + e.getMessage());
        }
    }

    public static boolean deleteScrollByScrollId(String scrollId, String userId) {
        String userType = null;

        // First, fetch the userType based on userId from UserInfo table
        String userTypeQuery = "SELECT userType FROM UserInfo WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL_user);
             PreparedStatement pstmt = conn.prepareStatement(userTypeQuery)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userType = rs.getString("userType");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (userType == null) {
            System.out.println("User not found.");
            return false;
        }

        // Check if userType is general
        if ("general".equals(userType)) {
            // Fetch the userId associated with the scrollId from Scroll table
            String scrollOwnerQuery = "SELECT userId FROM Scroll WHERE scrollId = ?";
            String scrollOwnerId = null;
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(scrollOwnerQuery)) {

                pstmt.setString(1, scrollId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    scrollOwnerId = rs.getString("userId");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            // If the provided userId does not match the scroll's owner userId, return with a message
            if (!userId.equals(scrollOwnerId)) {
                System.out.println("You are not the author and cannot delete this scroll.");
                return false;
            }
        }

        // Fetch scrollName based on scrollId
        String fetchQuery = "SELECT scrollName FROM Scroll WHERE scrollId = ?";
        String scrollName = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(fetchQuery)) {

            pstmt.setString(1, scrollId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                scrollName = rs.getString("scrollName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // If scrollName is not found, return false
        if (scrollName == null) {
            System.out.println("Scroll not found.");
            return false;
        }

        // Try to delete the file locally
        File fileToDelete = new File("src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName);
        if (fileToDelete.exists() && !fileToDelete.delete()) {
            // If the file exists but couldn't be deleted, return false
            System.out.println("Failed to delete the local file.");
            return false;
        }

        // Delete the record from the database
        String deleteQuery = "DELETE FROM Scroll WHERE scrollId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, scrollId);
            int rowsAffected = pstmt.executeUpdate();

            // If rowsAffected is more than 0, it means a record was deleted
            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



}