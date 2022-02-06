package app.http;

import static util.Constant.SET_COOKIE_FORMAT;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cookie {
    private String name;
    private String value;
    private String path = "/";
    private Duration maxAge = Duration.ofDays(7);

    @Override
    public String toString() {
        return String.format(SET_COOKIE_FORMAT, name, value, path, maxAge);
    }
}
