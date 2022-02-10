package springmvc;

import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ViewResolver {

    private static final String PREFIX = "./webapp";

    public static void resolve(ModelAndView mav, HttpResponse httpResponse) {
        try {
            httpResponse.setStatus(mav.getStatus());
            if (mav.getStatus().equals(HttpStatus.FOUND)) {
                httpResponse.setHeader("Location", mav.getView());
            } else {
                httpResponse.setStatus(HttpStatus.OK);
                httpResponse.setBody(mav.isBody() ? mav.getView().getBytes() : getBytes(mav.getView()));
            }
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
        }
    }

    private static byte[] getBytes(String url) throws IOException {
        return Files.readAllBytes(new File(PREFIX + url).toPath());
    }
}
