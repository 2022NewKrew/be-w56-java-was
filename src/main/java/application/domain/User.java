package application.domain;

public class User {
    private final String name;
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean validate(String password) {
        return this.password.equals(password);
    }
}
