package webserver;

import http.HttpHeader;
import http.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

public class ViewHandler {

    private static final String ROOT_RESOURCE_PATH = "./webapp";
    private static final String LINE_BREAK = "\r\n";
    private static final String SPACE = " ";

    private ViewHandler() {
    }

    public static void handle(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        Path path = Paths.get(ROOT_RESOURCE_PATH + httpResponse.getPath());
        httpResponse.writeBody(Files.readAllBytes(path));
        writeStatusLine(dos, httpResponse);
        writeHeader(dos, httpResponse);
        writeBody(dos, httpResponse);
        dos.flush();
    }

    private static void writeStatusLine(DataOutputStream dos, HttpResponse httpResponse)
        throws IOException {
        dos.writeBytes(
            httpResponse.getVersion().toString() + SPACE + httpResponse.getStatus().toString()
                + LINE_BREAK);
    }

    private static void writeHeader(DataOutputStream dos, HttpResponse httpResponse)
        throws IOException {
        Map<HttpHeader, String> headers = httpResponse.getHeaders();
        for (Entry<HttpHeader, String> entry : headers.entrySet()) {
            dos.writeBytes(entry.getKey().toString() + ": " + entry.getValue() + LINE_BREAK);
        }
        dos.writeBytes(LINE_BREAK);
    }

    private static void writeBody(DataOutputStream dos, HttpResponse httpResponse)
        throws IOException {
        dos.write(httpResponse.getBody());
    }
}
