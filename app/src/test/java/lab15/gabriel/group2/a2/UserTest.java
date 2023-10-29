package lab15.gabriel.group2.a2;

import static org.junit.Assert.*;

import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Database.UserDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserTest {
    String scrollId = "100";
    String scrollName = "scroll_01";
    String CURRENT_DIR = System.getProperty("user.dir");
    String scrollFilePath = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName;
    String userid = "101";
    String scrollType = "private";
    String scrollPassword = "scroll_01";
    String uploadDate = "2023-07-08 23:59:59";
    String scrollContent = "1010111111\n11111111\n00000000000\n";
    String downloadNumber = "0";

    String phone_number = "123";
    String email_address = "anthony@gmail.com";
    String full_name = "123";

    String userId_user = "123";

    String username = "user2";

    String userPassword = "123";
    String userType = "general user";
    User userTest = new User(phone_number,email_address,full_name,userId_user,username,userPassword,userType);
    Scroll scrollTest = new Scroll(scrollId, scrollName, scrollFilePath, userid, scrollType, scrollPassword, uploadDate, scrollContent, downloadNumber);

    @BeforeEach
    public void setup(){
        try {
            UserDB.init_db();
            UserDB.insert_data(userTest);
            ScrollDB.insert_data_Scroll(scrollTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createUser(){
        assertNotNull(userTest);
        assertEquals(userTest.getPhone_number(), "123");
        assertEquals(userTest.getEmail_address(), "anthony@gmail.com");
        assertEquals(userTest.getFull_name(), "123");
        assertEquals(userTest.getUserId(),"123");
        assertEquals(userTest.getUsername(), "user2");
        assertEquals(userTest.getPassword(),"123");
        assertEquals(userTest.getUserType(), "general user");
    }
    @Test
    public void userMethodTest(){
        userTest.setPhone_number("456");
        assertEquals(userTest.getPhone_number(),"456");
        userTest.setEmail_address("anthony7e4@gmail.com");
        assertEquals("anthony7e4@gmail.com", userTest.getEmail_address());
        userTest.setFull_name("CL");
        assertEquals("CL", userTest.getFull_name());
        userTest.setUserId("456");
        assertEquals("456", userTest.getUserId());
        userTest.setUsername("user3");
        assertEquals("user3", userTest.getUsername());
        userTest.setUserType("admin user");
        assertEquals("admin user", userTest.getUserType());
        userTest.setPassword("456");
        assertEquals("456", userTest.getPassword());
        userTest.setUserTrackNumber("1");
        assertEquals("1",userTest.getUserTrackNumber());
    }
    @Test
    public void addScrollToUserTest(){
        userTest.addScrollToUser(scrollTest);
        assertNotNull(userTest.getScrollList());
    }

    @Test
    public void deleteScrollToUserTest(){
        userTest.deleteScrollToUser(scrollTest);
        assertEquals(0,userTest.getScrollList().size());
    }
    @Test
    public void userToStringTest(){
        assertEquals("User{" +
                "phone_number='" + phone_number + '\'' +
                ", email_address='" + email_address + '\'' +
                ", full_name='" + full_name + '\'' +
                ", userId='" + userId_user + '\'' +
                ", username='" + username + '\'' +
                ", password='" + userPassword + '\'' +
                ", userType='" + userType + '\'' +
                '}', userTest.toString());
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
