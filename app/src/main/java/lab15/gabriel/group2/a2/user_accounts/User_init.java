package lab15.gabriel.group2.a2.user_accounts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.User;
import lab15.gabriel.group2.a2.UserUI;
import lab15.gabriel.group2.a2.Admin;

public class User_init {

    public static void main(String[] args) {
        String username = "user1";
        String password = "password1";
        String AdminPwd = "adminPwd";
        String encryptedPassword_admin = encryptPassword(AdminPwd);
        String encryptedPassword = encryptPassword(password);
        User user1 = new User("123","123","123","123",username, encryptedPassword,"general");
        User user2 = new User("123","123","123","1",username, encryptedPassword,"general");
        Admin admin1 = new Admin("13330003333", "admin1@gmail.com", "IAMADMIN", "20", "admin1", encryptedPassword_admin, "admin");

        String databaseResult = UserDB.insert_data(user1);
        String databaseResult3 = UserDB.insert_data(user2);
        String databaseResult2 = UserDB.insert_data(admin1);
    }

    public static String encryptPassword(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for(byte b: hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
}
