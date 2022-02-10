package domain.session;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SessionId {

    private static final List<String> CHARSET;
    private static final int LENGTH = 13;

    static {
        CHARSET = Arrays.stream("0123456789QWERTYUIOPASDFGHJKLZXCVBNM".split(""))
                .collect(Collectors.toList());
    }

    private final String value;

    public SessionId(String value) {
        this.value = value;
    }

    public static SessionId create() {
        Collections.shuffle(CHARSET);
        return new SessionId(CHARSET.subList(0, LENGTH).stream().collect(Collectors.joining()));
    }

    public String getValue() {
        return value;
    }
}
