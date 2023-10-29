package lab15.gabriel.group2.a2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lab15.gabriel.group2.a2.Database.User_Scroll_DB;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class User_Scroll_DBTEST {

        @BeforeEach
        public void setup() throws Exception {
            User_Scroll_DB.init_db();
        }

        @Test
        public void test_insert_new_scroll_name() throws Exception {
            User_Scroll_DB.insertScrollName("200", Arrays.asList("file1.txt", "file2.txt"));

            List<String> scrollNames = User_Scroll_DB.getScrollNamesByUserID("200");
            assertEquals(Arrays.asList("file1.txt", "file2.txt"), scrollNames);
        }

        @Test
        public void test_update_existing_scroll_name() throws Exception {
            User_Scroll_DB.insertScrollName("250", Arrays.asList("doc1.txt"));
            User_Scroll_DB.insertScrollName("250", Arrays.asList("doc2.txt"));

            List<String> scrollNames = User_Scroll_DB.getScrollNamesByUserID("250");
            assertEquals(Arrays.asList("doc1.txt", "doc2.txt"), scrollNames);
        }

        @Test
        public void test_get_scroll_names_by_userID() throws Exception {
            User_Scroll_DB.insertScrollName("300", Arrays.asList("note1.txt", "note2.txt"));

            List<String> fetchedNames = User_Scroll_DB.getScrollNamesByUserID("300");
            assertEquals(Arrays.asList("note1.txt", "note2.txt"), fetchedNames);
        }

        @Test
        public void test_get_nonexistent_scroll_names_by_userID() throws Exception {
            List<String> fetchedNames = User_Scroll_DB.getScrollNamesByUserID("999");
            assertNull(fetchedNames);
        }

        @Test
        public void test_insert_empty_scroll_name() throws Exception {
            User_Scroll_DB.insertScrollName("450", Arrays.asList(""));
            List<String> scrollNames = User_Scroll_DB.getScrollNamesByUserID("450");
            assertEquals(Arrays.asList(""), scrollNames);
        }

        @Test
        public void test_insert_long_scroll_name() throws Exception {
            StringBuilder builder = new StringBuilder(1000);
            for (int i = 0; i < 1000; i++) {
                builder.append("a");
            }
            String longScrollName = builder.toString() + ".txt";

            User_Scroll_DB.insertScrollName("500", Arrays.asList(longScrollName));

            List<String> scrollNames = User_Scroll_DB.getScrollNamesByUserID("500");
            assertEquals(Arrays.asList(longScrollName), scrollNames);
        }

        @Test
        public void test_insert_multiple_users_and_check_scroll_names() throws Exception {
            User_Scroll_DB.insertScrollName("600", Arrays.asList("file600_1.txt", "file600_2.txt"));
            User_Scroll_DB.insertScrollName("700", Arrays.asList("file700_1.txt"));
            User_Scroll_DB.insertScrollName("800", Arrays.asList("file800_1.txt", "file800_2.txt", "file800_3.txt"));

            assertEquals(Arrays.asList("file600_1.txt", "file600_2.txt"), User_Scroll_DB.getScrollNamesByUserID("600"));
            assertEquals(Arrays.asList("file700_1.txt"), User_Scroll_DB.getScrollNamesByUserID("700"));
            assertEquals(Arrays.asList("file800_1.txt", "file800_2.txt", "file800_3.txt"), User_Scroll_DB.getScrollNamesByUserID("800"));
        }
        @AfterEach
        public void cleanup() throws Exception {
            try {
                File dbFile = new File("src/main/java/lab15/gabriel/group2/a2/Database/User_scroll_list.db");
                if (dbFile.exists()) {
                    dbFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}
