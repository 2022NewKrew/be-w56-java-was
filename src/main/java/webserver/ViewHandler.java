package webserver;

import http.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewHandler {

    private static final String ROOT_RESOURCE_PATH = "./webapp";

    public static void handle(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        Path path = Paths.get(ROOT_RESOURCE_PATH + httpResponse.getView());
        httpResponse.writeBody(Files.readAllBytes(path));
        httpResponse.write(dos);
    }
}
