package model;

public class UserAccount {
    private final int id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public UserAccount(UserAccountDTO userAccountDTO, int id) {
        this.userId = userAccountDTO.getUserId();
        this.password = userAccountDTO.getPassword();
        this.name = userAccountDTO.getName();
        this.email = userAccountDTO.getEmail();
        this.id = id;
    }

    public int getId() {
        return id;
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
}
