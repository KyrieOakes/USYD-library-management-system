package lab15.gabriel.group2.a2.Login_Register;

import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.User;

public class Register {

    public static void main(String[] args) {

    }

    public static Boolean register(String phone_number, String email_address, String full_name, String userId, String username, String password) {
        try {
            User newUser = new User(phone_number, email_address, full_name, userId, username, password, "general");
            String register_result = UserDB.insert_data(newUser);

            if (("Insertion successful for user with ID: " + userId).equals(register_result)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


