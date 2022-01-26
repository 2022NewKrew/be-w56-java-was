package model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    @Builder.Default
    private int id = -1;
    private String stringId;
    private String password;
    private String name;
    private String email;

    public void changePassword(String password){
        this.password = password;
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public Boolean isNew(){
        return id == -1;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", stringId='" + stringId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
