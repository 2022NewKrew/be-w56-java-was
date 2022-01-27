package util;

import db.DataBase;
import http.HttpResponse;
import model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseMapper {

    private HttpResponse httpResponse = new HttpResponse();

    private Map<String, String> parseUrl(String requsetUrl) {
        String[] splitUrl = requsetUrl.split("\\?");
        Map<String, String> map = new HashMap<>() {{
            put("url", splitUrl[0]);
            put("query", null);
        }};
        if (splitUrl.length == 2) {
            map.replace("query", splitUrl[1]);
        }
        return map;
    }

    public void buildResponse(String requestUrl, OutputStream out) throws IOException {
        Map<String, String> urlInfo = parseUrl(requestUrl);
        Map<String, String> queryData = new HashMap<>();
        String url = urlInfo.get("url");
        String responseDataPath = url;
        if (urlInfo.get("query") != null) {
            String query = urlInfo.get("query");
            queryData = HttpRequestUtils.parseQueryString(query);
            executeQuery(queryData, url);
        }
        switch (url) {
            case "/" : responseDataPath = "/index.html";
            case "/user/create" : responseDataPath = "/user/list.html";
        }
        httpResponse.setDos(out);
        httpResponse.setBody(responseDataPath);
        httpResponse.setResponseContentType(responseDataPath);
    }

    public void executeQuery(Map<String, String> queryData, String url) {
        switch (url) {
            case "/user/create" : signup(queryData);
        }
    }

    public void signup(Map<String, String> queryData) {
        User user = new User(queryData.get("userId"), queryData.get("password"), queryData.get("name"), queryData.get("email"));
        DataBase.addUser(user);
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
