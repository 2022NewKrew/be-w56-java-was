package webserver.resolver;

import webserver.domain.Cookie;
import webserver.domain.Response;
import webserver.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewResolver {
    private final static ViewResolver instance = new ViewResolver();
    private final Map<String, String> redirect;

    public static ViewResolver getInstance() {
        return instance;
    }

    private ViewResolver() {
        redirect = new HashMap<>();
        redirect.put("/", "/index.html");
    }

    public Response resolveResponse(String returnFromController, Cookie cookie) throws IOException {
        if (returnFromController.startsWith("redirect:"))
            return makeResponse(getRedirectResponseHeader(returnFromController.substring("redirect:".length())),
                    cookie,
                    null);
        if (redirect.containsKey(returnFromController))
            returnFromController = redirect.get(returnFromController);

        String bodyType = "html";
        byte[] body = "wrong url".getBytes();
        if (returnFromController.indexOf('.') != -1) {
            bodyType = IOUtils.getFileFormat(returnFromController);
            body = getFileResponseBody(returnFromController);
        }

        return makeResponse(getFileResponseHeader(body.length, bodyType), cookie, body);
    }

    private Response makeResponse(String responseHeader, Cookie cookie, byte[] body) {
        if (!cookie.getAddedKeys().isEmpty()) {
            responseHeader += cookie.getAddedKeys()
                    .stream()
                    .map(key -> "Set-Cookie: " + key + "=" + cookie.getData(key) + "; Path=/")
                    .collect(Collectors.joining("\r\n"));
        }
        responseHeader += "\r\n";

        return new Response(responseHeader, body);
    }

    private String getFileResponseHeader(int lengthOfBodyContent, String bodyType) {
        return "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/" + bodyType + ";charset=utf-8\r\n" +
                "Content-Length: " + lengthOfBodyContent + "\r\n";
    }

    private String getRedirectResponseHeader(String url) {
        return "HTTP/1.1 302 FOUND\r\n" +
                "Location: " + url + "\r\n";
    }

    private byte[] getFileResponseBody(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }
}
