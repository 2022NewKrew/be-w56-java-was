package webserver.model;

import lombok.*;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cookie {
    private String name;
    private String value;
    private String path = "/";
    private Duration maxAge = Duration.ofDays(7);

    @Override
    public String toString() {
        return "Set-Cookie: " +
                name + "=" + value + "; " +
                "Path=" + path + "; " +
                "maxAge=" + maxAge.toSeconds() + ";";
    }
}
