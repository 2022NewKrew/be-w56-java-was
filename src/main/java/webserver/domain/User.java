package webserver.domain;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;
}
