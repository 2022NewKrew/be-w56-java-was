package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestPathUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestPathUtils.class);

    public static String extractRequestURL(String line){
        String[] tokens = line.split(" ");
        log.info("Request URL: {}", tokens[1]);
        return tokens[1];
    }


}
