package lab15.gabriel.group2.a2;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import org.junit.jupiter.api.Test;
import lab15.gabriel.group2.a2.Database.UserDB;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserDB_Test {

    @Test
    public void testMainFunctionForCoverage() {
        UserDB userDB = new UserDB();
        UserDB.main(new String[] {});
    }

    @Test
    public void insert_data() throws Exception {
        try {
            UserDB.init_db();
            User userTest = new User("123", "anthony@gmail.com", "123", "123", "user2", "123", "general user");
            String response = UserDB.insert_data(userTest);
            assertEquals("Insertion successful for user with ID: 123", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void password_check() throws Exception {
        try {
            UserDB.init_db();
            User userTest = new User("321", "test@gmail.com", "321", "456", "testuser", "password123", "general user");
            UserDB.insert_data(userTest);

            String password = UserDB.passwordCheck("456");
            assertEquals("password123", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get_all_users() throws Exception {
        try {
            UserDB.init_db();
            User user1 = new User("555", "user1@gmail.com", "user1", "555", "user1name", "pass1", "general user");
            User user2 = new User("666", "user2@gmail.com", "user2", "666", "user2name", "pass2", "general user");
            UserDB.insert_data(user1);
            UserDB.insert_data(user2);

            assertEquals(2, UserDB.getAllUsers().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete_user() throws Exception {
        try {
            UserDB.init_db();
            User userTest = new User("999", "deleteMe@gmail.com", "999", "999", "deleteMe", "passToDelete", "general user");
            UserDB.insert_data(userTest);

            String deleteResponse = UserDB.deleteUser_ByUserID("999");
            assertEquals("Deleted successfully.", deleteResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void user_detail_test() throws Exception {
        try {
            UserDB.init_db();
            User userTest = new User("444", "detailTest@gmail.com", "444", "444", "detailTest", "detailPass", "general user");
            UserDB.insert_data(userTest);

            User fetchedUser = UserDB.user_detail("444");
            assertEquals("444", fetchedUser.getUserId());
            assertEquals("detailTest", fetchedUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void return_users_test() throws Exception {
        try {
            UserDB.init_db();
            User user1 = new User("100", "return1@gmail.com", "100", "100", "returnUser1", "returnPass1", "general user");
            User user2 = new User("200", "return2@gmail.com", "200", "200", "returnUser2", "returnPass2", "general user");
            UserDB.insert_data(user1);
            UserDB.insert_data(user2);

            String users = UserDB.return_users();
            assertTrue(users.contains("100"));
            assertTrue(users.contains("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get_all_user_ids_test() throws Exception {
        try {
            UserDB.init_db();
            User user1 = new User("111", "id1@gmail.com", "111", "111", "idUser1", "idPass1", "general user");
            User user2 = new User("222", "id2@gmail.com", "222", "222", "idUser2", "idPass2", "general user");
            UserDB.insert_data(user1);
            UserDB.insert_data(user2);

            List<String> userIds = UserDB.getAllUserIds();
            assertTrue(userIds.contains("111"));
            assertTrue(userIds.contains("222"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query_nonexistent_user_detail() throws Exception {
        try {
            UserDB.init_db();
            User fetchedUser = UserDB.user_detail("600");
            assertNull(fetchedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_duplicate_userID_test() throws Exception {
        try {
            UserDB.init_db();
            User user1 = new User("700", "dup1@gmail.com", "700", "700", "dupUser1", "dupPass1", "general user");
            UserDB.insert_data(user1);

            User user2 = new User("700", "dup2@gmail.com", "700", "700", "dupUser2", "dupPass2", "general user");
            String response = UserDB.insert_data(user2);
            assertEquals("Insertion failed: User ID '700' already exists.",response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get_admin_ID(){
        List<String> adminIDs = UserDB.getAdminID();
    }

    @Test
    public void getUserIdByUsername(){
        int result = UserDB.getUserIdByUsername("123");
        assertEquals(0,result);
    }

    @Test
    public void doesUserIdExist(){
        Boolean rs = UserDB.doesUserIdExist("123");
        assertEquals(false,rs);
    }

    @Test
    public void updateUser() throws Exception {
        UserDB.init_db();
        String rs = UserDB.updateUser(1,"123","fsd","fs@gami.com",
                "fas","123","123");
        assertEquals("User with ID '1' not found.",rs);

    }

    @Test
    public void getUsernameByUserId() throws Exception {
        UserDB.init_db();
        User_init.main(new String[] {});

        String name = UserDB.getUsernameByUserId("123");
        assertEquals("user1",name);
    }

    @Test
    public void changeUserType() throws Exception {
        UserDB.init_db();
        User_init.main(new String[] {});

        String result = UserDB.changeUserType(1,"admin");
        assertEquals("User data updated successfully.",result);

        String result2 = UserDB.changeUserType(99,"admin");
        assertEquals("User with ID '99' not found.", result2);
    }

    @AfterEach
    public void cleanup() {
        try {
            File dbFile = new File("src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db");
            if (dbFile.exists()) {
                dbFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
