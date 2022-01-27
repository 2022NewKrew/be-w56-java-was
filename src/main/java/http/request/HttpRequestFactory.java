package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import util.Constant;
import webserver.HttpMethod;

public class HttpRequestFactory {

    public static final String CONTENT_LENGTH = "Content-Length";

    public static HttpRequest getHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> startLineAndHeader = readInputStreamStartLineAndHeader(bufferedReader);
        String startLineString = startLineAndHeader.get(0);
        String headerString = startLineAndHeader.get(1);

        RequestStartLine startLine = getStartLine(startLineString);
        RequestHeader header = new RequestHeader(headerString);

        if (header.has(CONTENT_LENGTH)) {
            return new HttpRequest(startLine, header,
                    readRequestBody(bufferedReader, Integer.parseInt(header.get(CONTENT_LENGTH))));
        }
        return new HttpRequest(startLine, header, null);
    }

    private static List<String> readInputStreamStartLineAndHeader(BufferedReader bufferedReader)
            throws IOException {
        StringBuilder result = new StringBuilder();

        String line;
        while (!(line = bufferedReader.readLine()).equals("")) {
            result.append(line).append(Constant.lineBreak);
        }

        return List.of(result.toString().split(Constant.lineBreak, 2));
    }

    private static RequestStartLine getStartLine(String startLineString) {
        List<String> components = List.of(startLineString.split(" "));
        HttpMethod method = HttpMethod.valueOf(components.get(0));
        String url = components.get(1);
        String protocol = components.get(2);

        List<String> queryComponents;
        if (url.contains("?")) {
            queryComponents = List.of(url.split("\\?"));
            return new RequestStartLine(method, queryComponents.get(0), protocol,
                    queryComponents.get(1));
        }

        return new RequestStartLine(method, url, protocol);
    }

    private static RequestBody readRequestBody(BufferedReader bufferedReader, int bodySize)
            throws IOException {
        char[] cbuf = new char[bodySize];
        bufferedReader.read(cbuf);

        return new RequestBody(new String(cbuf));
    }
}
