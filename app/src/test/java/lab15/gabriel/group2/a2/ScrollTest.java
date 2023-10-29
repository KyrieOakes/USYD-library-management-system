package lab15.gabriel.group2.a2;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;
import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ScrollTest {
    String scrollId = "10001";
    String scrollName = "newscroll";
    //String CURRENT_DIR = System.getProperty("user.dir");
    String scrollFilePath = "src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName;
    String userid = "101";
    String scrollType = "private";
    String scrollPassword = "scroll_01";
    String uploadDate = "2023-07-08 23:59:59";
    String scrollContent = "1010111111\n11111111\n";
    String downloadNumber = "0";
    Scroll scrollTest = new Scroll(scrollId, scrollName, scrollFilePath, userid, scrollType, scrollPassword, uploadDate, scrollContent, downloadNumber);

    User new_user = new User("123","anthony@gmail.com", "AL", "101", "AL", "102","general user");

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream inContent;

    public ScrollTest(ByteArrayInputStream inContent) {
        this.inContent = inContent;
    }

    @BeforeEach
    public void setup(){
        try {
            UserDB.init_db();
            ScrollDB.init_db();
            User_init.main(new String[]{});
            UserDB.insert_data(new_user);
            ScrollDB.insert_data_Scroll(scrollTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scrollMethodTest(){
        assertNotNull(scrollTest);
        scrollTest.setScrollId("102");
        assertEquals(scrollTest.getScrollId(), "102");
        //scrollTest.setScrollId("101");
        scrollTest.setScrollName("scroll_02.txt");
        assertEquals(scrollTest.getScrollName(),"scroll_02.txt");
        //scrollTest.setScrollName("scroll_01.txt");
        scrollTest.setScrollPassword("102");
        assertEquals(scrollTest.getScrollPassword(), "102");
        //scrollTest.setScrollPassword("scroll_01");
        scrollTest.setType("private");
        assertEquals(scrollTest.getType(),"private");
        scrollTest.setScrollFilePath("src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/scroll_02.txt");
        assertEquals(scrollTest.getScrollFilePath(),"src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/scroll_02.txt");
        //scrollTest.setScrollFilePath(CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/scroll_01.txt");
        scrollTest.setUploadDate("2024-10-10 23:59:59");
        assertEquals(scrollTest.getUploadDate(),"2024-10-10 23:59:59");
        //scrollTest.setUploadDate("2023-07-08 23:59:59");
        scrollTest.setUserId("102");
        assertEquals(scrollTest.getUserId(),"102");
        //scrollTest.setUserId("101");
    }

    @Test
    public void addNewScrollTest() throws IOException {
        Scroll.addNewScroll(scrollId, scrollName, userid, scrollType, scrollPassword, scrollContent,downloadNumber);

        System.out.printf(scrollFilePath+"\n");
//         read content from the file
        String actualContent = readContentFromFile(scrollFilePath)+"\n";

//         compare the actual content with the expected content
        assertEquals(scrollContent, actualContent);
    }




    @Test
    public void downloadScrollTest() throws Exception {
        ScrollDB.main(new String[]{});
        UserDB.init_db();
        User_init.main(new String[]{});

        User new_user = new User("123","2342@gmail.com","rujie",
                "4399","ruijie","4399","general");
        UserDB.insert_data(new_user);
        Scroll.addNewScroll("20","20","4399","public","4399",
                "0101010","2");


        Boolean rs = Scroll.downloadScroll("20",new_user.getUserId());




//        User new_user1 = new User("123","2342@gmail.com","rujie",
//                "7777","ruijie","7777","general");
//        UserDB.insert_data(new_user1);
//        Scroll.addNewScroll("9678","Chris","7777","private","7777",
//                "0101010","2");
//
//        Boolean kk = Scroll.downloadScroll("9678",new_user1.getUserId());
//        User new_user1 = new User("123","2342@gmail.com","rujie",
//                "7777","ruijie","7777","general");
//        UserDB.insert_data(new_user1);
//        Scroll.addNewScroll("9678","Chris","7777","private","7777",
//                "0101010","2");
//
//        Boolean kk = Scroll.downloadScroll("9678",new_user1.getUserId());
//        assertTrue("Expected the second scroll download to be successful!", kk);

    }

    public String readContentFromFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .collect(Collectors.joining("\n"));
    }

    @Test
    public void test_setFilterCheck(){
        Scroll.setFilterCheck();
        Scroll.getFilterCheck();
        Scroll.getFilteredResult();
    }

    @Test
    public void removeScrollBy_scrollNameTest(){
        Boolean result = Scroll.removeScrollBy_scrollName(new_user, scrollTest);
        assertEquals(true, result);
    }

    @AfterEach
    public void cleanup() {
        try {
            //ScrollDB.deleteScrollByScrollId(scrollId, userid);
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
