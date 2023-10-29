package lab15.gabriel.group2.a2;

import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.Database.User_Scroll_DB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lab15.gabriel.group2.a2.Database.ScrollDB;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.sql.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Scroll_DB_test {

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize the database before each test
        ScrollDB.init_db();
        User_Scroll_DB.init_db();
        UserDB.init_db();
    }

    @AfterEach
    public void tearDown() {
        // Delete the database file after each test
        rmFile.main(new String[] {});
    }

    @Test
    public void testInsertDataScroll() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUser", "testType", "testPassword", "testDate", "testContent", "testDownloadNum");
        String result = ScrollDB.insert_data_Scroll(testScroll);
        assertEquals("Insertion successful for Scroll with Scroll ID: testId", result);
    }

    @Test
    public void testGetScrollsByUserId() {
        // Assuming we've inserted a Scroll in testInsertDataScroll with userId "testUser"
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUser", "testType", "testPassword", "testDate", "testContent", "testDownloadNum");
        ScrollDB.insert_data_Scroll(testScroll);

        List<Scroll> scrolls = ScrollDB.get_Scrolls_By_UserId("testUser");
        assertNotNull(scrolls);
        assertEquals(1, scrolls.size());
        assertEquals("testName", scrolls.get(0).getScrollName());
    }


    @Test
    public void testDuplicateScrollInsertion() {
        Scroll testScroll1 = new Scroll("testId", "testName", "testPath", "testUser", "testType", "testPassword", "testDate", "testContent", "testDownloadNum");
        ScrollDB.insert_data_Scroll(testScroll1);
        String result = ScrollDB.insert_data_Scroll(testScroll1);
        assertTrue(result.contains("Already exist"));
    }

    @Test
    public void testGetScrollNameAndContentByUserId() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUser", "testType", "testPassword", "testDate", "testContent", "testDownloadNum");
        ScrollDB.insert_data_Scroll(testScroll);

        List<String> results = ScrollDB.get_ScrollNameAndContent_By_UserId();
        assertNotNull(results);
        assertEquals(2, results.size()); // scrollName and content
        assertEquals("testName", results.get(0));
        assertEquals("testContent", results.get(1));
    }

    @Test
    public void testIn_itDb() throws Exception {
        File dbFile = new File("src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }

        assertFalse(dbFile.exists());

        ScrollDB.init_db();

        assertTrue(dbFile.exists());

    }
    @Test
    public void testReturnScrollByID() {
        // Given a scroll has been inserted
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUser", "testType", "testPassword", "testDate", "testContent", "testDownloadNum");
        ScrollDB.insert_data_Scroll(testScroll);

        // When we retrieve by ID
        Scroll result = ScrollDB.return_scroll_by_ID("testId");

        // Then the retrieved scroll should match the inserted one
        assertNotNull(result);
        assertEquals("testName", result.getScrollName());
    }


    @Test
    void testReturnScrollById() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        ScrollDB.insert_data_Scroll(testScroll);
        Scroll result = ScrollDB.return_scroll_by_ID("testId");
        assertNotNull(result);
        assertEquals("testName", result.getScrollName());
        // Further assertions can be added for all fields
    }

    @Test
    void tes_InsertDataScroll() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        String result = ScrollDB.insert_data_Scroll(testScroll);
        assertTrue(result.contains("Insertion successful"));
    }

    @Test
    void tes_tGetScrollsByUserId() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        ScrollDB.insert_data_Scroll(testScroll);
        List<Scroll> results = ScrollDB.get_Scrolls_By_UserId("testUserId");
        assertEquals(1, results.size());
        // Further assertions can be added based on the list's contents
    }

    @Test
    void testGetScrollName_AndContentByUserId() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        ScrollDB.insert_data_Scroll(testScroll);
        List<String> results = ScrollDB.get_ScrollNameAndContent_By_UserId();
        assertTrue(results.contains("testName"));
        assertTrue(results.contains("content"));
    }

    @Test
    void testSynchronization() {
        Scroll testScroll = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        ScrollDB.insert_data_Scroll(testScroll);
        Boolean result = ScrollDB.synchronization();
        assertTrue(result);

        File file = new File("src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/testName");
        assertTrue(!file.exists());

        // Cleanup after the test
        file.delete();
    }

    // Test for duplicate insert scenario
    @Test
    void testDuplicateInsertDataScroll() {
        Scroll testScroll1 = new Scroll("testId", "testName", "testPath", "testUserId", "private", "password", "date", "content", "0");
        Scroll testScroll2 = new Scroll("testId", "testName2", "testPath2", "testUserId2", "private", "password2", "date2", "content2", "1");

        String result1 = ScrollDB.insert_data_Scroll(testScroll1);
        assertTrue(result1.contains("Insertion successful"));

        String result2 = ScrollDB.insert_data_Scroll(testScroll2);
        assertTrue(result2.contains("Already exist"));
    }

    @Test
    void testInitDb() throws Exception {
        ScrollDB.init_db();
        try (Connection conn = DriverManager.getConnection(ScrollDB.DB_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Scroll';");
            assertTrue(rs.next());
        }
    }

    @Test
    public void testmain() {
        ScrollDB result = new ScrollDB();
        ScrollDB.main(new String[] {});

    }

    @Test
    public void testGetAllScrollsDownloadCount() {
        Map<String, Integer> downloadCounts = ScrollDB.getAllScrollsDownloadCount();
    }

    @Test
    public void updateRow() throws Exception {
        ScrollDB.updateRow("1","10101010");
    }

    @Test
    public void updateScrollUserId() throws Exception {
        ScrollDB.updateScrollUserId("123","321");
    }

    @Test
    public void getAllScrollsByUser() throws Exception {
        Map<String, List<String>> result = ScrollDB.getAllScrollsByUser();
    }


    @Test
    public void updateDownloadCount() throws Exception {
        ScrollDB.updateDownloadCount("1");
    }

    @Test
    public void deleteScrollByScrollId() throws Exception {
        ScrollDB.deleteScrollByScrollId("1","123");
    }

}

