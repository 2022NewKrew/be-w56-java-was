package webserver;

import http.RequestMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceContainer {

    public static byte[] process(RequestMessage request) {
        try {
            // TODO Path의 Get 수정
            File file = new File("./webapp" + request.getRequestLine().getRequestTarget().getPath().getValue());
            byte[] bytes = Files.readAllBytes(file.toPath());
            return bytes;
        } catch (IOException e) {
            return new byte[]{};
        }
    }
}
