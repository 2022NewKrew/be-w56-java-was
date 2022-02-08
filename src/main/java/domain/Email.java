package domain;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Email {
    private final String email;

    public Email(String email) {
        valid(email);
        this.email = URLDecoder.decode(email, StandardCharsets.UTF_8);
    }

    private void valid(String email) {
        if (email == null || email.equals("")) {
            throw new NullPointerException("이메일을 입력하세요.");
        }
    }

    @Override
    public String toString() {
        return email;
    }
}
