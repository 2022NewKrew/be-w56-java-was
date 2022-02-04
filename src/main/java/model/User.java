package model;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
