package controller.request;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오전 11:19
 */
public class RequestHeader {

    private Map<String, String> paramMap;

    private RequestHeader(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public static RequestHeader from(List<String> requestHeaderStrings) {
        Map<String, String> paramMap = new HashMap<>();
        for (String requestHeaderString : requestHeaderStrings) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(requestHeaderString);
            paramMap.put(pair.getKey(), pair.getValue());
        }
        return new RequestHeader(paramMap);
    }

    public String getParameter(String key) {
        return paramMap.get(key);
    }
}
