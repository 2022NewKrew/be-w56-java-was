package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class MyRequest {

    private static final Logger log = LoggerFactory.getLogger(MyRequest.class);

    private static final int METHOD = 0;
    private static final int REQUEST_URI = 1;
    private static final int REQUEST_URL = 0;
    private static final int REQUEST_PARAMS = 1;
    private static final int HTTP_VERSION = 2;
    private static final int KEY = 0;
    private static final int VALUE = 1;

    private final RequestParams params = new RequestParams();
    private String method;
    private String requestUrl;
    private String httpVersion;
    private String connection;
    private String accept;


    private MyRequest() {
    }

    public MyRequest(InputStream httpRequestInputStream) throws IOException {
        BufferedReader httpRequestBufferedReader = new BufferedReader(new InputStreamReader(httpRequestInputStream, StandardCharsets.UTF_8));
        String request = httpRequestBufferedReader.readLine();
        log.info("requestHeader : {}", request);
        String[] requestInfo = request.split(" ");

        this.method = requestInfo[METHOD];
        this.httpVersion = requestInfo[HTTP_VERSION];

        updateRequestUri(requestInfo[REQUEST_URI]);

//        while (!(request = httpRequestBufferedReader.readLine()).equals(" ")) {
//            log.info("requestHeader : {}", request);
//        }
    }

    private void updateRequestUri(String uri) {
        String[] uriInfo = uri.split("\\?");
        this.requestUrl = uriInfo[REQUEST_URL];

        if (uriInfo.length > 1) {
            updateRequestParams(uriInfo[REQUEST_PARAMS]);
        }
    }

    private void updateRequestParams(String paramsString) {
        if (paramsString == null) {
            return;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(paramsString, "&");

        while (stringTokenizer.hasMoreTokens()) {
            String[] param = stringTokenizer.nextToken().split("=");
            String key = param[KEY];
            String value = param[VALUE];

            params.put(key, value);
        }
    }

    public String getPathVariable(String key) {
        return params.get(key);
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getConnection() {
        return connection;
    }

    public String getAccept() {
        return accept;
    }
}
