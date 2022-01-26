package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HandleResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandleRequestHeader {
    private Map<String, String> requestParamMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(HandleRequestHeader.class);

    public void parseRequestHeader(BufferedReader br) throws IOException {
        String str = null;

        parseRequestLine(br.readLine());

        while (!(str = br.readLine()).equals("")) {
            log.debug("msg : {}", str);
            String[] split = str.split(":");
            requestParamMap.put(split[0].trim(), split[1].trim());
        }
    }

    public void parseRequestLine(String requestLine) {
        log.debug("msg : {}", requestLine);
        String[] requestLineArr = requestLine.split(" ");

        requestParamMap.put("method", requestLineArr[0]);
        requestParamMap.put("url", requestLineArr[1]);
        requestParamMap.put("http", requestLineArr[2]);
    }

    public String getUrl() {
        return requestParamMap.get("url");
    }

    public String getFirstAccept() {
        String accept = requestParamMap.get("Accept");
        return accept.split(",")[0];
    }
}
