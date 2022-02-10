package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.HttpResponse;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class NormalRequestResponseBuilder extends ResponseBuilder {
    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        String uri = requestHeader.getHeader("uri");

        if (!uri.contains(".")) {
            uri += ".html";
        }

        byte[] body = readBody(uri);
        if (uri.contains(".html")) {
            body = DynamicHtmlBuilder.getDynamicHtml(body, model);
        }

        return HttpResponse.builder()
                .locationUri(uri)
                .htmlResponseHeader(HttpResponseHeader.RESPONSE_200)
                .body(body)
                .accept(requestHeader.getAccept())
                .build();
    }
}
