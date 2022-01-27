package model;

import exception.InvalidUserInputException;

public class User {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        validateNotNull(userId, password, name, email);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validateNotNull(String userId, String password, String name, String email) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserInputException("아이디는 빈 칸일 수 없습니다.");
        }
        if (password == null || password.isEmpty()) {
            throw new InvalidUserInputException("비밀번호는 빈 칸일 수 없습니다.");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidUserInputException("이름은 빈 칸일 수 없습니다.");
        }
        if (email == null || email.isEmpty()) {
            throw new InvalidUserInputException("이메일은 빈 칸일 수 없습니다.");
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

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
