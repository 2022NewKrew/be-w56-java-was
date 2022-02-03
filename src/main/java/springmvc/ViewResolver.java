package springmvc;

import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    private static final String PREFIX = "./webapp";

    public static void resolve(String view, HttpResponse httpResponse) {
        try {
            if (view.startsWith("redirect:")) {
                httpResponse.setStatus(HttpStatus.FOUND);
                httpResponse.setHeader("Location", view.split(":")[1]);
            } else {
                httpResponse.setStatus(HttpStatus.OK);
                httpResponse.setBody(getBytes(view));
            }
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
        }
    }

    private static byte[] getBytes(String url) throws IOException {
        return Files.readAllBytes(new File(PREFIX + url).toPath());
    }
}
