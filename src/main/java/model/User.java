package model;

import exception.PasswordMissMatchException;

public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public void validateEqualsPassword(String inputPassword) throws PasswordMissMatchException {
        if (!equalsPassword(inputPassword)) {
            throw new PasswordMissMatchException("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean equalsPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }


}
