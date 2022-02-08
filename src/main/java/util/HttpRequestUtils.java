package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.request.RequestBody;
import webserver.request.RequestHeader;
import webserver.request.RequestLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);
    private static final String EMPTY = "";

    public static Request createRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        if(line == null || line.equals(EMPTY)) {
            return null;
        }
        RequestLine requestLine = RequestLine.from(line);
        log.info("requestLine: {}", requestLine);

        RequestHeader requestHeader = RequestHeader.from(br);
        log.info("requestHeader: {}", requestHeader);

        RequestBody requestBody = RequestBody.from(IOUtils.readData(br, requestHeader.getContentLength()));

        log.info("requestBody: {}", requestBody);
        return Request.of(requestLine, requestHeader, requestBody);
    }

}
