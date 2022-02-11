package app.http;

import static util.Constant.COOKIE_FORMAT;

import java.time.Duration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cookie {
    private String name;
    private String value;
    private String path = "/";
    private Duration maxAge = Duration.ofDays(7);

    public static Cookie from() {
        return new Cookie();
    }

    private Cookie() {}

    public String cookieValue() {
        return String.format(COOKIE_FORMAT, name, value, path, maxAge);
    }

    @Override
    public String toString() {
        return String.format(COOKIE_FORMAT, name, value, path, maxAge);
    }
}
