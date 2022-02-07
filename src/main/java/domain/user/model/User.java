package domain.user.model;

import lombok.Builder;

public class User {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    @Builder
    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
