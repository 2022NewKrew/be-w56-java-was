package was.domain.requestHandler;

import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import was.meta.MediaTypes;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NegotiationHandler implements RequestHandler {

    private NegotiationHandler() {
    }

    public static NegotiationHandler getInstance() {
        return NegotiationHandlerWrapper.INSTANCE;
    }

    private static class NegotiationHandlerWrapper {
        private static final NegotiationHandler INSTANCE = new NegotiationHandler();
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) {
        if (HttpStatus.FOUND.equals(res.getStatus())) {
            return;
        }

        final String fileExtension;
        if (!req.getPath().contains(".")) {
            fileExtension = "";
        } else {
            fileExtension = req.getPath().split("\\.")[1];
        }

        String acceptStr = req.getHeader(HttpHeaders.ACCEPT);
        if (acceptStr == null) {
            acceptStr = "*.*";
        }

        final List<String> accept = Arrays.stream(acceptStr.split(","))
                .map(token -> token.replaceAll("\\*", "(.*)").trim().split(";")[0])
                .collect(Collectors.toList());

        final MediaTypes mediaTypes = MediaTypes.findMediaType(accept, fileExtension);

        final Map<String, String> header = new HashMap<>();

        header.put(HttpHeaders.CONTENT_TYPE, mediaTypes.getValue());
        header.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(res.getContentLength()));

        res.addAllHeaders(header);
    }
}
