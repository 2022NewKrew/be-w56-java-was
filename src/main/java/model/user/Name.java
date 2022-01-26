package model.user;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Name {
    private final String name;

    public Name(String name) {
        valid(name);
        this.name = URLDecoder.decode(name, StandardCharsets.UTF_8);
    }

    private void valid(String name) {
        if (name == null) {
            throw new NullPointerException("이름을 입력하세요.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
