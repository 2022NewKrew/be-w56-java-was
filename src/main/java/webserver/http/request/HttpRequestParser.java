package webserver.http.request;

import webserver.http.domain.HttpMethod;
import webserver.http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        startLine(httpRequest, bufferedReader);
        header(httpRequest, bufferedReader);
        if(httpRequest.getMethod().equals(HttpMethod.POST.toString())) {
            parseBody(httpRequest, bufferedReader);
        }
        return httpRequest;
    }

    private static void parseBody(HttpRequest httpRequest, BufferedReader bufferedReader) throws IOException {
        int contentLen = Integer.parseInt(httpRequest.getHeaderValue("Content-Length"));
        if(contentLen > 0) {
            String body = IOUtils.readData(bufferedReader, contentLen);
            setParams(httpRequest, body);
        }
    }

    private static void header(HttpRequest httpRequest, BufferedReader bufferedReader) throws IOException {
        String line;
        while(!(line = bufferedReader.readLine()).equals("")) {
            if(line == null) {
                break;
            }
            String[] keyAndValue = line.split(": ");
            httpRequest.addHeader(keyAndValue[0], keyAndValue[1]);
        }
        httpRequest.setCookie();
    }

    private static void startLine(HttpRequest httpRequest, BufferedReader bufferedReader) throws IOException {
        String startLine = bufferedReader.readLine();
        String[] parsedStartLine = HttpRequestUtils.parseBySpace(startLine);

        httpRequest.setMethod(parsedStartLine[0]);
        httpRequest.setRequestUri(parsedStartLine[1]);
        if(httpRequest.getMethod().equals(HttpMethod.GET.toString()) && httpRequest.getRequestUri().contains("\\?")) {
            String[] urlAndParam = httpRequest.getRequestUri().split("\\?");
            setParams(httpRequest, urlAndParam[1]);
        }
    }

    private static void setParams(HttpRequest req, String paramData) {
        Map<String, String> param = HttpRequestUtils.parseQueryString(paramData);
        req.setParams(param);
    }
}
