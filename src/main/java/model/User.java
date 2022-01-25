package model;

import util.Checker;
import util.SecurePassword;

import java.util.Objects;

public class User {
    public static final User NONE = new User();
    public static final String ID_REGEX = "[0-9a-z]+";
    public static final int ID_MIN = 6;
    public static final int ID_MAX = 12;
    public static final String  PW_REGEX = "[0-9a-zA-Z]+";
    public static final int PW_MIN = 8;
    public static final int PW_MAX = 18;
    public static final String NAME_REGEX = ".+";
    public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 32;
    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final int EMAIL_MIN = 7;
    public static final int EMAIL_MAX = 127;

    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public User(
            final String id,
            final String password,
            final String name,
            final String email
    ) throws IllegalArgumentException
    {
        validate(id, password, name, email);
        this.id = id.trim();
        this.password = SecurePassword.genPassword(password);
        this.name = name.trim();
        this.email = email.trim();
    }

    private User() {
        this.id = "none";
        this.password = "";
        this.name = "비회원";
        this.email = "";
    }

    private void validate(
            final String id,
            final String password,
            final String name,
            final String email
    )
    {
        Checker.checkString("id", id, ID_REGEX, ID_MIN, ID_MAX);
        Checker.checkString("password", password, PW_REGEX, PW_MIN, PW_MAX);
        Checker.checkString("name", name, NAME_REGEX, NAME_MIN, NAME_MAX);
        Checker.checkString("email", email, EMAIL_REGEX, EMAIL_MIN, EMAIL_MAX);
    }

    public String getId() {
        return id;
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

    public boolean isNone() {
        return this.equals(NONE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && password.equals(user.password) && name.equals(user.name) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, name, email);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
