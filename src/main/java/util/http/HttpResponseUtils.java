package util.http;

import app.core.TemplateEngine;
import util.ui.Model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponseUtils {

    private HttpResponseUtils() {
    }

    public static void response(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        dos.writeBytes(httpResponse.headerText());
        if (httpResponse.getBody() != null) {
            byte[] body = httpResponse.getBody();
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    public static void dynamicResponse(HttpResponse httpResponse, String location, Model model) throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        httpResponse.setStatus(HttpStatus.OK);
        byte[] body = TemplateEngine.render(location, model);
        String[] array = location.split("\\.");
        String format = array[array.length - 1];
        httpResponse.setHeader("Content-Type", String.format("text/%s;charset=utf-8", format));
        httpResponse.setHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);
    }

    public static void staticResponse(HttpResponse httpResponse, String location) throws IOException {
        httpResponse.setStatus(HttpStatus.OK);
        byte[] body = Files.readAllBytes(new File("./webapp" + location).toPath());
        String[] array = location.split("\\.");
        String format = array[array.length - 1];
        httpResponse.setHeader("Content-Type", String.format("text/%s;charset=utf-8", format));
        httpResponse.setHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);
    }

    public static void serverErrorResponse(HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        byte[] body = "500 Internal Server Error".getBytes(StandardCharsets.UTF_8);
        httpResponse.setHeader("Content-Type", "text/html;charset=utf-8");
        httpResponse.setHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);
    }

    public static void notFoundResponse(HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.NOT_FOUND);
        byte[] body = "404 Not Found".getBytes(StandardCharsets.UTF_8);
        httpResponse.setHeader("Content-Type", "text/html;charset=utf-8");
        httpResponse.setHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);

    }

    public static void redirectResponse(HttpResponse httpResponse, String location, String host) {
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader("Location", String.format("http://%s%s", host, location));
    }

}
