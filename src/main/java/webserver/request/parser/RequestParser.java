package webserver.request.parser;

import webserver.http.request.HttpBody;
import webserver.http.request.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpUrlQuery;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {

    private RequestParser() {

    }

    public static HttpRequest parse(BufferedReader br) throws IOException {
        HttpHeader header = HeaderParser.parse(br);
        HttpUrlQuery query = QueryParser.parse(br, header);
        HttpBody body = BodyParser.parse(br, header);

        return new HttpRequest(header, body, query);
    }
}
