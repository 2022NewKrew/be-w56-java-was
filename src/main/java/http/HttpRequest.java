package http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

@Getter
public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;

    private final RequestParams requestParams = new RequestParams();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            requestLine = createRequestLine(br);
            requestParams.addRequestParams(requestLine.getQueryString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public RequestLine createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new RuntimeException("requestLine이 존재하지 않습니다.");
        }
        return new RequestLine(line);
    }
}
