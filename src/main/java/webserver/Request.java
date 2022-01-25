package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Request {
    private final String type;
    private final String uri;
    private final Map<String, String> query;
    private final String httpVer;
    private final Map<String, List<String>> header = new HashMap<>();
    private final List<String> body = new ArrayList<>();

    public Request(BufferedReader a) throws IOException {
        String[] requestLine = a.readLine().split(" ");

        type = requestLine[0];
        uri = HttpRequestUtils.parseUri(requestLine[1]);
        query = HttpRequestUtils.parseQuery(requestLine[1]);
        httpVer = requestLine[2];

        String line;
        while (!(line = a.readLine()).equals("")) {
            HttpRequestUtils.Pair b = HttpRequestUtils.parseHeader(line);
            header.put(b.getKey(), Arrays.asList(b.getValue().split(", ")));
        }

        //TODO : body가 있는경우 추가하는 로직 + body가 없는 경우에도 돌아갈 수 있게 로직 세팅
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", query=" + query +
                ", httpVer='" + httpVer + '\'' + '\n' +
                ", header=" + header + '\n' +
                ", body=" + body +
                '}';
    }
}
