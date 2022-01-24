package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class HttpResponseUtils {

    public static Map<String, String> parseReponseLine(String requestLine) {
        Map<String, String> ret = new HashMap<>();
        String[] parsed = requestLine.split(" ");
        ret.put("requestMethod", parsed[0]);
        ret.put("requestURL", parsed[1]);
        ret.put("requestProtocol", parsed[2]);
        return ret;
    }

    public static List<HttpRequestUtils.Pair> parseResponseHeader(BufferedReader br) throws IOException {
        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        String line;
        do {
            line = br.readLine();
            HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(line);
            if (header != null)
                headers.add(header);
        } while (!line.equals(""));

        return headers;
    }

    public static String extractMIME(List<HttpRequestUtils.Pair> headers){
        Optional<HttpRequestUtils.Pair> acceptHeader = headers.stream().filter(header -> header.getKey().equals("Accept")).findAny();
        if (acceptHeader.isPresent()) {
            return acceptHeader.get().getValue().split(",")[0];
        }
        return "";
    }

    public static byte[] matchURL(String requestURL) throws IOException {
        File file = new File("./webapp" + requestURL);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }
        return Files.readAllBytes(new File("./webapp/404.html").toPath());
    }
}
