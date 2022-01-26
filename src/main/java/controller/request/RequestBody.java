package controller.request;

import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오전 11:19
 */
public class RequestBody {

    private Map<String, String> paramMap;

    private RequestBody(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public static RequestBody of(String queryString) {
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(queryString);
        return new RequestBody(paramMap);
    }

    public String getParameter(String key) {
        return paramMap.get(key);
    }
}
