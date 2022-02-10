package util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtils {

    private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);

    private static final String ENCODING = "UTF-8";
    private static final String PARAM_SEPARATOR = "&";
    private static final String PARAM_DEFINE = "=";
    private static final String PARAM_INIT = "?";
    private static final String PARAM_INIT_REGEX = "\\?";

    private UrlUtils() {}

    public static String decode(String url) {
        try {
            return URLDecoder.decode(url, ENCODING);
        } catch (java.io.UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return url;
        }
    }

    public static String trimParams(String url) {
        return url.split("\\?")[0];
    }

    public static Map<String, String> getParams(String url) {
        Map<String, String> params = new HashMap<>();
        for (String param: getStringParams(url).split(PARAM_SEPARATOR)) {
            putParam(params, param);
        }
        return params;
    }

    private static String getStringParams(String url) {
        return url.contains(PARAM_INIT) ? url.split(PARAM_INIT_REGEX)[1] : "";
    }

    private static void putParam(Map<String, String> params, String param) {
        if (param.contains(PARAM_DEFINE)) {
            params.put(param.split(PARAM_DEFINE)[0], param.split(PARAM_DEFINE)[1]);
        }
    }
}
