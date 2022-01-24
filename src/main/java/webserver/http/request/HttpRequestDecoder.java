package webserver.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import webserver.http.request.exceptions.NullRequestException;

public class HttpRequestDecoder {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestDecoder.class);

    public static HttpRequest decode(InputStream in) throws IOException, NullRequestException, URISyntaxException {
        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);

        HttpRequestLine requestLine = createRequestLine(br);
        HttpRequestHeaders requestHeaders = createRequestHeaders(br);
        HttpRequestBody requestBody = createRequestBody(br);

        return new HttpRequest(requestLine, requestHeaders, requestBody);
    }

    private static HttpRequestLine createRequestLine(BufferedReader br)
        throws IOException, NullRequestException, URISyntaxException {
        String httpRequestLine = br.readLine();

        if (httpRequestLine == null) {
            throw new NullRequestException();
        }

        log.debug("Request Line : {}", httpRequestLine);

        String[] httpRequestLineSplitArray = httpRequestLine.split(" ");
        return new HttpRequestLine(
            Method.getMethodFromString(httpRequestLineSplitArray[0]),
            new URI(httpRequestLineSplitArray[1]),
            httpRequestLineSplitArray[2]
        );
    }

    private static HttpRequestHeaders createRequestHeaders(BufferedReader br) throws IOException {
        HttpRequestHeaders requestHeaders = new HttpRequestHeaders();

        while (br.ready()) {
            String line = br.readLine();
            if (line.isEmpty()) {
                break;
            }

            log.debug("Header : {}", line);

            Pair header = HttpRequestUtils.parseHeader(line);
            requestHeaders.addHeader(header.getKey(), header.getValue());
        }
        return requestHeaders;
    }

    private static HttpRequestBody createRequestBody(BufferedReader br) {
        // TODO : 3단계 요구사항
        return new HttpRequestBody("");
    }
}
