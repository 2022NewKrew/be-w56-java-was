package webserver;

import http.Headers;
import http.RequestBody;
import http.RequestLine;
import http.RequestMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InputHandler {
    private InputHandler() {
    }

    public static RequestMessage receiveRequestMessage(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        RequestLine startLine = RequestLine.create(readStartLine(buffer));
        Headers requestHeader = Headers.create(readHeader(buffer));
        RequestBody requestBody = RequestBody.create(readData(buffer, requestHeader));
        return new RequestMessage(startLine, requestHeader, requestBody);
    }

    private static String readStartLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    private static List<String> readHeader(BufferedReader br) throws IOException {
        List<String> header = new ArrayList<>();
        String line;
        while (!(line = br.readLine()).equals("")) {
            header.add(line);
        }
        return header;
    }

    private static Optional<String> readData(BufferedReader br, Headers headers) throws IOException {
        String key = "Content-Length";
        Optional<String> contentLength = headers.sendContainKeyValue(key);
        if (contentLength.isEmpty()) {
            return Optional.empty();
        }
        int length = Integer.parseInt(contentLength.get());
        char[] body = new char[length];
        br.read(body, 0, length);
        return Optional.of(String.copyValueOf(body));
    }
}
