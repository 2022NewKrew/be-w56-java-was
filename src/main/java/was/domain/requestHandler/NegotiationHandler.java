package was.domain.requestHandler;


import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import was.meta.MediaTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class NegotiationHandler implements RequestHandler {

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) {
        if (isRedirect(res)) {
            return;
        }

        if (res.getContentType() == null) {
            final MediaTypes mediaTypes = findMediaTypeByAcceptTokenAndFileExtension(req, res);
            res.addHeader(HttpHeaders.CONTENT_TYPE, mediaTypes.getValue());
        }

        res.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(res.getContentLength()));
        requestHandlerChain.handle(req, res);
    }

    private MediaTypes findMediaTypeByAcceptTokenAndFileExtension(HttpRequest req, HttpResponse res) {
        final List<String> acceptTokens = Arrays.stream(getAcceptHeader(req).split(","))
                .map(this::toAcceptToken)
                .collect(Collectors.toList());

        final String fileExtension = getFileExtension(res);

        return MediaTypes.findMediaType(acceptTokens, fileExtension);
    }

    private boolean isRedirect(HttpResponse res) {
        return HttpStatus.FOUND.equals(res.getStatus());
    }

    private String toAcceptToken(String acceptHeader) {
        return acceptHeader.replaceAll("\\*", "(.*)")
                .trim().split(";")[0];
    }

    private String getFileExtension(HttpResponse res) {
        if (res.hasNotViewPath()) {
            return "";
        }

        final String viewPath = res.getViewPath();

        final String fileExtension;
        if (!viewPath.contains(".")) {
            fileExtension = "";
        } else {
            fileExtension = viewPath.split("\\.")[1];
        }

        return fileExtension;
    }

    private String getAcceptHeader(HttpRequest req) {
        String accept = req.getHeader(HttpHeaders.ACCEPT);
        if (accept == null) {
            accept = "*.*";
        }
        return accept;
    }
}
