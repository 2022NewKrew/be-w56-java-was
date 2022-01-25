package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponseUtils {

    public static void res(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        dos.writeBytes(httpResponse.headerText());
        if (httpResponse.getBody() != null) {
            byte[] body = httpResponse.getBody();
            dos.write(body, 0, body.length);
        }
        dos.flush();
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

    public static void redirectResponse(HttpResponse httpResponse, String location, String host) {
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader("Location", String.format("http://%s%s", host, location));
    }


}
