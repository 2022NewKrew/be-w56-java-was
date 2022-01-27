package application.model;

import application.dto.UserLoginRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static application.common.ExceptionMessage.*;

@Getter
@AllArgsConstructor
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public static User valueOf(String userId, String password, String name, String email) {
        validateUserId(userId);
        validatePassword(password);
        validateName(name);
        validateEmail(email);
        return new User(userId, password, name, email);
    }

    private static void validateUserId(String userId) throws IllegalArgumentException {
        if (userId == null || userId.trim().length() == 0) {
            throw new IllegalArgumentException(VALUE_LENGTH_LOWERBOUND_EXCEPTION.getMessage() + " Reason: userId");
        }

        if (userId.length() > USER_ID_LENGTH_UPPERBOUND) {
            throw new IllegalArgumentException(USER_ID_LENGTH_UPPERBOUND_EXCEPTION.getMessage());
        }
    }

    private static void validatePassword(String password) throws IllegalArgumentException {
        if (password == null || password.trim().length() == 0) {
            throw new IllegalArgumentException(VALUE_LENGTH_LOWERBOUND_EXCEPTION.getMessage() + " Reason: password");
        }
        if (password.length() > PASSWORD_LENGTH_UPPERBOUND) {
            throw new IllegalArgumentException(PASSWORD_LENGTH_UPPERBOUND_EXCEPTION.getMessage());
        }
    }

    private static void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException(VALUE_LENGTH_LOWERBOUND_EXCEPTION.getMessage() + " Reason: name");
        }
        if (name.trim().length() > NAME_LENGTH_UPPERBOUND) {
            throw new IllegalArgumentException(NAME_LENGTH_UPPERBOUND_EXCEPTION.getMessage());
        }
    }

    private static void validateEmail(String email) throws IllegalArgumentException {
        String regex = "[a-z0-9]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL_FORM_EXCEPTION.getMessage());
        }
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    private static final int USER_ID_LENGTH_UPPERBOUND = 20;
    private static final int PASSWORD_LENGTH_UPPERBOUND = 20;
    private static final int NAME_LENGTH_UPPERBOUND = 10;

    public void verifyPassword(UserLoginRequest request) throws IllegalArgumentException {
        if (!Objects.equals(password, request.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_VERIFY_FAILED_EXCEPTION.getMessage());
        }
    }
}
