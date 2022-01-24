package util;

import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

import static util.Constant.*;

@Getter
public class Request {
    private final String method;
    private final String target;
    private final String version;
    private final Map<String, String> headers;

    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    @Builder
    public Request(String requestLine, List<Pair> pairs) {
        String[] tokens = requestLine.split(BLANK);
        this.method = tokens[0];
        this.target = tokens[1];
        this.version = tokens[2];
        headers = pairs.stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public Response makeResponse() {
        try {
            String path = this.target;
            if (path.equals(ROOT_PATH)) {
                path = INDEX_PATH;
            }
            path = WEBAPP_PATH + path;
            byte[] body = Files.readAllBytes(new File(path).toPath());
            return Response.builder()
                    .statusCode(STATUSCODE_200)
                    .reasonPhrase(OK)
                    .contextType(headers.get(ACCEPT).split(COMMA)[0])
                    .body(body)
                    .build();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Response.builder()
                    .statusCode(STATUSCODE_404)
                    .reasonPhrase(NOT_FOUND)
                    .contextType(headers.get(ACCEPT).split(COMMA)[0])
                    .body(new byte[0])
                    .build();
        }
    }
}
