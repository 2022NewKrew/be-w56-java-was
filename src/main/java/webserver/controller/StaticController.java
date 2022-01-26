package webserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import webserver.Response;
import webserver.web.HttpStatus;
import webserver.web.request.Request;
import webserver.web.request.Url;

import javax.xml.stream.events.StartDocument;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StaticController implements Controller{

    private List<String> supplyUrl = new ArrayList<>();
    private static final String PREFIX = "/Users/kakao/Desktop/be-w56-java-was/webapp/";
    private static final String HOST = "localhost:8080/";

    public StaticController() {
        supplyUrl.add("/");
    }

    @Override
    public boolean isSupply(Request request) {
        String requestUrl = request.getUrl().toString();
        if(isMatched(requestUrl)) {
            return true;
        }
        return supplyUrl.stream().anyMatch(req -> req.equals(requestUrl));
    }

    @Override
    public Response handle(Request request, OutputStream out) throws IOException {
        Url url = request.getUrl();
        String target = url.getUrl().replace(HOST, "");
        if (target.equals("/"))
            target = "/index.html";
        File file = new File(PREFIX + target);
        byte[] result = Files.readAllBytes(file.toPath());
        return new Response.ResponseBuilder(out).setStatus(HttpStatus.OK)
                .setResult(result)
                .setContentLength(result.length)
                .setContentType(new Tika().detect(file))
                .build();
    }

    private boolean isMatched(String requestUrl) {
        return requestUrl.contains(".");
    }
}
