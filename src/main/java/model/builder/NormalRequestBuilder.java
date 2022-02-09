package model.builder;

import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class NormalRequestBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException, SQLException {
        String uri = requestHeader.getHeader("uri");
        if (uri.equals("/")) {
            uri = Links.MAIN;
        }

        if (!uri.contains(".")) {
            uri += ".html";
        }

        byte[] body = readBody(uri);
        if (uri.contains(".html")) {
            body = DynamicHtmlBuilder.getDynamicHtml(body, model);
        }

        return ResponseHeader.builder()
                .uri(uri)
                .htmlResponseHeader(HtmlResponseHeader.RESPONSE_200)
                .body(body)
                .accept(requestHeader.getAccept())
                .build();
    }
}
