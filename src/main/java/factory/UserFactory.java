package factory;

import model.User;

public class UserFactory {

    private UserFactory() {
    }

    public static User create(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }

}
