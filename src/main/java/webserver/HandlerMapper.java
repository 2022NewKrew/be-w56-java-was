package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HandlerMapper {
    private Request request;
    private Response response;

    public HandlerMapper(Request request, Response response){
        this.request = request;
        this.response = response;
    }

    public void start() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + request.getUri()).toPath());
        response.writeHeader(body.length, 200);
        response.writeBody(body);
    }
}
