package model;

import java.util.Map;

public class User implements Model {
    final static String USER_ID = "userId";
    final static String PW = "password";
    final static String NAME = "name";
    final static String EMAIL = "email";


    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email)  {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> userMap){
        this.userId = userMap.get("userId");
        this.password = userMap.get("password");
        this.name = userMap.get("name");
        this.email = userMap.get("email");
    }

    @Override
    public String getParam(String param) throws IllegalArgumentException{
        if(param.equals(USER_ID)){
            return getUserId();
        }
        if(param.equals(PW)){
            return getPassword();
        }
        if(param.equals(NAME)){
            return getName();
        }
        if(param.equals(EMAIL)){
            return getEmail();
        }
        throw new IllegalArgumentException("No parameter :"+param+"is in the Model !");

    }



    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean validateUser(String upw){
        return password.equals(upw);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
