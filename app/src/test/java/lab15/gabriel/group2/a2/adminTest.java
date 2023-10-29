package lab15.gabriel.group2.a2;

import jdk.nashorn.internal.runtime.Scope;
import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import lab15.gabriel.group2.a2.Database.UserDB;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class adminTest {
    String scrollId = "100";
    String scrollName = "scroll_01";
    String CURRENT_DIR = System.getProperty("user.dir");
    String scrollFilePath = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName;
    String userid = "1";
    String scrollType = "private";
    String scrollPassword = "scroll_01";
    String uploadDate = "2023-07-08 23:59:59";
    String scrollContent = "1010111111\n11111111\n00000000000\n";
    String downloadNumber = "0";
    Admin admin = new Admin("1234567890", "admin@example.com", "Admin User", "A1", "adminUser", "password123", "admin");
    List<User> userList = new ArrayList<User>();
    User user1 = new User("1234567891", "user1@example.com", "User 1", "1", "userOne", "pass1", "general");
    User user2 = new User("1234567892", "user2@example.com", "User 2", "2", "userTwo", "pass2", "general");

    Scroll scrollTest = new Scroll(scrollId, scrollName, scrollFilePath, userid, scrollType, scrollPassword, uploadDate, scrollContent, downloadNumber);



    @BeforeEach
    public void setUp() {
        try {
            UserDB.init_db();
            ScrollDB.init_db();
            User_init.main(new String[]{});
            UserDB.insert_data(user1);
            UserDB.insert_data(user2);
            ScrollDB.insert_data_Scroll(scrollTest);
            userList.add(user1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testViewAllUsers() {
        // This method just prints the user details.
        // In a real scenario, you might want to capture the printed output and assert against it.
        // Here, we're just calling the method to ensure it doesn't throw any exceptions.
        assertDoesNotThrow(() -> admin.viewAllUsers(userList));
    }

    @Test
    public void testAddUser() {
        // Assuming UserDB.insert_data returns a success message when a user is added
        User user3 = new User("1234567892", "user3@example.com", "User 3", "3", "userThree", "pass2", "general");
        String result = admin.addUser(user3);
        assertEquals("Insertion successful for user with ID: 3", result); // Assuming "Success" is the message when a user is added.
    }

    @Test
    public void testDeleteUserByUserID() {
        //  UserDB.deleteUser_ByUserID returns a success message when a user is deleted

        String result = admin.deleteUserByUserID("1");
        assertEquals("Deleted successfully.", result); // Assuming "Success" is the message when a user is deleted.
    }

    @Test
    public void getAllScrollsDownloadCountTest(){
        Map<String, Integer> mapTest = new HashMap<>();
        mapTest.put("scroll_01", 0);
        assertEquals(mapTest, admin.getAllScrollsDownloadCount());
    }

    @Test
    public void testViewAllScrollsOfUsers() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Execute the method
        admin.viewAllScrollsOfUsers();

        // Reset the output stream back to normal
        System.setOut(originalOut);

        // Expected output
        String expectedOutput = "UserID: 1\n" +
                "Scrolls:\n" +
                "    - scroll_01\n" +
                "----------------------\n";//+ "UserID: 2\n" + "Scrolls:\n"+"    - "+"\n"+"----------------------\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void changeUserRoleTest() throws Exception {
        admin.changeUserRole("1","admin");
        int UserUniqueId = UserDB.getUserIdByUsername("1");
        System.out.println("UserUniqueId"+UserUniqueId);
        String result = UserDB.changeUserType(UserUniqueId, "admin");
        System.out.printf(result+"\n");
        assertEquals("admin", UserDB.getUserTypeById(UserUniqueId));
    }
    @Test
    public void viewAllUsersTest(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

       admin.viewAllUsers(userList);
//        User user1 = new User("1234567891",
//                "user1@example.com", "User 1",
//                "1", "userOne", "pass1", "general");

        // Reset the output stream back to normal
        System.setOut(originalOut);
        String expectedOutput = "----------------------\n"+"UserID: "+"1\n"+"Username: "+"userOne\n"+"Full Name: "+"User 1\n"+"Email Address: "+
                "user1@example.com\n"+"Phone Number: "+"1234567891\n"+"User Type: "+"general\n"+"----------------------\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    @AfterEach
    public void cleanup() {
        try {
            File dbFile = new File("src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db");
            File dbFile2 = new File("src/main/java/lab15/gabriel/group2/a2/Database/ScrollDB.db");
            if (dbFile.exists()) {
                dbFile.delete();
            }
            if(dbFile2.exists()){
                dbFile2.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}