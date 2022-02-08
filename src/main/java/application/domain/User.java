package application.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private final String name;
    private final String password;

    private final Set<User> friends = new HashSet<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void addFriend(User user){
        friends.add(user);
    }

    public boolean validate(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
