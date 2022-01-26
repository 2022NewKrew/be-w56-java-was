package webserver;

import exception.ServerErrorException;
import lombok.extern.slf4j.Slf4j;
import util.HttpMethod;
import util.HttpRequest;
import util.HttpResponse;
import util.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static util.HttpRequestUtils.parseRequest;

@Slf4j
public class RequestHandler extends Thread {
    private static final Map<Predicate<HttpRequest>, BiConsumer<HttpRequest, HttpResponse>> handlers;
    static {
        // TODO: Handlers probably deserve an dedicated object
        // TODO: Need a better way to register handlers. It seems possible to implement spring's annotation style mapping with Reflection library.
        handlers = new LinkedHashMap<>();

        handlers.put(
                httpRequest -> {
                    // TODO: Pattern is compiling every time
                    String urlRegex = "/user/create.*";
                    return httpRequest.getHttpMethod() == HttpMethod.GET && Pattern.matches(urlRegex, httpRequest.getUri());
                }, RequestHandler::getUserCreateHandler);
        handlers.put(
                httpRequest -> {
                    // TODO: Pattern is compiling every time
                    // TODO: Pattern for static server is probably too broad
                    String urlRegex = ".*";
                    return httpRequest.getHttpMethod() == HttpMethod.GET && Pattern.matches(urlRegex, httpRequest.getUri());
                }, RequestHandler::safeGetStaticHandler);
    }

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        // 1. Parse the http request to form servletReq
        // 2. Search url in the url to handler mapper
        // 3. Call the matching url handler(servlet, req, res)
        // 4. Post-process the response(header+body) and send it to client
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest httpRequest = parseRequest(br);
            // TODO: Http version should be provided by the handler. Copying from request is wrong.
            HttpResponse httpResponse = new HttpResponse(httpRequest, httpRequest.getHttpVersion());
            handlers.entrySet().stream()
                    .filter(entry -> entry.getKey().test(httpRequest))
                    .findFirst()
                    // TODO: This exception is caught nowhere. We need a global error handler.
                    .orElseThrow(ServerErrorException::new).getValue().accept(httpRequest, httpResponse);
            dos.write(httpResponse.toByte());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void getUserCreateHandler(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> queryStrings = httpRequest.getQueryStrings();
        httpResponse.setContentTypeWithURI("text/plain;charset=utf-8");
        httpResponse.setBody(queryStrings.entrySet().toString().getBytes());
        httpResponse.setHttpStatus(HttpStatus.OK);
    }

    private static void safeGetStaticHandler(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            getStaticHandler(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            // TODO: This is not right
            httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void getStaticHandler(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String uri = httpRequest.getUri();
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        Path path = Paths.get("./webapp" + uri);
        httpResponse.setContentTypeWithURI(uri);
        httpResponse.setBody(Files.readAllBytes(path));
        httpResponse.setHttpStatus(HttpStatus.OK);
    }
}
