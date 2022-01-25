package webserver;

import lombok.Getter;
import lombok.ToString;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Getter
@ToString
public class Request {
    private final String type;
    private final String uri;
    private final Map<String, String> query;
    private final String httpVer;
    private final Map<String, List<String>> header = new HashMap<>();
    private final List<String> body = new ArrayList<>();

    public Request(InputStream in) throws IOException {
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inReader);

        String[] requestLine = reader.readLine().split(" ");

        type = requestLine[0];
        uri = HttpRequestUtils.parseUri(requestLine[1]);
        query = HttpRequestUtils.parseQuery(requestLine[1]);
        httpVer = requestLine[2];

        String line;
        while (!(line = reader.readLine()).equals("")) {
            HttpRequestUtils.Pair headerPair = HttpRequestUtils.parseHeader(line);
            header.put(headerPair.getKey(), Arrays.asList(headerPair.getValue().split(", ")));
        }

        //TODO : body가 있는경우 추가하는 로직 + body가 없는 경우에도 돌아갈 수 있게 로직 세팅
    }
}
