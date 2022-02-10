package springmvc;

import util.HtmlUtils;
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
                render(mav, httpResponse);
            }
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
        }
    }

    private static void render(ModelAndView mav, HttpResponse httpResponse) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (mav.getModel().isEmpty()) {
            httpResponse.setBody(Files.readAllBytes(new File(PREFIX + mav.getView()).toPath()));
        } else {
            httpResponse.setBody(HtmlUtils.getBytes(mav.getModel(), PREFIX + mav.getView()));
        }
    }
}
