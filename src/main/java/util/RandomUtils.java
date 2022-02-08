package util;

import java.util.UUID;

public class RandomUtils {

    public static String getRandomID() {
        return UUID.randomUUID().toString();
    }
}
