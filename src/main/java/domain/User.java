package domain;

import java.util.Map;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(Map<String, String> query){
        this.userId = query.get("userId");
        this.password = query.get("password");
        this.name = query.get("name");
        this.email = query.get("email");
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
