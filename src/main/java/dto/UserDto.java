package dto;

import domain.Email;
import domain.Name;
import domain.Password;
import domain.UserId;

import java.time.LocalDateTime;

public class UserDto {
    private UserId userId;
    private Password password;
    private Name name;
    private Email email;
    private LocalDateTime createdAt;

    public UserDto() {
    }

    public UserDto(UserId userId, Password password, Name name, Email email, LocalDateTime createdAt) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", password=" + password +
                ", name=" + name +
                ", email=" + email +
                ", createdAt=" + createdAt +
                '}';
    }
}
