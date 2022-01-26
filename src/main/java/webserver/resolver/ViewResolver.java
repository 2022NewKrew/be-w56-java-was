package webserver.resolver;

import webserver.domain.Response;
import webserver.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ViewResolver {
    private final static ViewResolver instance = new ViewResolver();
    private Map<String, String> redirect;

    public static ViewResolver getInstance() {
        return instance;
    }

    private ViewResolver() {
        redirect = new HashMap<>();
        redirect.put("/", "/index.html");
    }

    public Response resolveResponse(String returnFromController) throws IOException {
        if (returnFromController.startsWith("redirect:"))
            return getRedirectResponse(returnFromController.substring("redirect:".length()));
        if (redirect.containsKey(returnFromController))
            returnFromController = redirect.get(returnFromController);

        String bodyType = "html";
        byte[] body = "wrong url".getBytes();
        if (returnFromController.indexOf('.') != -1) {
            bodyType = IOUtils.getFileFormat(returnFromController);
            body = getFileResponseBody(returnFromController);
        }

        return new Response(getFileResponseHeader(body.length, bodyType), body);
    }

    private String getFileResponseHeader(int lengthOfBodyContent, String bodyType) {
        return "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/" + bodyType + ";charset=utf-8\r\n" +
                "Content-Length: " + lengthOfBodyContent + "\r\n" +
                "\r\n";
    }

    private byte[] getFileResponseBody(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private Response getRedirectResponse(String url) {
        return new Response("HTTP/1.1 302 FOUND\r\n" +
                "Location: " + url + "\r\n" +
                "\r\n");
    }
}
