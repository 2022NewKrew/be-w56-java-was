package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import util.Constant;

public class HttpRequestFactory {

    public static final String CONTENT_LENGTH = "Content-Length";

    public static HttpRequest getHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String input = readInputStreamToEmpty(bufferedReader);
        String startLineString = getStartLineString(input);
        String headerString = getHeaderString(input);

        RequestStartLine startLine = RequestStartLine.stringToRequestLine(startLineString);
        RequestHeader header = RequestHeader.stringToRequestHeader(headerString);

        String bodyString = getBodyString(bufferedReader, header);

        RequestBody body = RequestBody.stringToRequestBody(bodyString);
        return new HttpRequest(startLine, header, body);
    }

    private static String readInputStreamToEmpty(BufferedReader bufferedReader)
            throws IOException {
        StringBuilder result = new StringBuilder();

        String line = bufferedReader.readLine();
        while (line != null && !line.isEmpty()) {
            result.append(line).append(Constant.lineBreak);
            line = bufferedReader.readLine();
        }

        return result.toString();
    }

    private static String readRequestBody(BufferedReader bufferedReader, int bodySize) {
        try {
            char[] cbuf = new char[bodySize];
            bufferedReader.read(cbuf);

            return new String(cbuf);
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    private static String getStartLineString(String input) {
        return input.split(Constant.lineBreak)[0];
    }

    private static String getHeaderString(String input) {
        return input.split(Constant.lineBreak, 2)[1];
    }

    private static String getBodyString(BufferedReader bufferedReader, RequestHeader header) {
        if (header.has(CONTENT_LENGTH)) {
            int bodyLength = Integer.parseInt(header.get(CONTENT_LENGTH));
            return readRequestBody(bufferedReader, bodyLength);
        }

        return "";
    }
}
