package model.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "member")
public class User {

    @EmbeddedId
    @Column(name = "user_id")
    private UserId userId;
    @Embedded
    private Password password;
    @Embedded
    private Name name;
    @Embedded
    private Email email;

    public User(UserId userId, Password password, Name name, Email email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId.getUserId();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    @Override
    public String toString() {
        return userId.toString();
    }
}
