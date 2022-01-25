package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
}
