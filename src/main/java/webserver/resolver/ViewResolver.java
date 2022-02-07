package webserver.resolver;

import model.User;
import webserver.RequestHandler;
import webserver.domain.Cookie;
import webserver.domain.Model;
import webserver.domain.Response;
import webserver.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
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

    public Response resolveResponse(String returnFromController, Cookie cookie, Model model) throws IOException {
        if (returnFromController.startsWith("redirect:"))
            return makeResponse(getRedirectResponseHeader(returnFromController.substring("redirect:".length())),
                    cookie,
                    null);
        if (redirect.containsKey(returnFromController))
            returnFromController = redirect.get(returnFromController);

        if (returnFromController.indexOf('.') == -1) {
            returnFromController += ".html";
        }
        String bodyType = IOUtils.getFileFormat(returnFromController);
        byte[] body = getFileResponseBody(returnFromController, model);

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

    private byte[] getFileResponseBody(String url, Model model) throws IOException {
        if (model.isEmpty())
            return Files.readAllBytes(new File("./webapp" + url).toPath());

        String fileString = Files.readString(new File("./webapp" + url).toPath());
        RequestHandler.log.debug(fileString);
        for (String key : model.getKeys()) {
            Object object = model.getAttribute(key);
            fileString = insertModel(fileString, key, object);
        }

        return fileString.getBytes();
    }

    private String insertModel(String string, String key, Object value) {
        StringBuilder sb = new StringBuilder(string);
        StringBuilder contentBuilder = new StringBuilder();

        int start = string.indexOf("{{#" + key + "}}");
        int end = string.indexOf("{{/" + key + "}}");
        String content = string.substring(start + ("{{#" + key + "}}").length(), end);
        sb.delete(start, end + ("{{/"+key+"}}").length());

        if (value instanceof List) {
            ((List<?>) value).forEach(e -> contentBuilder.append(insertFields(content, e)));
        } else {
            contentBuilder.append(insertFields(content, value));
        }
        sb.insert(start, contentBuilder);

        return sb.toString();
    }

    private String insertFields(String string, Object value) {
        RequestHandler.log.debug("insertFields");
        User user = (User)value;
        RequestHandler.log.debug(user.toString());
        Field[] fields = value.getClass().getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String fieldValue = null;
            try {
                fieldValue = (String)field.get(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            RequestHandler.log.debug(name);
            RequestHandler.log.debug(fieldValue);
            string = string.replace("{{"+name+"}}", fieldValue);
        }
        return string;
    }
}
