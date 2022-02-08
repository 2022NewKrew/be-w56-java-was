package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HandlerMapper;

import java.util.UUID;

public class SessionUtils {
    final private static String SESSION_ID = "SESSIONID";
    private static final Logger log = LoggerFactory.getLogger(SessionUtils.class);

    public static String getRandomID() {
        return UUID.randomUUID().toString();
    }

    public static String getSessionId(String cookie) {
        String[] tokens = cookie.split("; ");
        log.info("getSessionId -- ");

        for (String token : tokens) {
            log.info(token);
            String[] tuple = token.split("=");
            if (tuple[0].equals(SESSION_ID)) {
                return tuple[1];
            }
        }
        return null;
    }



}
