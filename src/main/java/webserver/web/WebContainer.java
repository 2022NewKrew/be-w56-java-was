package webserver.web;

import lombok.RequiredArgsConstructor;

import java.net.Socket;

@RequiredArgsConstructor
public class WebContainer {
    private final WebService webService;
    private final RequestParser requestParser;

    public void init(Socket connection) {
        RequestHandler requestHandler = new RequestHandler(connection, webService, requestParser);
        requestHandler.run();
    }
}
