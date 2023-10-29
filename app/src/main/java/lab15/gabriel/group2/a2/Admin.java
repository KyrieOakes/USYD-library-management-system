package lab15.gabriel.group2.a2;

import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.Database.ScrollDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


public class Admin extends User {

    public Admin(String phone_number, String email_address, String full_name, String userId, String username, String password, String userType) {
        super(phone_number, email_address, full_name, userId, username, password, userType);
    }

    // View all users => use the method which have written in the UserDB.java (getAllUsers())
    public void viewAllUsers(List<User> allUsers) { // allUsers => UserDB.getAllUsers()
        for (User user : allUsers) {
            System.out.println("----------------------");
            System.out.println("UserID: " + user.getUserId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Full Name: " + user.getFull_name());
            System.out.println("Email Address: " + user.getEmail_address());
            System.out.println("Phone Number: " + user.getPhone_number());
            System.out.println("User Type: " + user.getUserType());
            System.out.println("----------------------");
        }
    }

    // Add a new user in the system
    public String addUser(User user) { // when add a new person, we need to construct the user entirely, for example each profile information
        return UserDB.insert_data(user);
    }

    // Delete a user by userID
    public String deleteUserByUserID(String userID) {
        return UserDB.deleteUser_ByUserID(userID);
    }

    public Map<String, Integer> getAllScrollsDownloadCount() {
        return ScrollDB.getAllScrollsDownloadCount();
    }

    public void viewAllScrollsOfUsers() {
        Map<String, List<String>> userScrolls = ScrollDB.getAllScrollsByUser();

        for (Map.Entry<String, List<String>> entry : userScrolls.entrySet()) {
            System.out.println("UserID: " + entry.getKey());
            System.out.println("Scrolls:");
            for (String scrollName : entry.getValue()) {
                System.out.println("    - " + scrollName);
            }
            System.out.println("----------------------");
        }
    }

    public void changeUserRole(String userId, String userType){
        User current_user = UserDB.user_detail(userId);
        current_user.setUserType(userType);
        int UserUniqueId = UserDB.getUserIdByUsername(userId);
        UserDB.changeUserType(UserUniqueId, userType);
    }


}