package util;

import org.slf4j.Logger;

import java.util.Map;

public class LogUtils {

    public static void requestLog(Map<String, String> requestMap, Logger log) {
        log.debug("request");
        for(Map.Entry<String, String> request : requestMap.entrySet())
            log.debug("{} : {}", request.getKey(), request.getValue());
    }

    public static void headerLog(Map<String, String> headerMap, Logger log) {
        log.debug("header");
        for(Map.Entry<String, String> header : headerMap.entrySet())
            log.debug("{} : {}", header.getKey(), header.getValue());
    }

}
