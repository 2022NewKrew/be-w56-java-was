package webserver.http.request;

import static webserver.http.HttpMeta.INDEX_OF_HTTP_VERSION_IN_REQUEST_LINE;
import static webserver.http.HttpMeta.INDEX_OF_METHOD_IN_REQUEST_LINE;
import static webserver.http.HttpMeta.INDEX_OF_URI_IN_REQUEST_LINE;
import static webserver.http.HttpMeta.SEPARATOR_OF_REQUEST_LINE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
        throws IOException, NullRequestException {
        String httpRequestLineString = br.readLine();

        if (httpRequestLineString == null) {
            throw new NullRequestException();
        }

        log.debug("Request Line : {}", httpRequestLineString);

        String[] httpRequestLineSplitArray = httpRequestLineString.split(SEPARATOR_OF_REQUEST_LINE);
        HttpRequestLine httpRequestLine = new HttpRequestLine(
            Method.getMethodFromString(httpRequestLineSplitArray[INDEX_OF_METHOD_IN_REQUEST_LINE]),
            httpRequestLineSplitArray[INDEX_OF_URI_IN_REQUEST_LINE],
            httpRequestLineSplitArray[INDEX_OF_HTTP_VERSION_IN_REQUEST_LINE]
        );

        log.debug(
            "Method: {} URI: {} Version: {} decoded",
            httpRequestLine.getMethod(),
            httpRequestLine.getUri(),
            httpRequestLine.getHttpVersion()
        );
        return httpRequestLine;
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
        log.debug("Request Headers(size:{}}) decoded", requestHeaders.getSize());
        return requestHeaders;
    }

    private static HttpRequestBody createRequestBody(BufferedReader br) {
        // TODO : 3단계 요구사항
        HttpRequestBody httpRequestBody = new HttpRequestBody("");
        log.debug("Request Body(size:{}) decoded", httpRequestBody.getBodySize());
        return httpRequestBody;
    }
}
