package http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;

    private final RequestParams requestParams = new RequestParams();

    private HttpHeader httpHeader;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            parsingRequestLine(br);
            requestParams.addRequestParams(requestLine.getQueryString());
            parsingHeader(br);
            String bodyLine = IOUtils.readData(br, httpHeader.getContentLength());
            requestParams.addRequestParams(bodyLine);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public void parsingRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new RuntimeException("requestLine이 존재하지 않습니다.");
        }
        requestLine = new RequestLine(line);
    }

    public void parsingHeader(BufferedReader br) throws IOException {
        httpHeader = new HttpHeader();
        String line = br.readLine();
        while (!line.equals("")) {
            log.debug("header : {}", line);
            httpHeader.addHeaderParameter(line);
            line = br.readLine();
        }
    }
}
