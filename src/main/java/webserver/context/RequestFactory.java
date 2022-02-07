package webserver.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.IOUtils.readData;

public class RequestFactory {
    private static final Logger log = LoggerFactory.getLogger(RequestFactory.class);

    private static String readMethod(String line)  {
        return line.split(" ")[0];
    }

    private static String readUrl(String line)  {
        return line.split(" ")[1].split("\\?")[0];
    }

    private static Map<String,String> createParamMap(String line)  {
        String[] urlParams = line.split(" ")[1].split("\\?");
        if (urlParams.length >= 2){
            return HttpRequestUtils.parseQueryString(urlParams[1]);
        }
        return new HashMap<>();
    }

    private static List<String> readHeadersBy(BufferedReader br) throws IOException {
        List<String> headerLines = new ArrayList<>();
        String line = br.readLine();
        while (!line.equals("")){
            log.debug("header : {}", line);
            headerLines.add(line);
            line = br.readLine();
        }
        return headerLines;
    }

    private static Map<String,String> createHeaderDataMap(List<String> lines) {
        Map<String,String> headerDataMap = new HashMap<>();
        for (String line : lines) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headerDataMap.put(pair.getKey(), URLDecoder.decode(pair.getValue(), StandardCharsets.UTF_8));
        }
        return headerDataMap;
    }

    private static int getContentsLengthBy(Map<String,String> headerDataMap) {
        if (headerDataMap.containsKey("Content-Length")) {
            return Integer.parseInt(headerDataMap.get("Content-Length").strip());
        }
        return 0;
    }

    private static Map<String,String> createBodyDataMapByHeaderDataMap(BufferedReader br, int contentLength) throws IOException {
        String bodyData = readData(br, contentLength);
        log.debug("bodyData : {}", bodyData);
        return HttpRequestUtils.parseQueryString(bodyData);
    }

    public static Request createRequestBy(BufferedReader br) throws IOException {
        String firstLine = br.readLine();
        log.debug("request firstLine : {}", firstLine);
        if (firstLine == null) throw new IOException("Null Request");

        String method = readMethod(firstLine);
        String url = readUrl(firstLine);
        Map<String,String> paramMap = createParamMap(firstLine);
        Map<String,String> headerDataMap = createHeaderDataMap(readHeadersBy(br));
        Map<String,String> bodyDataMap = createBodyDataMapByHeaderDataMap(br, getContentsLengthBy(headerDataMap));

        return new Request(method, url, paramMap, headerDataMap, bodyDataMap);

    }
}
