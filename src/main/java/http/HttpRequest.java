package http;

import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

import static util.Constant.*;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final String target;
    private final HttpVersion version;
    private final HttpHeader headers;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Builder
    public HttpRequest(String requestLine, List<Pair> pairs) {
        String[] tokens = requestLine.split(BLANK);
        this.method = HttpMethod.valueOf(tokens[0]);
        this.target = tokens[1];
        this.version = HttpVersion.from(tokens[2]);
        this.headers = HttpHeader.of(pairs);
    }

    public HttpResponse makeResponse() {
        List<Pair> pairs = new ArrayList<>();
        logger.info("makeResponse {}", headers.get(ACCEPT));
        pairs.add(Pair.of(CONTENT_TYPE, headers.get(ACCEPT).split(COMMA)[0] + UTF_8));
        try {
            String path = this.target;
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
}
