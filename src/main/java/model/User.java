package model;

public class User {
    private final String REGEX_USER_ID = "^\\w+$";
    private final String REGEX_PASSWORD = "^\\w+$";
    private final String REGEX_NAME = "^\\w+$";
    private final String REGEX_EMAIL = "^[\\s\\S]+$";

    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) throws Exception {
        if(!checkRegexOfUser(userId,password,name,email)) {
            throw new Exception();
        }
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private boolean checkRegexOfUser (String userId, String password, String name, String email) {
        return checkRegexOfString(userId, REGEX_USER_ID)
                && checkRegexOfString(password, REGEX_PASSWORD)
                && checkRegexOfString(name, REGEX_NAME)
                && checkRegexOfString(email, REGEX_EMAIL);

    }
    private boolean checkRegexOfString(String str, String regex) {
        return str != null && str.matches(regex);
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

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
