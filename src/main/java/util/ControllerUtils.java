package util;

import model.HttpResponse;
import model.HttpResponseBuilder;

import java.nio.charset.StandardCharsets;

public class ControllerUtils {
    private ControllerUtils() {

    }

    public static HttpResponse redirect(String locationUri) {
        return HttpResponseBuilder.build(
                locationUri,
                "".getBytes(StandardCharsets.UTF_8),
                HttpResponseHeader.REDIRECT_302,
                "text/html"
        );
    }
}
