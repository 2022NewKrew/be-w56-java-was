package app.http;

import static util.Constant.ACCEPT;
import static util.Constant.BLANK;
import static util.Constant.COMMA;
import static util.Constant.CONTENT_LENGTH;
import static util.Constant.CONTENT_TYPE;
import static util.Constant.INDEX_PATH;
import static util.Constant.ROOT_PATH;
import static util.Constant.UTF_8;
import static util.Constant.WEBAPP_PATH;
import static util.HttpRequestUtils.parseParams;
import static util.HttpRequestUtils.parseTarget;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils.Pair;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final String url;
    private final HttpVersion version;
    private final HttpHeader headers;
    private final HttpRequestParams params;
    private final HttpRequestBody body;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Builder
    public HttpRequest(String requestLine, HttpHeader header, HttpRequestBody body) {
        String[] tokens = requestLine.split(BLANK);
        this.method = HttpMethod.valueOf(tokens[0]);
        String[] targetTokens = parseTarget(tokens[1]);
        this.url = targetTokens[0];
        this.params = HttpRequestParams.of(parseParams(targetTokens));
        this.version = HttpVersion.from(tokens[2]);
        this.headers = header;
        this.body = body;
    }

    public HttpResponse makeResponse(String path) {
        List<Pair> pairs = new ArrayList<>();
        pairs.add(Pair.of(CONTENT_TYPE, headers.get(ACCEPT).split(COMMA)[0] + UTF_8));
        try {
            if (path.equals(ROOT_PATH)) {
                path = INDEX_PATH;
            }
            path = WEBAPP_PATH + path;
            byte[] body = Files.readAllBytes(new File(path).toPath());
            pairs.add(Pair.of(CONTENT_LENGTH, String.valueOf(body.length)));
            return HttpResponse.builder()
                    .version(HttpVersion.HTTP_1_1)
                    .status(HttpStatus.OK)
                    .pairs(pairs)
                    .body(body)
                    .build();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponse.builder()
                    .version(HttpVersion.HTTP_1_1)
                    .status(HttpStatus.NOT_FOUND)
                    .pairs(pairs)
                    .body(new byte[0])
                    .build();
        }
    }

    public HttpResponse makeResponse() {
        return makeResponse(this.url);
    }
}
