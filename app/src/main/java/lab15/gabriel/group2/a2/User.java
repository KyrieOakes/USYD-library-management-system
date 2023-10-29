package lab15.gabriel.group2.a2;

import java.util.List;
import java.util.ArrayList;
import java.util.*;

public class User {
    private String userTrackNumber; //This number is unique, used to track different users in the database
    private String phone_number;
    private String email_address;
    private String full_name;
    private String userId;// customisable ID keys, no 2 accounts can have the same ID key.
    private String username;
    private String password;

    private String userType; // general user/admin user/guest

    private List<Scroll> scrollList = new ArrayList<>();


    public User(String phone_number, String email_address, String full_name, String userId, String username, String password, String userType) {
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.full_name = full_name;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.scrollList = new ArrayList<>();
    }

    public List<Scroll> getScrollList() {
        return scrollList;
    }

    public void addScrollToUser(Scroll newScroll){
        this.scrollList.add(newScroll);
    }

    public void deleteScrollToUser(Scroll scrollToDelete){
        this.scrollList.remove(scrollToDelete);
    }

    public String getPhone_number(){
        return this.phone_number;
    }

    public void setPhone_number(String pn){
        this.phone_number = pn;
    }

    public String getEmail_address(){
        return this.email_address;
    }

    public void setEmail_address(String email_address){
        this.email_address = email_address;
    }

    public String getUserType(){
        return this.userType;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getFull_name(){
        return this.full_name;
    }

    public void setFull_name(String full_name){
        this.full_name = full_name;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone_number='" + phone_number + '\'' +
                ", email_address='" + email_address + '\'' +
                ", full_name='" + full_name + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    public String toInsertSQL() {
        return "INSERT INTO UserInfo (phone_number, full_name, email_address, username, password, userType, userId) VALUES (" +
                "'" + phone_number + "', " +
                "'" + full_name + "', " +
                "'" + email_address + "', " +
                "'" + username + "', " +
                "'" + password + "', " +
                "'" + userType + "', " +
                "'" + userId + "');";
    }


    public void setUserType(String userType){
        this.userType = userType;
    }


    public String getUserTrackNumber() {
        return userTrackNumber;
    }

    public void setUserTrackNumber(String userTrackNumber) {
        this.userTrackNumber = userTrackNumber;
    }
}
