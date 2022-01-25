package webserver.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;
}
