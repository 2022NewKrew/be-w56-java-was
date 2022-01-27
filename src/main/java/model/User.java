package model;

import com.google.common.base.Strings;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    private User(String userId, String password, String name, String email) {
        validateUserInfo(userId, password, name, email);

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private static void validateUserInfo(String userId, String password, String name, String email) {
        if(Strings.isNullOrEmpty(userId) ||
           Strings.isNullOrEmpty(password) ||
           Strings.isNullOrEmpty(name) ||
           Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("회원 정보가 적절하지 않습니다.");
        }
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

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public static class Builder {
        private String userId;
        private String password;
        private String name;
        private String email;

        private Builder() {}

        public Builder userId(String userId) {
            this.userId = userId;

            return this;
        }

        public Builder password(String password) {
            this.password = password;

            return this;
        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        public Builder email(String email) {
            this.email = email;

            return this;
        }

        public User build() {
            return new User(
                    this.userId,
                    this.password,
                    this.name,
                    this.email
            );
        }
    }
}
