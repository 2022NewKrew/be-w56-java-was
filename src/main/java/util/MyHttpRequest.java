package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
    private final MyAttribute attribute = new MyAttribute();
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

        String contentLength = headers.get("Content-Length");
        if (contentLength != null) {
            log.debug("Content-Length : {}", contentLength);
            String entity = (read(httpRequestInputStream, Integer.parseInt(contentLength)));
            log.debug("Entity : {}", entity);
            // entity 값을 읽어 Params로 읽을 수 있도록 반영한다.
            // TODO: 객체바인딩, @RequestBody, 이거할려면 많은 시간이 걸릴것 같으므로 일단 Keep
            updateRequestParams(entity);
        }

    }

    private String read(final InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            int data = inputStream.read();

            if (data == '\n') {
                break;
            }

            if (data == '\r') {
                continue;
            }

            sb.append((char) data);
        }

        return sb.toString();
    }

    private String read(final InputStream inputStream, int contentLength) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < contentLength; i++) {
            sb.append((char) inputStream.read());
        }

        return sb.toString();
    }

    // accept 값중 첫번째 인자를 accept로 호출합니다.
    public String getAccept() {
        return headers.get("Accept").split(",")[0].trim();
    }

    public String getHost() {
        return headers.get("Host");
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

    public MyCookie getCookie() {
        return headers.getMyCookie();
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
        return new MyRequestDispatcher(viewPath);
    }

    public void setAttribute(String key, Object value) {
        attribute.set(key, value);
    }

    public MyAttribute getAttribute() {
        return attribute;
    }
}
