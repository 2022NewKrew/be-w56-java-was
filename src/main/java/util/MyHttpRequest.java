package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

public class MyHttpRequest {

    private static final Logger log = LoggerFactory.getLogger(MyHttpRequest.class);

    private static final int METHOD = 0;
    private static final int REQUEST_URI = 1;
    private static final int REQUEST_URL = 0;
    private static final int REQUEST_PARAMS = 1;
    private static final int HTTP_VERSION = 2;
    private static final int KEY = 0;
    private static final int VALUE = 1;

    private final RequestParams params = new RequestParams();
    private final MyHeaders headers = new MyHeaders();
    private MyHttpStatus method;
    private String requestURI;
    private String httpVersion;

    private MyHttpRequest() {
    }

    public MyHttpRequest(InputStream httpRequestInputStream) throws IOException {
        String request = read(httpRequestInputStream);
        log.debug("Line : {}", request);
        String[] requestInfo = request.split(" ");

        this.method = MyHttpStatus.valueOf(requestInfo[METHOD]);
        this.httpVersion = requestInfo[HTTP_VERSION];

        updateRequestUri(requestInfo[REQUEST_URI]);

        while (!(request = read(httpRequestInputStream)).isEmpty()) {
            log.debug("Header : {}", request);
            headers.set(request);
        }
    }

    private String read(final InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            char data = (char) inputStream.read();
            if (data == '\n') {
                break;
            }

            if (data == '\r') {
                continue;
            }

            sb.append(data);
        }

        return sb.toString();
    }

    // accept 값중 첫번째 인자를 accept로 호출합니다.
    public String getAccept() {
        return headers.get("accept").split(",")[0].trim();
    }

    private void updateRequestUri(String uri) {
        String[] uriInfo = uri.split("\\?");
        this.requestURI = uriInfo[REQUEST_URL];

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

    public MyHttpStatus getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public MyRequestDispatcher getRequestDispatcher(String viewPath) {
        //TODO redirectURI 만들기
        return new MyRequestDispatcher(viewPath);
    }
}
