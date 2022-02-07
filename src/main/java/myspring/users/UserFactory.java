package myspring.users;

public class UserFactory {

    public static User createUser(String id, String password) {
        return new User(0, id, password, "", "");
    }

    public static User createUser(String id, String password, String name, String email) {
        return new User(0, id, password, name, email);
    }

    public static User createUser(long seq, String id, String password, String name, String email) {
        return new User(seq, id, password, name, email);
    }

}
