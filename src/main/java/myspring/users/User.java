package myspring.users;

import org.jetbrains.annotations.NotNull;

public class User {

    private long seq;
    @NotNull
    private String id;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String email;

    public User (long seq, @NotNull String id, @NotNull String password, @NotNull String name, @NotNull String email) {
        this.seq = seq;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public long getSeq() {
        return seq;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getEmail() {
        return email;
    }

}
