package lab15.gabriel.group2.a2.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import lab15.gabriel.group2.a2.User;
import lab15.gabriel.group2.a2.Admin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String DB_PATH = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

//    -------------------------------Main----------------
    public static void main(String[] args) {
        try {
            init_db();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    -------------------------------DB init-------------------------------
    public static void init_db() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {


            // Create the table with an auto-incrementing Item_id
            String createTableSQL = "CREATE TABLE IF NOT EXISTS UserInfo (\n"
                    + " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
                    + " phone_number TEXT NOT NULL,\n"
                    + " full_name TEXT NOT NULL,\n"
                    + " email_address TEXT NOT NULL,\n"
                    + " username TEXT NOT NULL,\n"
                    + " password TEXT NOT NULL,\n"
                    + " userType TEXT NOT NULL,\n"
                    + " userId TEXT NOT NULL UNIQUE\n"
                    + ");";
            stmt.execute(createTableSQL);
        }
    }
    //    -------------------------------Insert data-------------------------------
    public static List<String> getAdminID() {
        List<String> adminIDs = new ArrayList<>();

        String query = "SELECT userId FROM UserInfo WHERE userType = 'admin'";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                adminIDs.add(rs.getString("userId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adminIDs;
    }





    public static String insert_data(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String insertSQL = user.toInsertSQL();
            stmt.execute(insertSQL);
            return "Insertion successful for user with ID: " + user.getUserId();

        } catch (java.sql.SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                return "Insertion failed: User ID '" + user.getUserId() + "' already exists.";
            } else {
                return "Insertion failed: " + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Insertion failed due to an unexpected error.";
        }
    }

    //    -------------------------------update the user info-------------------------------
    public static String updateUser(int ID, String phone_number, String full_name, String email_address,
                                    String username, String password, String userId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Step 1: Check if data in database is different from the provided data
            String selectSQL = "SELECT * FROM UserInfo WHERE ID = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setInt(1, ID);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    return "User with ID '" + ID + "' not found.";
                }

            }

            // Step 2: Update the user data
            String updateSQL = "UPDATE UserInfo SET phone_number = ?, email_address = ?, full_name = ?, " +
                    "username = ?, password = ?, userId = ? WHERE ID = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                updateStmt.setString(1, phone_number);
                updateStmt.setString(2, email_address);
                updateStmt.setString(3, full_name);
                updateStmt.setString(4, username);
                updateStmt.setString(5, password);
                updateStmt.setString(6, userId);
                updateStmt.setInt(7, ID);

                int updatedRows = updateStmt.executeUpdate();
                if (updatedRows > 0) {
                    return "User data updated successfully.";
                } else {
                    return "Failed to update user data.";
                }
            }
        } catch (java.sql.SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                return "update failed: User ID '" + userId + "' already exists.";
            } else {
                return "update failed: " + e.getMessage();
            }
        }
    }

    public static boolean doesUserIdExist(String userId) {
        String query = "SELECT 1 FROM UserInfo WHERE userId = ?"; // Use "SELECT 1" for efficiency
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, userId); // Set the userId parameter
            ResultSet rs = pstmt.executeQuery();
    
            // If the result set is not empty, the userId exists
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return false;
    }

    // get user Unique ID by user ID
    public static Integer getUserIdByUsername(String userId) {
        int unique_ID = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String selectSQL = "SELECT ID FROM UserInfo WHERE userId = ?";

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setString(1, userId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    unique_ID = rs.getInt("ID");
                }
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving user ID: " + e.getMessage());
        }

        return unique_ID;
    }


    //    -------------------------------match password-------------------------------
    public static String passwordCheck(String userId){
        String resultPassword = "None";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String checkPW = "SELECT password FROM UserInfo WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkPW);

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                resultPassword = rs.getString("password");
//                System.out.println("Password found: " + resultPassword);
            } else {
                System.out.println("No user found with userId: " + userId);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
            return resultPassword;
    }

    public static User user_detail(String userId){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            String selectUserSQL = "SELECT * FROM UserInfo WHERE userId = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectUserSQL)) {

                selectStmt.setString(1, userId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    String phone_number = rs.getString("phone_number");
                    String email_address = rs.getString("email_address");
                    String full_name = rs.getString("full_name");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String userType = rs.getString("userType");

                    if ("admin".equalsIgnoreCase(userType)) {
                        return new Admin(phone_number, email_address, full_name, userId, username, password, userType);
                    } else {
                        return new User(phone_number, email_address, full_name, userId, username, password, userType);
                    }
                } else {
                    return null; // or throw a new Exception("User not found");
                }

            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String return_users() {
        StringBuilder result = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String selectUsernamesSQL = "SELECT userId FROM UserInfo";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectUsernamesSQL)) {

                while (rs.next()) {
                    String username = rs.getString("userId");
                    result.append(username).append("\n"); // New line for next username
                }

                if (result.length() == 0) {
                    return "No users found.";
                } else {
                    return result.toString();
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                return "Error fetching userId: " + e.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error occurred.";
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String selectAllUsersSQL = "SELECT * FROM UserInfo";

            try (PreparedStatement selectStmt = conn.prepareStatement(selectAllUsersSQL);
                 ResultSet rs = selectStmt.executeQuery()) {

                while (rs.next()) {
                    String phone_number = rs.getString("phone_number");
                    String email_address = rs.getString("email_address");
                    String full_name = rs.getString("full_name");
                    String userId = rs.getString("userId");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String userType = rs.getString("userType");

                    User user = new User(phone_number, email_address, full_name, userId, username, password, userType);
                    users.add(user);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }


    public static String deleteUser_ByUserID(String userID) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String deleteSQL = "DELETE FROM UserInfo WHERE userId = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setString(1, userID);
                int deletedRows = deleteStmt.executeUpdate();
                if (deletedRows > 0) {
                    return "Deleted successfully.";
                } else {
                    return "Failed to delete user.";
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error occurred.";
        }
    }

    public static List<String> getAllUserIds() {
        List<String> userIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String selectAllUserIdsSQL = "SELECT userId FROM UserInfo";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectAllUserIdsSQL)) {

                while (rs.next()) {
                    String userId = rs.getString("userId");
                    userIds.add(userId);
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                // Optionally handle the error or rethrow as appropriate for your application
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally handle the error or rethrow as appropriate for your application
        }
        return userIds;
    }

    public static String getUsernameByUserId(String userId) {
        String query = "SELECT username FROM UserInfo WHERE userId = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, userId); // Set the userId parameter
            ResultSet rs = pstmt.executeQuery();
    
            // If the result set contains a record, return the username
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;  // Return null if the userId doesn't exist or if there's an error
    }

    public static String changeUserType(int ID, String userType) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Step 1: Check if data in database is different from the provided data
            String selectSQL = "SELECT * FROM UserInfo WHERE ID = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setInt(1, ID);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    return "User with ID '" + ID + "' not found.";
                }

            }

            // Step 2: Update the user data
            String updateSQL = "UPDATE UserInfo SET userType = ? WHERE ID = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                updateStmt.setString(1, userType);
                updateStmt.setInt(2, ID);

                int updatedRows = updateStmt.executeUpdate();
                if (updatedRows > 0) {
                    return "User data updated successfully.";
                } else {
                    return "Failed to update user data.";
                }
            }
        } catch (java.sql.SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                return "update failed: User ID '" + ID + "' already exists.";
            } else {
                return "update failed: " + e.getMessage();
            }
        }
    }
    public static String getUserTypeById(int id) throws Exception {
        String userType = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT userType FROM UserInfo WHERE ID = ?")) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userType = rs.getString("userType");
            }
        }

        return userType;
    }
}
