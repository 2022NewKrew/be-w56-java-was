package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.exception.InvalidHttpMethodException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static util.HttpRequestUtils.Pair;
import static util.HttpRequestUtils.parseHeader;

public class HttpRequestHandler {
    private CustomHttpRequest httpRequest;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    public HttpRequestHandler() {
        httpRequest = new CustomHttpRequest();
    }

    public CustomHttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void parseRequest(BufferedReader br) throws IOException {
        httpRequest.putHeaderRequestLine(parseRequestFirstLine(br.readLine()));
        String requestURI = httpRequest.getRequestURI();

        if(isQueryStringExist(requestURI)) {
            log.debug("---------query string exist-----------");
            parserQueryString(requestURI);
            log.debug("new uri : {}", httpRequest.getRequestURI());
        }
        String str;
        while (!(str = br.readLine()).equals("")) {
            log.debug("msg : {}", str);
            Pair data = parseHeader(str);

            httpRequest.putHeaderData(data);
        }
    }

    public boolean isStaticResourceRequest() {
        String pattern = "^[a-zA-Z0-9/.-]*\\.[a-z]*$";
        String requestURI = httpRequest.getRequestURI();

        return Pattern.matches(pattern, requestURI);
    }

    private Map<String, String> parseRequestFirstLine(String requestLine) {
        log.debug("msg : {}", requestLine);
        String[] requestLineArr = requestLine.split(" ");

        if(!CustomHttpMethod.isHttpMethod(requestLineArr[0]) || requestLineArr.length != 3) {
            throw new InvalidHttpMethodException("invalid http method request");
        }

        return new HashMap<>() {{
            put("method", requestLineArr[0]);
            put("uri", requestLineArr[1]);
            put("http", requestLineArr[2]);
        }};
    }

    private void parserQueryString(String uri) {
        int startIdx = uri.indexOf("?") + 1;
        String queryString = uri.substring(startIdx);

        String[] split = queryString.split("&");
        Map<String, String> param = new HashMap<>();
        for (String s : split) {
            String[] keyValue = s.split("=");
            validateQueryString(keyValue);

            param.put(keyValue[0], keyValue[1]);
        }

        httpRequest.putRequestParam(param);
        httpRequest.putHeaderRequestLine(new HashMap<>(){
            {
                put("uri", uri.split("\\?")[0]);
            }
        });
    }

    private void validateQueryString(String[] keyValue) {
        if(keyValue.length != 2) {
            throw new InvalidHttpMethodException("invalid query string format");
        }

        String pattern = "^[a-zA-Z0-9]*$";
        if(!Pattern.matches(pattern, keyValue[0])) {
            throw new InvalidHttpMethodException("invalid query string format");
        }
    }

    private boolean isQueryStringExist(String uri) {
        return uri.contains("?");
    }
}
