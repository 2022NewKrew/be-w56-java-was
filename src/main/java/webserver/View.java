package webserver;

import org.apache.tika.Tika;
import webserver.web.response.HttpStatus;
import webserver.web.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class View {

    private static final String PREFIX = "/Users/kakao/Desktop/be-w56-java-was/webapp/";
    private static final String HOST = "localhost:8080/";
    private final String viewPath;
    private final Model model;
    private final Response.ResponseBuilder builder;

    public View(String viewPath, Model model, Response.ResponseBuilder builder) {
        this.viewPath = viewPath;
        this.model = model;
        this.builder = builder;
    }

    public Response render() throws IOException {
        if (viewPath.contains("redirect")) {
            return builder.setStatus(HttpStatus.REDIRECT)
                    .setRedirectLocation(viewPath.split(":")[1])
                    .build();
        }
        File file = new File(PREFIX + viewPath);
        byte[] result = Files.readAllBytes(file.toPath());
        return builder.setStatus(HttpStatus.OK)
                .setResult(result)
                .setContentLength(result.length)
                .setContentType(new Tika().detect(file))
                .build();
    }
}
