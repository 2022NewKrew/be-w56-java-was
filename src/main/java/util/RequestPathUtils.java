package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;


public class RequestPathUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestPathUtils.class);

    public static Boolean containsParam(String url){
        return url.contains("?");
    }

    public static String extractRequestParam(String url){
        String[] tokens = url.split("\\?");
        log.info("Request Param: {} / {}", tokens[0], tokens[1]);
        return tokens[1];
    }

    public static String extractRequestUrlOnly(String url){
        String[] tokens = url.split("\\?");
        return tokens[0];
    }

}
