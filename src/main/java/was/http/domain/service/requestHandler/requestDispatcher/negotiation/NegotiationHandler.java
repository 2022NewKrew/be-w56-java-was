package was.http.domain.service.requestHandler.requestDispatcher.negotiation;


import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.view.View;
import was.http.meta.HttpHeaders;
import was.http.meta.MediaTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class NegotiationHandler {

    public void negotiate(View view, HttpRequest req, HttpResponse res) {
        if (res.isRedirect()) {
            return;
        }

        if (res.hasNotContentType()) {
            final MediaTypes mediaTypes = findMediaTypeByAcceptTokenAndFileExtension(req, view);
            res.addHeader(HttpHeaders.CONTENT_TYPE, mediaTypes.getValue());
        }

        res.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(res.getContentLength()));
    }

    private MediaTypes findMediaTypeByAcceptTokenAndFileExtension(HttpRequest req, View view) {
        final List<String> acceptTokens = Arrays.stream(getAcceptHeader(req).split(","))
                .map(this::toAcceptToken)
                .collect(Collectors.toList());

        final String fileExtension = getFileExtension(view);

        return MediaTypes.findMediaType(acceptTokens, fileExtension);
    }

    private String toAcceptToken(String acceptHeader) {
        return acceptHeader.replaceAll("\\*", "(.*)")
                .trim().split(";")[0];
    }

    private String getFileExtension(View view) {
        final String viewPath = view.getPath();

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
